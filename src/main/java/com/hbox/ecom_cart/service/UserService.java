package com.hbox.ecom_cart.service;

import com.hbox.ecom_cart.dto.LoginDto;
import com.hbox.ecom_cart.dto.RegisterDto;
import com.hbox.ecom_cart.dto.UserDto;

import java.util.List;


public interface UserService {

    String registerUser(RegisterDto registerDto);

    String loginUser(LoginDto loginDto);

    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);
}
