package com.hbox.ecom_cart.service;

import java.util.Map;

public interface RazorpayPaymentTypeService {

    boolean verifyWebhookSignature(Map<String, Object> payload, String signature);
}
