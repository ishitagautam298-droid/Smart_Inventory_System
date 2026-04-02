package com.inventory.controller;

import com.inventory.model.OrderBean;
import com.inventory.service.OrderService;
import com.inventory.ui.dialogs.ConfirmDialog;

import javax.swing.*;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * EBT: ActionListener — handles all order button events.
 */
public class OrderController implements ActionListener {

    // OOPs: Composition
    private final OrderService service = new OrderService();

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case "NEW_ORDER": break;
            case "REFRESH":   break;
            default: break;
        }
    }

    public List<OrderBean> loadAllOrders() {
        return service.getAllOrders();
    }

    public List<OrderBean> loadByStatus(String status) {
        return service.getOrdersByStatus(status);
    }

    public boolean placeOrder(int productId, int supplierId, int qty, double total) {
        OrderBean o = new OrderBean(0, productId, "", supplierId, "", qty, total, "", "PENDING");
        boolean ok = service.placeOrder(o);
        if (ok) JOptionPane.showMessageDialog(null, "Order placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        else    JOptionPane.showMessageDialog(null, "Failed to place order.",      "Error",   JOptionPane.ERROR_MESSAGE);
        return ok;
    }

    public boolean cancelOrder(int orderId, Window parent) {
        ConfirmDialog dlg = new ConfirmDialog(null, "Cancel Order",
            "Are you sure you want to cancel Order #" + orderId + "?");
        dlg.setVisible(true);
        if (dlg.isConfirmed()) return service.cancelOrder(orderId);
        return false;
    }

    public int getActiveOrderCount() {
        return service.getActiveOrderCount();
    }
}
