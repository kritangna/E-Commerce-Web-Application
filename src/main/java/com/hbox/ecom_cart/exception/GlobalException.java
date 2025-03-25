package com.hbox.ecom_cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = {EcomCartException.class})
    public ResponseEntity<ErrorDetails> handleGlobalException(EcomCartException exception,
                                                        WebRequest webRequest) {
        System.out.println("*********Inside Global Exception**********");

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false)
        );

        HttpStatus status = exception.getStatus();
        return switch (status) {
            case BAD_REQUEST -> new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

            case UNAUTHORIZED -> new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);

            case FORBIDDEN -> new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);

            case NOT_FOUND -> new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

            default -> new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<ErrorDetails> handleUserNameNotFoundException(UsernameNotFoundException exception,
                                                                        WebRequest webRequest) {
        System.out.println("*********Username Not Found Exception**********");

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false)
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
