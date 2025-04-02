package com.hbox.ecom_cart.dto;


import com.hbox.ecom_cart.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProfileDto {

    private Long id;
    private User user;
    private String address;
    private String phoneNumber;
    private String email;
}
