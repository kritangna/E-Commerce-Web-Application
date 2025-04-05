package com.hbox.ecom_cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {

    private Long id;
    private String to;
    private String subject;
    private String body;
}
