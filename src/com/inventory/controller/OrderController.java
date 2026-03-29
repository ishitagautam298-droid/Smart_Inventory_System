package com.inventory.controller;

import com.inventory.backend.InventoryBackendFacade;
import com.inventory.model.Order;

import java.util.List;

public class OrderController {
    private final InventoryBackendFacade backend = InventoryBackendFacade.getInstance();

    public Order placeOrder(Order order) {
        return backend.placeOrder(order);
    }

    public List<Order> getAllOrders() {
        return backend.getAllOrders();
    }
}
