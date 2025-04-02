package com.hbox.ecom_cart.service.impl;

import com.hbox.ecom_cart.dto.*;
import com.hbox.ecom_cart.entity.*;
import com.hbox.ecom_cart.exception.EcomCartException;
import com.hbox.ecom_cart.repositoty.*;
import com.hbox.ecom_cart.service.OrderHistoryService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderHistoryServiceImpl implements OrderHistoryService {

    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private CustomerProfileRespository customerProfileRespository;
    private ModelMapper modelMapper;

    @Override
    public List<OrderDto> getOrderHistoryByCustomerId(Long customerId) {
        CustomerProfile customer = customerProfileRespository.findById(customerId).orElseThrow(() ->
                new EcomCartException(HttpStatus.BAD_REQUEST, "Customer Not Found!"));

        List<Order> orders = orderRepository.findByCustomerProfile(customer);
        List<OrderDto> orderDtoList = orders.stream().map((order) -> mapOrderToDto(order)).collect(Collectors.toList());
        return orderDtoList;
    }

    private OrderDto mapOrderToDto(Order order)
    {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setRazorpayOrderId(order.getRazorpayOrderId());
        User user = userRepository.findById(order.getCustomerProfile().getUser().getId()).orElseThrow(() -> new EcomCartException(HttpStatus.BAD_REQUEST, "User Not Found"));
        orderDto.setUserDto(modelMapper.map(user, UserDto.class));
        List<OrderItemDto> items = order.getOrderItems().stream().map((orderItem) -> {
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
        }).collect(Collectors.toList());
        orderDto.setOrderItems(items);
        CustomerProfile customer = customerProfileRespository.findById(order.getCustomerProfile().getId()).orElseThrow(() -> new EcomCartException(HttpStatus.BAD_REQUEST, "Customer Not Found"));
        orderDto.setCustomerProfileDto(modelMapper.map(customer, CustomerProfileDto.class));
        return orderDto;
    }
}
