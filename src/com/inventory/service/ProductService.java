package com.inventory.service;

import java.util.List;

import com.inventory.backend.InventoryDataStore;
import com.inventory.model.Product;

public class ProductService {
    private final InventoryDataStore dataStore = InventoryDataStore.getInstance();

    public Product addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product is required");
        }

        String name = requireText(product.getName(), "Product name");
        if (product.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        product.setName(name);
        return dataStore.saveProduct(product);
    }

    public Product getProductById(int productId) {
        Product product = dataStore.findProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found for ID: " + productId);
        }
        return product;
    }

    public List<Product> getAllProducts() {
        return dataStore.getProducts();
    }

    private String requireText(String value, String fieldName) {
        String normalized = value == null ? "" : value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        return normalized;
    }
}
