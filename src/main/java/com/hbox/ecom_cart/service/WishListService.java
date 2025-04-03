package com.hbox.ecom_cart.service;

import com.hbox.ecom_cart.dto.ProductDto;
import com.hbox.ecom_cart.dto.WishListDto;
import com.hbox.ecom_cart.entity.WishList;

import java.util.List;

public interface WishListService {

    WishListDto addWishList(Long customerProfileId, ProductDto productDto);

    List<ProductDto> getWishListByCustomerId(Long customerId);

}
