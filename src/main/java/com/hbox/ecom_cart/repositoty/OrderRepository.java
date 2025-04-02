package com.hbox.ecom_cart.repositoty;

import com.hbox.ecom_cart.dto.OrderDto;
import com.hbox.ecom_cart.entity.CustomerProfile;
import com.hbox.ecom_cart.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerProfile(CustomerProfile customerProfile);
}
