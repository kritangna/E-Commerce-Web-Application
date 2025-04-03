package com.hbox.ecom_cart.controller;

import com.hbox.ecom_cart.dto.CustomerProfileDto;
import com.hbox.ecom_cart.dto.ProductDto;
import com.hbox.ecom_cart.dto.WishListDto;
import com.hbox.ecom_cart.repositoty.WishListRepository;
import com.hbox.ecom_cart.service.WishListService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/e-com-cart/wish-list")
public class WishListController {

    private WishListService wishListService;

    @PostMapping("{id}")
    public ResponseEntity<WishListDto> addToWishList(@PathVariable("id") Long customerId, @RequestBody ProductDto productDto) {
        return new ResponseEntity<>(wishListService.addWishList(customerId, productDto), HttpStatus.CREATED );
    }

    @GetMapping("{id}")
    public ResponseEntity<List<ProductDto>> getAllWishListedProducts(@PathVariable("id") Long customerId)
    {
        List<ProductDto> list = wishListService.getWishListByCustomerId(customerId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
