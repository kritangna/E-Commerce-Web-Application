package com.hbox.ecom_cart.dto;

import com.hbox.ecom_cart.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {

    private Long id;
   // private OrderDto orderDto;
    private ProductDto productDto;
    private Integer quantity;
    private BigDecimal price;
}
