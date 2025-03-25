package com.hbox.ecom_cart.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class CustomObjectMapper {

    private final ObjectMapper objectMapper;

    public CustomObjectMapper(ObjectMapper objectMapper) {

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

}
