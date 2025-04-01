package com.hbox.ecom_cart.service.impl;

import com.hbox.ecom_cart.dto.*;
import com.hbox.ecom_cart.entity.*;
import com.hbox.ecom_cart.exception.EcomCartException;
import com.hbox.ecom_cart.repositoty.*;
import com.hbox.ecom_cart.service.InventoryService;
import com.hbox.ecom_cart.service.OrderService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final CategoryRepository categoryRepository;
    private OrderItemRepository orderItemRepository;
    private ModelMapper modelMapper;
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private InventoryService inventoryService;
    private RazorpayService razorpayService;


    @Override
    public OrderDto placeOrder(OrderDto orderDto) {
        User user = userRepository.findById(orderDto.getUser().getId()).orElseThrow(() ->
                new EcomCartException(HttpStatus.BAD_REQUEST, "User not found"));
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for(OrderItemDto orderItemDto : orderDto.getOrderItems())
        {
            Product product = productRepository.findById(orderItemDto.getProductDto().getId()).orElseThrow(() ->
                    new EcomCartException(HttpStatus.BAD_REQUEST, "Product not found"));

            totalPrice = totalPrice.add(product.getProductPrice().multiply(new BigDecimal(orderItemDto.getQuantity())));
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);

            if(isQuantityAvailable(orderItemDto))
            {
                orderItem.setQuantity(orderItemDto.getQuantity());
                product.setProductStock(product.getProductStock() - orderItemDto.getQuantity());
            }
            else
            {
                throw new EcomCartException(HttpStatus.BAD_REQUEST, "Quantity not available");
            }
            orderItem.setPrice(totalPrice);
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        String razorpayOrderId = razorpayService.createPaymentOrder(totalPrice);
        System.out.println("Razorpay Order ID: " + razorpayOrderId);
        if(razorpayOrderId != null)
        {
            order.setOrderStatus(OrderStatus.PLACED);
        }
        else
        {
            order.setOrderStatus(OrderStatus.PENDING);
        }
        order.setRazorpayOrderId(razorpayOrderId);
        Order savedOrder = orderRepository.save(order);

        System.out.println("Order Items before saving: " + order.getOrderItems().size());
        OrderDto savedOrderDto = mapOrderToDto(savedOrder);
        return savedOrderDto;
    }

    @Override
    public OrderDto getOrderByOrderId(Long orderId) {
        Order existingOrder = orderRepository.findById(orderId).orElseThrow(() ->
                new EcomCartException(HttpStatus.BAD_REQUEST, "Order not found"));
        OrderDto orderDto = mapOrderToDto(existingOrder);
        return orderDto;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> orderDtos = new ArrayList<>();
        for(Order order : orders)
        {
            OrderDto orderDto = mapOrderToDto(order);
            orderDtos.add(orderDto);
        }
        return orderDtos;
       // return orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, OrderStatusDto orderStatusDto) {
        Order existingOrder = orderRepository.findById(orderId).orElseThrow(() ->
                new EcomCartException(HttpStatus.BAD_REQUEST, "Order not found"));

        existingOrder.setOrderStatus(modelMapper.map(orderStatusDto.getOrderStatus(), OrderStatus.class));
        Order savedOrder = orderRepository.save(existingOrder);
        OrderDto savedOrderDto = mapOrderToDto(savedOrder);
        return savedOrderDto;
    }

    @Override
    public void deleteOrderById(Long orderId) {
        Order existingOrder = orderRepository.findById(orderId).orElseThrow(() ->
                new EcomCartException(HttpStatus.BAD_REQUEST, "Order not found"));
        if(existingOrder.getOrderStatus() == OrderStatus.DELIVERED)
        {
            orderRepository.delete(existingOrder);
        }
    }

    private OrderDto mapOrderToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setUser(order.getUser());
        orderDto.setOrderDate(order.getOrderDate());

        List<OrderItemDto> orderItems = order.getOrderItems().stream().map((orderItem) -> {
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setId(orderItem.getId());
            orderItemDto.setQuantity(orderItem.getQuantity());
            orderItemDto.setPrice(orderItem.getPrice());

            ProductDto productDto = new ProductDto();
            productDto.setId(orderItem.getProduct().getId());
            productDto.setProductName(orderItem.getProduct().getProductName());
            productDto.setProductDescription(orderItem.getProduct().getProductDescription());
            productDto.setProductPrice(orderItem.getProduct().getProductPrice());
            productDto.setProductStock(orderItem.getProduct().getProductStock());

            Category category = categoryRepository.findById(orderItem.getProduct().getCategory().getId()).orElseThrow(() -> new EcomCartException(HttpStatus.BAD_REQUEST, "Category not found"));
            productDto.setCategoryDto(modelMapper.map(category, CategoryDto.class));
            orderItemDto.setProductDto(productDto);
            return orderItemDto;
        }
        ).collect(Collectors.toList());
        orderDto.setOrderItems(orderItems);
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setRazorpayOrderId(order.getRazorpayOrderId());
        return orderDto;
    }

    private boolean isQuantityAvailable(OrderItemDto orderItemDto)
    {
        Product product = productRepository.findById(orderItemDto.getProductDto().getId()).orElseThrow(() ->
                new EcomCartException(HttpStatus.BAD_REQUEST, "Product not found"));
        Integer availableQuantity = product.getProductStock();
        if(availableQuantity < orderItemDto.getQuantity())
        {
            throw new EcomCartException(HttpStatus.BAD_REQUEST, "Quantity not available for the product: " + product.getProductName());
        }
        return true;
    }
}
