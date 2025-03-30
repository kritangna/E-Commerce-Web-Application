package com.hbox.ecom_cart.service;

import com.hbox.ecom_cart.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto addProduct(ProductDto productDto);

    ProductDto updateProduct(Long id, ProductDto productDto);

    ProductDto getProductById(Long id);

   // ProductDto getProductByName(String name);

    List<ProductDto> getAllProducts();

    void deleteProductById(Long id);

}
