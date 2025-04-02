package com.hbox.ecom_cart.controller;

import com.hbox.ecom_cart.dto.CustomerProfileDto;

import com.hbox.ecom_cart.service.CustomerProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/e-com-cart/profiles")
public class CustomerProfileController {

    private CustomerProfileService customerProfileService;

    @PostMapping("{id}")
    public ResponseEntity<CustomerProfileDto> createCustomerProfile(@PathVariable("id") Long userId, @RequestBody CustomerProfileDto customerProfileDto) {
        CustomerProfileDto newCustomerProfileDto = customerProfileService.createCustomerProfile(userId, customerProfileDto);
        return new ResponseEntity<>(newCustomerProfileDto, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<CustomerProfileDto> updateCustomerProfile(@PathVariable("id") Long userId, @RequestBody CustomerProfileDto customerProfileDto) {
        CustomerProfileDto customerDto = customerProfileService.updateCustomerProfileByUserId(userId, customerProfileDto);
        return new ResponseEntity<>(customerDto, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomerProfileDto> getCustomerProfileByUserId(@PathVariable("id") Long userId) {
        CustomerProfileDto customerProfileDto = customerProfileService.getCustomerProfileByUserId(userId);
        return new ResponseEntity<>(customerProfileDto, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<CustomerProfileDto>> getCustomerAllProfiles() {
        System.out.println("********** Inside getCustomerAllProfiles **********");
        List<CustomerProfileDto> customers = customerProfileService.getAllCustomerProfiles();
        return ResponseEntity.ok(customers);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCustomerProfile(@PathVariable("id") Long userId) {
        customerProfileService.deleteCustomerProfileByUserId(userId);
        return new ResponseEntity<>("Customer profile deleted", HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Profiles endpoint is working!");
    }
}
