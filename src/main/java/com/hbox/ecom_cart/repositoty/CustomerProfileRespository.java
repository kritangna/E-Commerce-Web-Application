package com.hbox.ecom_cart.repositoty;

import com.hbox.ecom_cart.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerProfileRespository extends JpaRepository<CustomerProfile, Long> {

    CustomerProfile findByUserId(Long UserId);
  //  CustomerProfile findByCustomerProfileId(Long customerId);
}
