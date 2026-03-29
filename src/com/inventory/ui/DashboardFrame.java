package com.inventory.ui;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("Dashboard");
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 240, 240));

        panel.add(createButton("Products"));
        panel.add(createButton("Suppliers"));
        panel.add(createButton("Orders"));
        panel.add(createButton("Reports"));

        add(panel);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(new Color(0, 123, 255));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);

        btn.addActionListener(e -> openPanel(text));
        return btn;
    }

    private void openPanel(String type) {
        switch (type) {
            case "Products":
                new ProductPanel();
                break;
            case "Suppliers":
                new SupplierPanel();
                break;
            case "Orders":
                new OrderPanel();
                break;
            case "Reports":
                new ReportPanel();
                break;
        }
    }
}
