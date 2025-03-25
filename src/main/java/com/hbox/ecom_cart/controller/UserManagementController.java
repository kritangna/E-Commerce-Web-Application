package com.hbox.ecom_cart.controller;

import com.hbox.ecom_cart.dto.UserDto;
import com.hbox.ecom_cart.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("api/e-com-cart/users")
@AllArgsConstructor
public class UserManagementController {

    private UserService userService;

    // Build REST API to Get User by Id - GET Method
    // http://127.0.0.1:8080/api/e-com-cart/users/1
    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId) {

        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    // Build REST API to Get All Users - GET Method
    // http://127.0.0.1:8080/api/e-com-cart/users
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {

        List<UserDto> userDtoList = userService.getAllUsers();
        return ResponseEntity.ok(userDtoList);
    }

    // Build REST API to Update Users based on Id - PUT Method
    // http://127.0.0.1:8080/api/e-com-cart/users/1
    @PutMapping("{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable("id") Long userId, @RequestBody UserDto userDto) {
        userDto.setId(userId);
        UserDto updatedUserDto = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(updatedUserDto);
    }

    // Build REST API to Delete User by id - DELETE Method
    // http://127.0.0.1:8080/api/e-com-cart/users/1
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User Deleted Successfully!");
    }

}
