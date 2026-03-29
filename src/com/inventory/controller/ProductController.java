package com.inventory.controller;

import com.inventory.model.Product;

public class ProductController {

    public void addProduct(Product product) {
        try {
            if (product.getName() == null || product.getName().isEmpty()) {
                throw new Exception("Product name cannot be empty");
            }

            if (product.getQuantity() < 0) {
                throw new Exception("Quantity cannot be negative");
            }

            // Dummy logic (no DB)
            System.out.println("Product Added Successfully:");
            System.out.println("Name: " + product.getName());
            System.out.println("Quantity: " + product.getQuantity());

        } catch (Exception e) {
            System.out.println("Product Error: " + e.getMessage());
        }
    }
}
