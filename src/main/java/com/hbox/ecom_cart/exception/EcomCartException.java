package com.hbox.ecom_cart.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class EcomCartException extends RuntimeException {

    private HttpStatus status;
    private String message;
}
