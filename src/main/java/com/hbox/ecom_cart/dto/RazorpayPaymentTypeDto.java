package com.hbox.ecom_cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RazorpayPaymentTypeDto {

    private String id;
    private String paymentType;
    private String paymentStatus;
}
