package com.hbox.ecom_cart.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WishListDto {

    private Long id;
    private CustomerProfileDto customerProfileDto;
    private ProductDto productDto;
}
