package com.hbox.ecom_cart.service;

import com.hbox.ecom_cart.dto.OrderDto;
import com.hbox.ecom_cart.dto.OrderStatusDto;
import com.hbox.ecom_cart.entity.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderDto placeOrder(OrderDto orderDto);

    OrderDto getOrderByOrderId(Long orderId);

    List<OrderDto> getAllOrders();

    OrderDto updateOrderStatus(Long orderId, OrderStatusDto orderStatusDto);

    void deleteOrderById(Long orderId);
}
