package com.inventory.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import com.inventory.controller.OrderController;
import com.inventory.controller.ProductController;
import com.inventory.model.Order;
import com.inventory.model.Product;

public class OrderPanel extends JFrame {
    private static final Color TEXT_BLACK = new Color(17, 24, 39);

    private final OrderController orderController = new OrderController();
    private final ProductController productController = new ProductController();
    private final DashboardFrame parent;
    private final DefaultTableModel productTableModel = new DefaultTableModel(
        new Object[]{"ID", "Product", "Available Qty", "Price"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final DefaultTableModel orderTableModel = new DefaultTableModel(
        new Object[]{"Order ID", "Product ID", "Quantity", "Order Date"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private JComboBox<String> productSelector;
    private JTextField quantityField;

    public OrderPanel(DashboardFrame parent) {
        this.parent = parent;
        setTitle("Order Management");
        setSize(860, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 14, 14));
        formPanel.setBackground(Color.WHITE);
        javax.swing.border.TitledBorder titledBorder = BorderFactory.createTitledBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(74, 222, 128), 2),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
            ),
            "Place Order"
        );
        titledBorder.setTitleColor(TEXT_BLACK);
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 22));
        formPanel.setBorder(titledBorder);

        JLabel productLabel = new JLabel("Product:");
        productLabel.setForeground(TEXT_BLACK);
        formPanel.add(productLabel);
        productSelector = new JComboBox<>();
        productSelector.setForeground(TEXT_BLACK);
        productSelector.setFont(new Font("Arial", Font.PLAIN, 15));
        formPanel.add(productSelector);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setForeground(TEXT_BLACK);
        formPanel.add(quantityLabel);
        quantityField = new JTextField();
        quantityField.setForeground(TEXT_BLACK);
        quantityField.setFont(new Font("Arial", Font.PLAIN, 16));
        quantityField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(187, 247, 208), 2),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        formPanel.add(quantityField);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonRow.setBackground(Color.WHITE);
        JButton orderBtn = new JButton("Place Order");
        JButton refreshBtn = new JButton("Refresh Tables");
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 14));
        refreshBtn.setBackground(new Color(34, 197, 94));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setOpaque(true);
        refreshBtn.setContentAreaFilled(true);
        refreshBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(21, 128, 61), 2),
            BorderFactory.createEmptyBorder(10, 22, 10, 22)
        ));
        orderBtn.setFont(new Font("Arial", Font.BOLD, 14));
        orderBtn.setBackground(new Color(22, 163, 74));
        orderBtn.setForeground(Color.WHITE);
        orderBtn.setFocusPainted(false);
        orderBtn.setOpaque(true);
        orderBtn.setContentAreaFilled(true);
        orderBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(21, 128, 61), 2),
            BorderFactory.createEmptyBorder(10, 22, 10, 22)
        ));
        buttonRow.add(refreshBtn);
        buttonRow.add(orderBtn);
        formPanel.add(new JLabel());
        formPanel.add(buttonRow);

        JTable productTable = new JTable(productTableModel);
        JTable orderTable = new JTable(orderTableModel);
        productTable.setRowHeight(24);
        orderTable.setRowHeight(24);

        JSplitPane splitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            new JScrollPane(productTable),
            new JScrollPane(orderTable)
        );
        splitPane.setResizeWeight(0.45);

        root.add(formPanel, BorderLayout.NORTH);
        root.add(splitPane, BorderLayout.CENTER);
        add(root);

        refreshData();

        orderBtn.addActionListener(e -> placeOrder());
        refreshBtn.addActionListener(e -> refreshData());
    }

    private void placeOrder() {
        try {
            int selectedIndex = productSelector.getSelectedIndex();
            if (selectedIndex < 0) {
                throw new Exception("Please select a product!");
            }

            List<Product> products = productController.getAllProducts();
            int productId = products.get(selectedIndex).getId();
            int quantity = Integer.parseInt(quantityField.getText().trim());

            if (quantity <= 0) {
                throw new Exception("Quantity must be greater than 0!");
            }

            Order order = new Order();
            order.setProductId(productId);
            order.setQuantity(quantity);

            orderController.placeOrder(order);

            JOptionPane.showMessageDialog(this, "Order Placed Successfully!");
            quantityField.setText("");
            refreshData();
            if (parent != null) {
                parent.refreshDashboard();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshData() {
        refreshProductSelector();
        refreshProductTable();
        refreshOrderTable();
        if (parent != null) {
            parent.refreshDashboard();
        }
    }

    private void refreshProductSelector() {
        productSelector.removeAllItems();
        for (Product product : productController.getAllProducts()) {
            productSelector.addItem(product.getId() + " - " + product.getName() + " (" + product.getQuantity() + " available)");
        }
    }

    private void refreshProductTable() {
        productTableModel.setRowCount(0);
        for (Product product : productController.getAllProducts()) {
            productTableModel.addRow(new Object[]{
                product.getId(),
                product.getName(),
                product.getQuantity(),
                product.getPrice()
            });
        }
    }

    private void refreshOrderTable() {
        orderTableModel.setRowCount(0);
        for (Order order : orderController.getAllOrders()) {
            orderTableModel.addRow(new Object[]{
                order.getId(),
                order.getProductId(),
                order.getQuantity(),
                order.getOrderDate()
            });
        }
    }
}
