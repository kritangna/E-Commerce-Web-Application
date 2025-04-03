package com.hbox.ecom_cart.repositoty;

import com.hbox.ecom_cart.dto.CustomerProfileDto;
import com.hbox.ecom_cart.dto.ProductDto;
import com.hbox.ecom_cart.entity.CustomerProfile;
import com.hbox.ecom_cart.entity.Product;
import com.hbox.ecom_cart.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    List<WishList> findByCustomerProfileId(Long customerProfileId);

    boolean existsByCustomerProfileAndProduct(CustomerProfile customerProfile, Product product);
}
