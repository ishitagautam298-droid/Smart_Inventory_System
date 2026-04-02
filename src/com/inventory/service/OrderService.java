package com.inventory.service;

import com.inventory.backend.OrderDAO;
import com.inventory.model.OrderBean;
import java.util.List;

/**
 * OOPs: Abstraction — validates and processes orders.
 */
public class OrderService {

    // OOPs: Composition
    private final OrderDAO orderDAO = new OrderDAO();

    public boolean placeOrder(OrderBean o) {
        if (o.getQuantity() <= 0)  return false;
        if (o.getProductId() <= 0) return false;
        o.setStatus("PENDING");
        return orderDAO.addOrder(o);
    }

    public List<OrderBean> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    public List<OrderBean> getOrdersByStatus(String status) {
        return orderDAO.getOrdersByStatus(status);
    }

    public boolean cancelOrder(int orderId) {
        return orderDAO.updateOrderStatus(orderId, "CANCELLED");
    }

    public boolean updateStatus(int orderId, String status) {
        return orderDAO.updateOrderStatus(orderId, status);
    }

    public int getActiveOrderCount() {
        return orderDAO.getActiveOrderCount();
    }
}
