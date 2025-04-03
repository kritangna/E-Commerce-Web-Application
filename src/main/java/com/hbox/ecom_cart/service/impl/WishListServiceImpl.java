package com.hbox.ecom_cart.service.impl;

import com.hbox.ecom_cart.dto.CategoryDto;
import com.hbox.ecom_cart.dto.CustomerProfileDto;
import com.hbox.ecom_cart.dto.ProductDto;
import com.hbox.ecom_cart.dto.WishListDto;
import com.hbox.ecom_cart.entity.*;
import com.hbox.ecom_cart.exception.EcomCartException;
import com.hbox.ecom_cart.repositoty.*;
import com.hbox.ecom_cart.service.CustomerProfileService;
import com.hbox.ecom_cart.service.WishListService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WishListServiceImpl implements WishListService {

    private ModelMapper modelMapper;
    private CustomerProfileRespository customerProfileRespository;
    private WishListRepository wishListRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;


    @Override
    public WishListDto addWishList(Long customerProfileId, ProductDto productDto) {
        CustomerProfile customer = customerProfileRespository.findById(customerProfileId).orElseThrow(() -> new EcomCartException(HttpStatus.BAD_REQUEST,"Customer Profile Not Found"));
        User user = userRepository.findById(customer.getUser().getId()).orElseThrow(() -> new EcomCartException(HttpStatus.BAD_REQUEST, "User Not Found"));
        Long productId = productDto.getId();

        System.out.println("Product Id: " + productId);

        Product product = productRepository.findById(productId).orElseThrow(() -> new EcomCartException(HttpStatus.BAD_REQUEST, "Product Not Found"));
        Category category = categoryRepository.findById(product.getCategory().getId()).orElseThrow(() -> new EcomCartException(HttpStatus.BAD_REQUEST, "Category Not Found"));
        product.setCategory(category);
        customer.setUser(user);
        WishList wishList = new WishList();
        if(!wishListRepository.existsByCustomerProfileAndProduct(customer, product)) {
            wishList.setCustomerProfile(customer);
            wishList.setProduct(product);
            wishListRepository.save(wishList);
        }
        else
        {
            throw new EcomCartException(HttpStatus.CONFLICT,"Item Already Exists in your wishlist");
        }
        WishListDto wishlistDto = new WishListDto();
        wishlistDto.setId(wishList.getId());
        ProductDto prodDto = mapProductToDto(product);
        CustomerProfileDto customerDto = mapCustomerToDto(customer);
        wishlistDto.setCustomerProfileDto(customerDto);
        wishlistDto.setProductDto(prodDto);
        return wishlistDto;
       // return modelMapper.map(wishList, WishListDto.class);
    }

    @Override
    public List<ProductDto> getWishListByCustomerId(Long customerId) {
        CustomerProfile customer = customerProfileRespository.findById(customerId).orElseThrow(() -> new EcomCartException(HttpStatus.BAD_REQUEST,"Customer Profile Not Found"));
        List<WishList> wishLists = wishListRepository.findByCustomerProfileId(customerId);
        List<ProductDto> products = wishLists.stream().map(wishList -> modelMapper.map(wishList, ProductDto.class)).collect(Collectors.toList());

        return products;
    }

    private ProductDto mapProductToDto(Product product) {
        CategoryDto categoryDto = modelMapper.map(product.getCategory(), CategoryDto.class);
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setProductName(product.getProductName());
        productDto.setProductDescription(product.getProductDescription());
        productDto.setProductPrice(product.getProductPrice());
        productDto.setProductStock(product.getProductStock());
        productDto.setCategoryDto(categoryDto);
        return productDto;
    }

    private CustomerProfileDto mapCustomerToDto(CustomerProfile customer) {
        CustomerProfileDto customerDto = new CustomerProfileDto();
        customerDto.setId(customer.getId());
        customerDto.setEmail(customer.getEmail());
        customerDto.setAddress(customer.getAddress());
        customerDto.setPhoneNumber(String.valueOf(customer.getPhoneNumber()));
        User user = userRepository.findById(customer.getUser().getId()).orElseThrow(() -> new EcomCartException(HttpStatus.NOT_FOUND,"User Not Found"));
        customerDto.setUser(user);
        return customerDto;

    }
}
