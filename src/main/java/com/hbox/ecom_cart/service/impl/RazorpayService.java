package com.hbox.ecom_cart.service.impl;

import com.hbox.ecom_cart.exception.EcomCartException;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RazorpayService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    public String createPaymentOrder(BigDecimal amount)
    {
        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpaySecret);

            JSONObject request = new JSONObject();
            request.put("amount", amount.multiply(new BigDecimal(100)));
            request.put("currency", "INR");
            request.put("receipt", "txn_123456");
            request.put("payment_capture", 1);

            System.out.println("request: " + request.toString());

            Order order = razorpayClient.orders.create(request);
            return order.get("id").toString();
        }
        catch(RazorpayException e)
        {
            throw new EcomCartException(HttpStatus.BAD_REQUEST, "Payment failed");
        }
    }
}
