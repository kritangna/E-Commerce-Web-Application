package com.hbox.ecom_cart.service.impl;

import com.hbox.ecom_cart.entity.Product;
import com.hbox.ecom_cart.exception.EcomCartException;
import com.hbox.ecom_cart.repositoty.ProductRepository;
import com.hbox.ecom_cart.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {

    private ProductRepository productRepository;

    @Override
    public void updateStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new EcomCartException(HttpStatus.BAD_REQUEST, "Product not found"));
        Integer currentStock = product.getProductStock();
//        if(currentStock < quantity) {
//            throw new EcomCartException(HttpStatus.BAD_REQUEST, "Product not enough");
//        }
        product.setProductStock(currentStock + quantity);
        productRepository.save(product);
    }

    @Override
    public Integer checkStock(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new EcomCartException(HttpStatus.BAD_REQUEST, "Product not found"));
        return product.getProductStock();
    }
}
