package com.hbox.ecom_cart.controller;


import com.hbox.ecom_cart.service.impl.RazorpayService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


@AllArgsConstructor
@RestController
@RequestMapping("api/e-com-cart/payments")
public class RazorpayController {

    private RazorpayService razorpayService;

    @PostMapping("razorpay/create-order")
    public ResponseEntity<String> createRazorpayOrder(@RequestParam BigDecimal amount)
    {
        String response = razorpayService.createPaymentOrder(amount);

        return new ResponseEntity<>("Payment successful: " + response, HttpStatus.CREATED);
    }
}
