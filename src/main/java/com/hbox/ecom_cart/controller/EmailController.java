package com.hbox.ecom_cart.controller;

import com.hbox.ecom_cart.dto.EmailDto;
import com.hbox.ecom_cart.service.impl.EmailService;
import com.hbox.ecom_cart.service.impl.ProductServiceImpl;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/e-com-cart/email")
public class EmailController {

    private EmailService emailService;

    @PostMapping("send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDto emailDto) {
        try {
            emailService.sendEmail(emailDto);
            return ResponseEntity.ok("Email sent successfully!");
        }
        catch (MessagingException e){
            return new ResponseEntity<>("Failed to send the email! " + e.getMessage() , HttpStatus.BAD_REQUEST);
        }
    }
}
