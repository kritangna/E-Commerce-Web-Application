package com.hbox.ecom_cart.repositoty;

import com.hbox.ecom_cart.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
