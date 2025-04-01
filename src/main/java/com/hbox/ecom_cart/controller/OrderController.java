package com.hbox.ecom_cart.controller;

import com.hbox.ecom_cart.dto.OrderDto;
import com.hbox.ecom_cart.dto.OrderStatusDto;
import com.hbox.ecom_cart.entity.OrderStatus;
import com.hbox.ecom_cart.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/e-com-cart/orders")
public class OrderController {

    private OrderService orderService;

    @PostMapping("place-order")
    public ResponseEntity<OrderDto> placeOrder(@RequestBody OrderDto orderDto) {
        OrderDto placedOrderDto = orderService.placeOrder(orderDto);
        return new ResponseEntity<>(placedOrderDto, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderDto> getOrderByOrderId(@PathVariable("id") Long id) {
        OrderDto orderDto = orderService.getOrderByOrderId(id);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orderDtos = orderService.getAllOrders();
        return orderDtos.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<OrderDto> updateOrderStatusOfOrderById(@PathVariable("id") Long id, @RequestBody OrderStatusDto orderStatusDto) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, orderStatusDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteOrderById(@PathVariable("id") Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.ok("Order deleted successfully");
    }
}
