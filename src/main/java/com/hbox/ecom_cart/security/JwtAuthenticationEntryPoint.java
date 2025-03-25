package com.hbox.ecom_cart.security;

import com.hbox.ecom_cart.config.CustomObjectMapper;
import com.hbox.ecom_cart.exception.ErrorDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private CustomObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // LocalDateTime currentTime = LocalDateTime.now();
        // String message = authException.getMessage();

        // Create error details
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "Unauthorized User",
                authException.getMessage()
        );

        // Convert error details to JSON
        String jsonResponse = objectMapper.getObjectMapper().writeValueAsString(errorDetails);

        // Set response properties
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Write response
        response.getWriter().write(jsonResponse);
    }
}
