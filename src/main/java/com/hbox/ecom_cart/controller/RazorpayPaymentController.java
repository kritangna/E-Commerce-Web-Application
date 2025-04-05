package com.hbox.ecom_cart.controller;

import com.hbox.ecom_cart.entity.RazorpayPaymentType;
import com.hbox.ecom_cart.repositoty.RazorpayPaymentTypeRepository;

import com.hbox.ecom_cart.service.RazorpayPaymentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("api/e-com-cart/razorpay-payments")
public class RazorpayPaymentController {

    private final RazorpayPaymentTypeRepository razorpayPaymentTypeRepository;
    private final RazorpayPaymentTypeService razorpayPaymentTypeService;


    @Autowired
    public RazorpayPaymentController(RazorpayPaymentTypeRepository razorpayPaymentTypeRepository,
                                     @Qualifier("razorpayPaymentTypeService") RazorpayPaymentTypeService razorpayPaymentTypeService) {
        this.razorpayPaymentTypeRepository = razorpayPaymentTypeRepository;
        this.razorpayPaymentTypeService = razorpayPaymentTypeService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload, @RequestHeader("X-Razorpay-Signature") String signature) {
        try {
            // Verify webhook signature (important for security)
            boolean isValid = razorpayPaymentTypeService.verifyWebhookSignature(payload, signature);
            if (!isValid) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
            }

            // Extract payment details
            Map<String, Object> payloadData = (Map<String, Object>) payload.get("payload");
            Map<String, Object> payment = (Map<String, Object>) payloadData.get("payment");
            Map<String, Object> paymentEntity = (Map<String, Object>) payment.get("response");

            String paymentId = (String) paymentEntity.get("id");
            String method = (String) paymentEntity.get("method"); // "card", "upi", "wallet", etc.
            String status = (String) paymentEntity.get("status"); // "captured", "failed", etc.

            System.out.println("Payment ID: " + paymentId);
            System.out.println("Payment Method: " + method);
            System.out.println("Payment Status: " + status);

            // Store payment details in database
            RazorpayPaymentType paymentType = new RazorpayPaymentType();
            paymentType.setId(paymentId);
            paymentType.setPaymentType(method);
            paymentType.setPaymentStatus(status);
           // try {
                System.out.println("Payment ID is : " + paymentType.getId());
                RazorpayPaymentType paymentCheck = razorpayPaymentTypeRepository.save(paymentType);
                System.out.println("Payment Check: " + paymentCheck);
//            }catch (Exception e){
//                e.printStackTrace();  // Print the actual error in console
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database save error: " + e.getMessage());
//            }


            return ResponseEntity.ok("Webhook received successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
        }
    }
}
