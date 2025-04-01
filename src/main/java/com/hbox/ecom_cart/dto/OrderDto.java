package com.hbox.ecom_cart.dto;


import com.hbox.ecom_cart.entity.OrderItem;
import com.hbox.ecom_cart.entity.OrderStatus;
import com.hbox.ecom_cart.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;
    private User user;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private List<OrderItemDto> orderItems;
    private String razorpayOrderId;
}
