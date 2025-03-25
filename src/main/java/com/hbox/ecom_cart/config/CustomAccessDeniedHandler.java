package com.hbox.ecom_cart.config;

import com.hbox.ecom_cart.exception.ErrorDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private CustomObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // Create error details
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "Unauthorized User",
                accessDeniedException.getMessage()
        );

        // Convert error details to JSON
        String jsonResponse = objectMapper.getObjectMapper().writeValueAsString(errorDetails);

        // Set response properties
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
}
