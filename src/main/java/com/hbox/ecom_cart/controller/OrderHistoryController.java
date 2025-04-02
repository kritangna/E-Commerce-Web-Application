package com.hbox.ecom_cart.controller;

import com.hbox.ecom_cart.dto.OrderDto;
import com.hbox.ecom_cart.service.OrderHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/e-com-cart/order-history")
public class OrderHistoryController {

    private OrderHistoryService orderHistoryService;

    @GetMapping("{id}")
    public ResponseEntity<List<OrderDto>> getAllOrdersByCustomerId(@PathVariable("id") Long customerId) {
        return ResponseEntity.ok(orderHistoryService.getOrderHistoryByCustomerId(customerId));
    }
}
