package com.hbox.ecom_cart.service;

import com.hbox.ecom_cart.dto.ProductDto;
import com.hbox.ecom_cart.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    ProductDto addProduct(ProductDto productDto);

    ProductDto updateProduct(Long id, ProductDto productDto);

    ProductDto getProductById(Long id);

   // ProductDto getProductByName(String name);

    List<ProductDto> getAllProducts();

    Page<Product> getAllProductsInPage(int page, int size);

    void deleteProductById(Long id);

}
