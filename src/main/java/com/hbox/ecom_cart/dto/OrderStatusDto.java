package com.hbox.ecom_cart.dto;

import com.hbox.ecom_cart.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusDto {

    private OrderStatus orderStatus;
}
