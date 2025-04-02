package com.hbox.ecom_cart.service;

import com.hbox.ecom_cart.dto.OrderDto;

import java.util.List;

public interface OrderHistoryService {

    List<OrderDto> getOrderHistoryByCustomerId(Long customerId);
}
