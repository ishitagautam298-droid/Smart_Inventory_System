package com.inventory.controller;

import com.inventory.model.Order;

public class OrderController {

    public void placeOrder(Order order) {
        try {
            if (order.getProductId() <= 0) {
                throw new Exception("Invalid Product ID");
            }

            if (order.getQuantity() <= 0) {
                throw new Exception("Quantity must be greater than 0");
            }

            // Dummy logic
            System.out.println("Order Placed Successfully:");
            System.out.println("Product ID: " + order.getProductId());
            System.out.println("Quantity: " + order.getQuantity());

        } catch (Exception e) {
            System.out.println("Order Error: " + e.getMessage());
        }
    }
}
