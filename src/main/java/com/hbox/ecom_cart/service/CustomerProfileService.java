package com.hbox.ecom_cart.service;

import com.hbox.ecom_cart.dto.CustomerProfileDto;

import java.util.List;

public interface CustomerProfileService {

    CustomerProfileDto createCustomerProfile(Long userId, CustomerProfileDto customerProfileDto);

    CustomerProfileDto getCustomerProfileByUserId(Long UserId);

    List<CustomerProfileDto> getAllCustomerProfiles();

    CustomerProfileDto updateCustomerProfileByUserId(Long userId, CustomerProfileDto customerProfileDto);

    void deleteCustomerProfileByUserId(Long UserId);
}
