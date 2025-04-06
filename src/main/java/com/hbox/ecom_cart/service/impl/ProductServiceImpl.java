package com.hbox.ecom_cart.service.impl;

import com.hbox.ecom_cart.dto.CategoryDto;
import com.hbox.ecom_cart.dto.ProductDto;
import com.hbox.ecom_cart.entity.Category;
import com.hbox.ecom_cart.entity.Product;
import com.hbox.ecom_cart.exception.EcomCartException;
import com.hbox.ecom_cart.repositoty.CategoryRepository;
import com.hbox.ecom_cart.repositoty.ProductRepository;
import com.hbox.ecom_cart.service.ProductService;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        Category category = loadCategory(productDto);
        Product product = modelMapper.map(productDto, Product.class);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return getProductWithCategory(modelMapper.map(savedProduct, ProductDto.class));
    }

    @CacheEvict(value = "products", key = "#id") // Invalidates cache in case of an update
    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new EcomCartException(HttpStatus.BAD_REQUEST, "Product not found"));

        if(productDto.getProductName() != null && !productDto.getProductName().equals(existingProduct.getProductName())) {
            existingProduct.setProductName(productDto.getProductName());
        }

        if(productDto.getProductDescription() != null && !productDto.getProductDescription().equals(existingProduct.getProductDescription())) {
            existingProduct.setProductDescription(productDto.getProductDescription());
        }

        if(productDto.getProductPrice() != null && !productDto.getProductPrice().equals(existingProduct.getProductPrice())) {
            existingProduct.setProductPrice(productDto.getProductPrice());
        }

        if(productDto.getProductStock() != null && !productDto.getProductStock().equals(existingProduct.getProductStock())) {
            existingProduct.setProductStock(productDto.getProductStock());
        }

        if(productDto.getCategoryDto() != null){
            Category category = loadCategory(productDto);
            existingProduct.setCategory(category);
        }
        Product savedProduct = productRepository.save(existingProduct);
        return getProductWithCategory(modelMapper.map(savedProduct, ProductDto.class));
    }

    @Cacheable(value = "products", key = "#id")
    @Override
    public ProductDto getProductById(Long id) {
        System.out.println("Fetching product from the db");
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new EcomCartException(HttpStatus.BAD_REQUEST, "Product not found"));
        Category category = loadCategory(modelMapper.map(existingProduct, ProductDto.class));
        return getProductWithCategory(modelMapper.map(existingProduct, ProductDto.class));
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();;
        for(Product product : products) {
            productDtos.add(getProductWithCategory(modelMapper.map(product, ProductDto.class)));
        }
        return productDtos;
    }

    // Using Caching technique to GET a certain number of products in 1 page
    @Cacheable(value = "products", key = "#page + '-' + #size")
    @Override
    public Page<Product> getAllProductsInPage(int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        return productRepository.findAll(pageable);
    }

    @Override
    public void deleteProductById(Long id) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() ->
                new EcomCartException(HttpStatus.BAD_REQUEST, "Product not found"));
        productRepository.delete(existingProduct);
    }

    private ProductDto getProductWithCategory(ProductDto productDto)
    {
        Category category = loadCategory(productDto);
        CategoryDto categoryDto = new CategoryDto();
        if(category != null)
        {
            categoryDto.setId(category.getId());
        }
        if(category.getCategoryName() != null) {
            categoryDto.setCategoryName(category.getCategoryName());
        }

        return new ProductDto(
                productDto.getId(),
                productDto.getProductName(),
                productDto.getProductDescription(),
                productDto.getProductPrice(),
                productDto.getProductStock(),
                categoryDto
        );
    }

    private Category loadCategory(ProductDto productDto)
    {
        CategoryDto categoryDto = productDto.getCategoryDto();
        Category category = new Category();
        if(categoryDto != null) {
            category = categoryRepository.findById(categoryDto.getId()).orElseThrow(() -> new EcomCartException(HttpStatus.NOT_FOUND, "Category not found"));
        }
        else
        {
            System.out.println("CategoryDto is empty in ProductDto");
        }
        return category;
    }
}
