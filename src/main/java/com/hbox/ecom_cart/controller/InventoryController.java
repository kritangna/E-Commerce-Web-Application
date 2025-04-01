package com.hbox.ecom_cart.controller;

import com.hbox.ecom_cart.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/e-com-cart/inventory")
public class InventoryController {

    private InventoryService inventoryService;

    @PutMapping("{id}")
    public ResponseEntity<String> updateStock(@PathVariable("id") Long productId, @RequestParam Integer quantity) {
        inventoryService.updateStock(productId, quantity);
        return ResponseEntity.ok("Stock updated successfully");
    }

    @GetMapping("{id}")
    public ResponseEntity<Integer> checkStock(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(inventoryService.checkStock(productId));
    }
}
