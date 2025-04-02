package com.hbox.ecom_cart.service.impl;


import com.hbox.ecom_cart.dto.CustomerProfileDto;
import com.hbox.ecom_cart.entity.CustomerProfile;
import com.hbox.ecom_cart.entity.User;
import com.hbox.ecom_cart.exception.EcomCartException;
import com.hbox.ecom_cart.repositoty.CustomerProfileRespository;
import com.hbox.ecom_cart.repositoty.UserRepository;
import com.hbox.ecom_cart.service.CustomerProfileService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class CustomerProfileImpl implements CustomerProfileService {

    private UserRepository userRepository;
    private CustomerProfileRespository customerProfileRespository;

    @Override
    public CustomerProfileDto createCustomerProfile(Long userId, CustomerProfileDto customerProfileDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EcomCartException(HttpStatus.NOT_FOUND, "User not found"));

        if(!user.getId().equals(customerProfileDto.getUser().getId())) {
            throw new EcomCartException(HttpStatus.FORBIDDEN, "User is not owner of this profile");
        }

        CustomerProfile customerProfile = customerProfileRespository.findByUserId(userId);

        if(customerProfile == null) {
            customerProfile = new CustomerProfile(
                    customerProfileDto.getId(),
                    user,
                    customerProfileDto.getAddress(),
                    customerProfileDto.getPhoneNumber(),
                    customerProfileDto.getEmail()
            );
            CustomerProfile newCustomer = customerProfileRespository.save(customerProfile);
            CustomerProfileDto newCustomerDto = mapCustomerProfileToDto(newCustomer);
            return newCustomerDto;
        }
        else
        {
            throw new EcomCartException(HttpStatus.BAD_REQUEST, "Customer profile already exists");
        }

    }

    @Override
    public CustomerProfileDto getCustomerProfileByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EcomCartException(HttpStatus.NOT_FOUND, "User not found"));
        CustomerProfile customerProfile = customerProfileRespository.findByUserId(userId);
        if(customerProfile == null) {
            throw new EcomCartException(HttpStatus.NOT_FOUND, "Customer not found");
        }
        CustomerProfileDto customerProfileDto = mapCustomerProfileToDto(customerProfile);
        return customerProfileDto;
    }

    @Override
    public List<CustomerProfileDto> getAllCustomerProfiles() {
        List<CustomerProfile> customerProfiles = customerProfileRespository.findAll();
        List<CustomerProfileDto> customerProfileDtos = new ArrayList<>();
        for (CustomerProfile customerProfile : customerProfiles) {
            CustomerProfileDto customerProfileDto = mapCustomerProfileToDto(customerProfile);
            customerProfileDtos.add(customerProfileDto);
        }

        return customerProfileDtos;
    }

    @Override
    public CustomerProfileDto updateCustomerProfileByUserId(Long userId, CustomerProfileDto customerProfileDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EcomCartException(HttpStatus.NOT_FOUND, "User not found"));
        CustomerProfile customerProfile = customerProfileRespository.findByUserId(userId);
        if(customerProfile == null) {
            throw new EcomCartException(HttpStatus.NOT_FOUND, "Customer profile not found");
        }

        customerProfile.setUser(user);
        if(customerProfileDto.getAddress() != null && !customerProfileDto.getAddress().equals(customerProfile.getAddress())) {
            customerProfile.setAddress(customerProfileDto.getAddress());
        }

        if(customerProfileDto.getEmail() != null && !customerProfileDto.getEmail().equals(customerProfile.getEmail())) {
            customerProfile.setEmail(customerProfileDto.getEmail());
        }

        if(customerProfileDto.getPhoneNumber() != null && !customerProfileDto.getPhoneNumber().equals(customerProfile.getPhoneNumber())) {
            customerProfile.setPhoneNumber(customerProfileDto.getPhoneNumber());
        }

        CustomerProfile updatedCustomerProfile = customerProfileRespository.save(customerProfile);
        CustomerProfileDto customerDto = mapCustomerProfileToDto(updatedCustomerProfile);
        return customerDto;
    }

    @Override
    public void deleteCustomerProfileByUserId(Long UserId) {
        User user = userRepository.findById(UserId).orElseThrow(() -> new EcomCartException(HttpStatus.NOT_FOUND, "User not found"));

        CustomerProfile customerProfile = customerProfileRespository.findByUserId(UserId);
        if(customerProfile == null) {
            throw new EcomCartException(HttpStatus.NOT_FOUND, "Customer profile not found");
        }
        else {
            customerProfileRespository.delete(customerProfile);
        }
    }

    private CustomerProfileDto mapCustomerProfileToDto(CustomerProfile customerProfile)
    {
        CustomerProfileDto customerProfileDto = new CustomerProfileDto();
        customerProfileDto.setId(customerProfile.getId());

        User user = customerProfile.getUser();
        if (user != null) {
            user.setId(customerProfile.getUser().getId());
            user.setFirstName(customerProfile.getUser().getFirstName());
            user.setLastName(customerProfile.getUser().getLastName());
            user.setUsername(customerProfile.getUser().getUsername());
            user.setEmail(customerProfile.getUser().getEmail());
            user.setPassword(customerProfile.getUser().getPassword());
            user.setRole(customerProfile.getUser().getRole());
        }
        customerProfileDto.setUser(customerProfile.getUser());
        customerProfileDto.setAddress(customerProfile.getAddress());
        customerProfileDto.setEmail(customerProfile.getEmail());
        customerProfileDto.setPhoneNumber(customerProfile.getPhoneNumber());
        return customerProfileDto;
    }
}
