package com.hbox.ecom_cart.service.impl;

import com.hbox.ecom_cart.dto.LoginDto;
import com.hbox.ecom_cart.dto.RegisterDto;
import com.hbox.ecom_cart.dto.UserDto;
import com.hbox.ecom_cart.entity.Role;
import com.hbox.ecom_cart.entity.User;
import com.hbox.ecom_cart.exception.EcomCartException;
import com.hbox.ecom_cart.repositoty.UserRepository;
import com.hbox.ecom_cart.security.JwtTokenProvider;
import com.hbox.ecom_cart.service.UserService;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public String registerUser(RegisterDto registerDto) {

        System.out.println("**********Inside RegisterUser**********");
        // Check if the Username already exists
        if(userRepository.existsByUsername(registerDto.getUsername())) {
            throw new EcomCartException(HttpStatus.BAD_REQUEST, "Username Already Exists!");
        }

        // Check if the Email already exists
        if(userRepository.existsByEmail(registerDto.getEmail())) {
            throw new EcomCartException(HttpStatus.BAD_REQUEST, "Email Already Exists!");
        }

        User newUser = new User();
        newUser.setFirstName(registerDto.getFirstName());
        newUser.setLastName(registerDto.getLastName());
        newUser.setUsername(registerDto.getUsername());
        newUser.setEmail(registerDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        newUser.setRole(registerDto.getRole() != null ? registerDto.getRole() : Role.CUSTOMER);
        userRepository.save(newUser);
        return "User registered Successfully!";
    }

    @Override
    public String loginUser(LoginDto loginDto) {

        System.out.println("***************Inside LoginUser****************");
        System.out.println(loginDto.getEmail());
        System.out.println(loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword()
        ));

        System.out.println(loginDto.getEmail());
        System.out.println(loginDto.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("Get Email and Password after setting authentication");
        System.out.println(loginDto.getEmail());
        System.out.println(loginDto.getPassword());

        String token = jwtTokenProvider.generateToken(authentication);

        System.out.println("token = " + token);

        return token;
    }

    @Override
    public UserDto getUserById(Long id) {

        System.out.println("**********Inside Get User By Id**********");
        User user = userRepository.findById(id).orElseThrow(() ->
                new EcomCartException(HttpStatus.BAD_REQUEST, "User not found with id: " + id));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        System.out.println("**********Inside Get All Users**********");
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = userList.stream().map((user) ->
                modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        return userDtoList;
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {

        User existingUser = userRepository.findById(id).orElseThrow(() ->
                new EcomCartException(HttpStatus.BAD_REQUEST, "User not found with id: " + id));

        if(userDto.getFirstName() != null && !userDto.getFirstName().equals(existingUser.getFirstName()))
        {
            existingUser.setFirstName(userDto.getFirstName());
        }

        if(userDto.getLastName() != null && !userDto.getLastName().equals(existingUser.getLastName()))
        {
            existingUser.setLastName(userDto.getLastName());
        }

        if(userDto.getUsername() != null && !userDto.getUsername().equals(existingUser.getUsername()))
        {
            existingUser.setUsername(userDto.getUsername());
        }

        if(userDto.getEmail() != null && !userDto.getEmail().equals(existingUser.getEmail()))
        {
            existingUser.setEmail(userDto.getEmail());
        }

        if(userDto.getPassword() != null && !userDto.getPassword().equals(existingUser.getPassword()))
        {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        User savedUser = userRepository.save(existingUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id).orElseThrow(() ->
                new EcomCartException(HttpStatus.BAD_REQUEST, "User Not Found with id: " + id));
        userRepository.deleteById(id);
    }

}
