package com.hbox.ecom_cart.controller;


import com.hbox.ecom_cart.dto.JwtAuthResponseDto;
import com.hbox.ecom_cart.dto.LoginDto;
import com.hbox.ecom_cart.dto.RegisterDto;
import com.hbox.ecom_cart.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/e-com-cart/users")
@AllArgsConstructor
public class UserRegistrationController {

    private UserService userService;

    // Build REST API to Add User - POST Method
    // http://127.0.0.1:8080/api/e-com-cart/users/register
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDto registerDto) {

        String response = userService.registerUser(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Build REST API to Login User - POST Method
    // http://127.0.0.1:8080/api/e-com-cart/users/login
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDto> loginUser(@RequestBody LoginDto loginDto) {

        String token = userService.loginUser(loginDto);
        JwtAuthResponseDto jwtAuthResponseDto = new JwtAuthResponseDto();
        jwtAuthResponseDto.setAccessToken(token);
        return new ResponseEntity<>(jwtAuthResponseDto, HttpStatus.OK);
    }
}
