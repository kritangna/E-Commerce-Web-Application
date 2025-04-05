package com.hbox.ecom_cart.controller;

import com.hbox.ecom_cart.dto.ProductDto;
import com.hbox.ecom_cart.entity.Product;
import com.hbox.ecom_cart.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/e-com-cart/products")
public class ProductController {

    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        ProductDto addedProduct = productService.addProduct(productDto);
        return new ResponseEntity<>(addedProduct,HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDto productDto) {
        ProductDto updatedProduct = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id) {
        ProductDto productDto = productService.getProductById(id);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productDtos = productService.getAllProducts();
        return ResponseEntity.ok(productDtos);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>("Product deleted", HttpStatus.OK);
    }

    @GetMapping("product-list")
    public Page<Product> getAllProductsInPage(@RequestParam int page, @RequestParam int size) {
        return productService.getAllProductsInPage(page, size);
    }

}
