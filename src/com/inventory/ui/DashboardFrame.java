package com.inventory.ui;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

import com.inventory.controller.OrderController;
import com.inventory.controller.ProductController;
import com.inventory.controller.SupplierController;
import com.inventory.model.Product;

public class DashboardFrame extends JFrame {
    private static final Color PRIMARY_BLUE = new Color(37, 99, 235);
    private static final Color SECONDARY_ORANGE = new Color(249, 115, 22);
    private static final Color TEAL = new Color(14, 116, 144);
    private static final Color GREEN = new Color(22, 163, 74);
    private static final Color VIOLET = new Color(168, 85, 247);

    private final ProductController productController = new ProductController();
    private final SupplierController supplierController = new SupplierController();
    private final OrderController orderController = new OrderController();

    private JLabel productCountLabel;
    private JLabel supplierCountLabel;
    private JLabel orderCountLabel;
    private JLabel inventoryValueLabel;

    public DashboardFrame() {
        setTitle("Smart Inventory Dashboard");
        setSize(840, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 20));
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        root.setBackground(new Color(241, 245, 249));

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createCenterContent(), BorderLayout.CENTER);
        root.add(createFooter(), BorderLayout.SOUTH);

        add(root);
        refreshSummary();
    }

    public void refreshDashboard() {
        refreshSummary();
        revalidate();
        repaint();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Operations Overview");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(new Color(15, 23, 42));

        JLabel subtitle = new JLabel("Track stock, suppliers, orders, and reports from one place.");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(new Color(71, 85, 105));

        JPanel titleBlock = new JPanel(new GridLayout(2, 1, 0, 4));
        titleBlock.setOpaque(false);
        titleBlock.add(title);
        titleBlock.add(subtitle);

        JButton refreshButton = new JButton("Refresh Summary");
        refreshButton.setBackground(new Color(255, 244, 230));
        refreshButton.setForeground(new Color(17, 24, 39));
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> refreshSummary());

        header.add(titleBlock, BorderLayout.WEST);
        header.add(refreshButton, BorderLayout.EAST);
        return header;
    }

    private JPanel createCenterContent() {
        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setOpaque(false);

        JPanel cards = new JPanel(new GridLayout(1, 4, 16, 0));
        cards.setOpaque(false);

        productCountLabel = new JLabel();
        supplierCountLabel = new JLabel();
        orderCountLabel = new JLabel();
        inventoryValueLabel = new JLabel();

        cards.add(createStatCard("Products", productCountLabel, PRIMARY_BLUE));
        cards.add(createStatCard("Suppliers", supplierCountLabel, TEAL));
        cards.add(createStatCard("Orders", orderCountLabel, GREEN));
        cards.add(createStatCard("Inventory Value", inventoryValueLabel, VIOLET));

        JPanel actions = new JPanel(new GridLayout(2, 2, 18, 18));
        actions.setOpaque(false);
        actions.add(createButton("Products", "Manage stock and review inventory levels"));
        actions.add(createButton("Suppliers", "Maintain supplier records and contacts"));
        actions.add(createButton("Orders", "Create orders and reduce inventory instantly"));
        actions.add(createButton("Reports", "View generated business and stock reports"));

        content.add(cards, BorderLayout.NORTH);
        content.add(actions, BorderLayout.CENTER);
        return content;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JLabel note = new JLabel("Seed accounts: admin / admin and manager / manager123");
        note.setForeground(new Color(100, 116, 139));
        note.setFont(new Font("Arial", Font.PLAIN, 12));
        footer.add(note, BorderLayout.WEST);
        return footer;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240)),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        card.setBackground(Color.WHITE);

        JPanel accentBar = new JPanel();
        accentBar.setPreferredSize(new Dimension(0, 6));
        accentBar.setBackground(accent);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(51, 65, 85));

        valueLabel.setFont(new Font("Arial", Font.BOLD, 22));
        valueLabel.setForeground(new Color(15, 23, 42));

        card.add(accentBar, BorderLayout.NORTH);
        card.add(titleLabel, BorderLayout.CENTER);
        card.add(valueLabel, BorderLayout.SOUTH);
        return card;
    }

    private JButton createButton(String text, String tooltip) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        Color buttonColor;
        switch (text) {
            case "Products":
                buttonColor = PRIMARY_BLUE;
                break;
            case "Suppliers":
                buttonColor = TEAL;
                break;
            case "Orders":
                buttonColor = GREEN;
                break;
            default:
                buttonColor = VIOLET;
                break;
        }
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(buttonColor.darker(), 2),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        btn.setToolTipText(tooltip);
        btn.setPreferredSize(new Dimension(220, 120));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            openPanel(text);
            refreshSummary();
        });
        return btn;
    }

    private void refreshSummary() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        int totalUnits = 0;
        double totalValue = 0;

        for (Product product : productController.getAllProducts()) {
            totalUnits += product.getQuantity();
            totalValue += product.getQuantity() * product.getPrice();
        }

        productCountLabel.setText(String.valueOf(productController.getAllProducts().size()) + " items");
        supplierCountLabel.setText(String.valueOf(supplierController.getAllSuppliers().size()) + " partners");
        orderCountLabel.setText(String.valueOf(orderController.getAllOrders().size()) + " orders");
        inventoryValueLabel.setText(currencyFormat.format(totalValue) + " / " + totalUnits + " units");
    }

    private void openPanel(String type) {
        switch (type) {
            case "Products":
                new ProductPanel(this).setVisible(true);
                break;
            case "Suppliers":
                new SupplierPanel(this).setVisible(true);
                break;
            case "Orders":
                new OrderPanel(this).setVisible(true);
                break;
            case "Reports":
                new ReportPanel(this).setVisible(true);
                break;
        }
    }
}
