package com.hbox.ecom_cart.repositoty;

import com.hbox.ecom_cart.entity.RazorpayPaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RazorpayPaymentTypeRepository extends JpaRepository<RazorpayPaymentType, String> {
}
