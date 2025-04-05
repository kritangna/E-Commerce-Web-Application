package com.hbox.ecom_cart.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hbox.ecom_cart.entity.Category;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable {

    private Long id;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private Integer productStock;
    private CategoryDto categoryDto;
}
