package com.inventory.ui;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("Dashboard");
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));

        JButton productBtn = new JButton("Products");
        JButton supplierBtn = new JButton("Suppliers");
        JButton orderBtn = new JButton("Orders");
        JButton reportBtn = new JButton("Reports");

        panel.add(productBtn);
        panel.add(supplierBtn);
        panel.add(orderBtn);
        panel.add(reportBtn);

        add(panel);

        productBtn.addActionListener(e -> new ProductPanel());
        supplierBtn.addActionListener(e -> new SupplierPanel());
        orderBtn.addActionListener(e -> new OrderPanel());
        reportBtn.addActionListener(e -> new ReportPanel());
    }
}
