package com.hbox.ecom_cart.service;

public interface InventoryService {

    void updateStock(Long productId, Integer quantity);

    Integer checkStock(Long productId);

}
