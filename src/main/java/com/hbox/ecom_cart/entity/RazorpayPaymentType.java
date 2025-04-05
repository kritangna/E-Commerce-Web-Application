package com.hbox.ecom_cart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="razorpay_payments")
public class RazorpayPaymentType {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String paymentType;
    private String paymentStatus;
}
