package com.inventory.service;

import java.time.LocalDate;
import java.util.List;

import com.inventory.backend.InventoryDataStore;
import com.inventory.model.Order;
import com.inventory.model.Product;

public class OrderService {
    private final InventoryDataStore dataStore = InventoryDataStore.getInstance();
    private final ProductService productService = new ProductService();

    public Order placeOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order is required");
        }
        if (order.getProductId() <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        if (order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Product product = productService.getProductById(order.getProductId());
        if (product.getQuantity() < order.getQuantity()) {
            throw new IllegalArgumentException(
                "Insufficient stock. Available quantity: " + product.getQuantity()
            );
        }

        product.setQuantity(product.getQuantity() - order.getQuantity());
        dataStore.saveProduct(product);

        if (order.getOrderDate() == null || order.getOrderDate().trim().isEmpty()) {
            order.setOrderDate(LocalDate.now().toString());
        }

        return dataStore.saveOrder(order);
    }

    public List<Order> getAllOrders() {
        return dataStore.getOrders();
    }
}
