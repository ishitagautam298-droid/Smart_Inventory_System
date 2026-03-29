package com.inventory.controller;

import com.inventory.model.Product;

public class ProductController {

    public void addProduct(Product product) {
        // Dummy logic
        System.out.println("Product Added: " + product.getName() + 
                           ", Quantity: " + product.getQuantity());
    }
}
