package com.hbox.ecom_cart.service.impl;

import com.hbox.ecom_cart.entity.Product;
import com.hbox.ecom_cart.exception.EcomCartException;
import com.hbox.ecom_cart.repositoty.ProductRepository;
import com.hbox.ecom_cart.service.InventoryService;
import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {

    private ProductRepository productRepository;
    private RedissonClient redissonClient;

    @Override
    public void updateStock(Long productId, Integer quantity) {
        RLock rlock = redissonClient.getLock("Initiate lock on product: " + productId);
        try
        {
            if(rlock.tryLock(10,5, TimeUnit.SECONDS))
            {
                System.out.println("********** Acquired the lock on the product to update its stock ***********");
                Product product = productRepository.findById(productId).orElseThrow(() ->
                        new EcomCartException(HttpStatus.BAD_REQUEST, "Product not found"));
                Integer currentStock = product.getProductStock();
                product.setProductStock(currentStock + quantity);
                productRepository.save(product);
            }
        }
        catch (InterruptedException e)
        {
            throw new EcomCartException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
        finally {
            if(rlock.isHeldByCurrentThread())
            {
                rlock.unlock();
            }
        }
    }

    @Override
    public Integer checkStock(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new EcomCartException(HttpStatus.BAD_REQUEST, "Product not found"));
        return product.getProductStock();
    }
}
