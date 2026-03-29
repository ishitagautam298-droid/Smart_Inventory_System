package com.inventory.controller;

import com.inventory.backend.InventoryBackendFacade;
import com.inventory.model.Product;

import java.util.List;

public class ProductController {
    private final InventoryBackendFacade backend = InventoryBackendFacade.getInstance();

    public Product addProduct(Product product) {
        return backend.addProduct(product);
    }

    public List<Product> getAllProducts() {
        return backend.getAllProducts();
    }
}
