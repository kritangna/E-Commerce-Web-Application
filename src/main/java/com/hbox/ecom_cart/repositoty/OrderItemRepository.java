package com.hbox.ecom_cart.repositoty;

import com.hbox.ecom_cart.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
