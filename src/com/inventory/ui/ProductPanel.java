package com.inventory.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;

import com.inventory.controller.ProductController;
import com.inventory.model.Product;

public class ProductPanel extends JFrame {
    private static final Color PAGE_BACKGROUND = new Color(239, 246, 255);
    private static final Color PANEL_BACKGROUND = Color.WHITE;
    private static final Color PRIMARY_BLUE = new Color(37, 99, 235);
    private static final Color SECONDARY_ORANGE = new Color(249, 115, 22);
    private static final Color TEXT_BLACK = new Color(17, 24, 39);
    private static final Color MUTED_BORDER = new Color(191, 219, 254);

    private final ProductController controller = new ProductController();
    private final DashboardFrame parent;
    private final DefaultTableModel tableModel = new DefaultTableModel(
        new Object[]{"ID", "Name", "Quantity", "Price", "Stock Status"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private JTextField nameField;
    private JTextField quantityField;
    private JTextField priceField;
    private JTable productTable;

    public ProductPanel(DashboardFrame parent) {
        this.parent = parent;
        setTitle("Product Management");
        setSize(760, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        root.setBackground(PAGE_BACKGROUND);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 14, 14));
        javax.swing.border.TitledBorder titledBorder = BorderFactory.createTitledBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(96, 165, 250), 2),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
            ),
            "Add Product"
        );
        titledBorder.setTitleColor(TEXT_BLACK);
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 22));
        formPanel.setBorder(titledBorder);
        formPanel.setBackground(PANEL_BACKGROUND);

        JLabel nameLabel = new JLabel("Product Name:");
        nameLabel.setForeground(TEXT_BLACK);
        formPanel.add(nameLabel);
        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(191, 219, 254), 2),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        nameField.setForeground(TEXT_BLACK);
        formPanel.add(nameField);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setForeground(TEXT_BLACK);
        formPanel.add(quantityLabel);
        quantityField = new JTextField();
        quantityField.setFont(new Font("Arial", Font.PLAIN, 16));
        quantityField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(191, 219, 254), 2),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        quantityField.setForeground(TEXT_BLACK);
        formPanel.add(quantityField);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setForeground(TEXT_BLACK);
        formPanel.add(priceLabel);
        priceField = new JTextField();
        priceField.setFont(new Font("Arial", Font.PLAIN, 16));
        priceField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(191, 219, 254), 2),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        priceField.setForeground(TEXT_BLACK);
        formPanel.add(priceField);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonRow.setBackground(PANEL_BACKGROUND);
        JButton addBtn = new JButton("Add Product");
        JButton refreshBtn = new JButton("Refresh List");
        addBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addBtn.setBackground(PRIMARY_BLUE);
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setOpaque(true);
        addBtn.setContentAreaFilled(true);
        addBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_BLUE.darker(), 2),
            BorderFactory.createEmptyBorder(10, 22, 10, 22)
        ));
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 14));
        refreshBtn.setBackground(SECONDARY_ORANGE);
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setOpaque(true);
        refreshBtn.setContentAreaFilled(true);
        refreshBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_ORANGE.darker(), 2),
            BorderFactory.createEmptyBorder(10, 22, 10, 22)
        ));
        buttonRow.add(refreshBtn);
        buttonRow.add(addBtn);
        formPanel.add(new JLabel());
        formPanel.add(buttonRow);

        productTable = new JTable(tableModel);
        productTable.setRowHeight(24);
        productTable.setForeground(TEXT_BLACK);
        productTable.setBackground(Color.WHITE);
        productTable.setSelectionBackground(new Color(219, 234, 254));
        productTable.setSelectionForeground(TEXT_BLACK);
        productTable.getTableHeader().setBackground(PRIMARY_BLUE);
        productTable.getTableHeader().setForeground(Color.WHITE);
        productTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        productTable.getColumnModel().getColumn(4).setCellRenderer(new StockStatusRenderer());

        root.add(formPanel, BorderLayout.NORTH);
        root.add(new JScrollPane(productTable), BorderLayout.CENTER);
        add(root);

        refreshTable();

        addBtn.addActionListener(e -> addProduct());
        refreshBtn.addActionListener(e -> refreshTable());
    }

    private void addProduct() {
        try {
            String name = nameField.getText().trim();
            String qtyText = quantityField.getText().trim();
            String priceText = priceField.getText().trim();

            if (name.isEmpty() || qtyText.isEmpty() || priceText.isEmpty()) {
                throw new Exception("All fields are required!");
            }

            int quantity = Integer.parseInt(qtyText);
            double price = Double.parseDouble(priceText);

            if (quantity < 0) {
                throw new Exception("Quantity cannot be negative!");
            }
            if (price < 0) {
                throw new Exception("Price cannot be negative!");
            }

            Product product = new Product();
            product.setName(name);
            product.setQuantity(quantity);
            product.setPrice(price);

            controller.addProduct(product);

            JOptionPane.showMessageDialog(this, "Product Added Successfully!");
            nameField.setText("");
            quantityField.setText("");
            priceField.setText("");
            refreshTable();
            if (parent != null) {
                parent.refreshDashboard();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity and price must be numeric!", "Error", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        for (Product product : controller.getAllProducts()) {
            String stockStatus = product.getQuantity() <= 5 ? "Low Stock" : "In Stock";
            tableModel.addRow(new Object[]{
                product.getId(),
                product.getName(),
                product.getQuantity(),
                currencyFormat.format(product.getPrice()),
                stockStatus
            });
        }
        if (parent != null) {
            parent.refreshDashboard();
        }
    }

    private static class StockStatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column
        ) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                component.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                String status = String.valueOf(value);
                component.setForeground("Low Stock".equals(status) ? new Color(220, 38, 38) : TEXT_BLACK);
            }
            return component;
        }
    }
}
