package com.hbox.ecom_cart.repositoty;

import com.hbox.ecom_cart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
