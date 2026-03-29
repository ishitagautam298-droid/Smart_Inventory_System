package com.inventory.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import com.inventory.controller.SupplierController;
import com.inventory.model.Supplier;

public class SupplierPanel extends JFrame {
    private static final Color TEXT_BLACK = new Color(17, 24, 39);

    private final SupplierController controller = new SupplierController();
    private final DashboardFrame parent;
    private final DefaultTableModel tableModel = new DefaultTableModel(
        new Object[]{"ID", "Name", "Contact", "Address"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private JTextField nameField;
    private JTextField contactField;
    private JTextField addressField;

    public SupplierPanel(DashboardFrame parent) {
        this.parent = parent;
        setTitle("Supplier Management");
        setSize(760, 460);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 14, 14));
        javax.swing.border.TitledBorder titledBorder = BorderFactory.createTitledBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 212, 191), 2),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
            ),
            "Add Supplier"
        );
        titledBorder.setTitleColor(TEXT_BLACK);
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 22));
        formPanel.setBorder(titledBorder);
        formPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Supplier Name:");
        nameLabel.setForeground(TEXT_BLACK);
        formPanel.add(nameLabel);
        nameField = new JTextField();
        nameField.setForeground(TEXT_BLACK);
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(153, 246, 228), 2),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        formPanel.add(nameField);

        JLabel contactLabel = new JLabel("Contact:");
        contactLabel.setForeground(TEXT_BLACK);
        formPanel.add(contactLabel);
        contactField = new JTextField();
        contactField.setForeground(TEXT_BLACK);
        contactField.setFont(new Font("Arial", Font.PLAIN, 16));
        contactField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(153, 246, 228), 2),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        formPanel.add(contactField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setForeground(TEXT_BLACK);
        formPanel.add(addressLabel);
        addressField = new JTextField();
        addressField.setForeground(TEXT_BLACK);
        addressField.setFont(new Font("Arial", Font.PLAIN, 16));
        addressField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(153, 246, 228), 2),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        formPanel.add(addressField);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonRow.setBackground(Color.WHITE);
        JButton refreshBtn = new JButton("Refresh List");
        JButton addBtn = new JButton("Add Supplier");
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 14));
        refreshBtn.setBackground(new Color(20, 184, 166));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setOpaque(true);
        refreshBtn.setContentAreaFilled(true);
        refreshBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(15, 118, 110), 2),
            BorderFactory.createEmptyBorder(10, 22, 10, 22)
        ));
        addBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addBtn.setBackground(new Color(13, 148, 136));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setOpaque(true);
        addBtn.setContentAreaFilled(true);
        addBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(15, 118, 110), 2),
            BorderFactory.createEmptyBorder(10, 22, 10, 22)
        ));
        buttonRow.add(refreshBtn);
        buttonRow.add(addBtn);
        formPanel.add(new JLabel());
        formPanel.add(buttonRow);

        JTable supplierTable = new JTable(tableModel);
        supplierTable.setRowHeight(24);

        root.add(formPanel, BorderLayout.NORTH);
        root.add(new JScrollPane(supplierTable), BorderLayout.CENTER);
        add(root);

        refreshTable();

        addBtn.addActionListener(e -> addSupplier());
        refreshBtn.addActionListener(e -> refreshTable());
    }

    private void addSupplier() {
        try {
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            String address = addressField.getText().trim();

            if (name.isEmpty() || contact.isEmpty()) {
                throw new Exception("All fields are required!");
            }

            Supplier s = new Supplier();
            s.setName(name);
            s.setContact(contact);
            s.setAddress(address);

            controller.addSupplier(s);

            JOptionPane.showMessageDialog(this, "Supplier Added!");
            nameField.setText("");
            contactField.setText("");
            addressField.setText("");
            refreshTable();
            if (parent != null) {
                parent.refreshDashboard();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Supplier supplier : controller.getAllSuppliers()) {
            tableModel.addRow(new Object[]{
                supplier.getId(),
                supplier.getName(),
                supplier.getContact(),
                supplier.getAddress()
            });
        }
        if (parent != null) {
            parent.refreshDashboard();
        }
    }
}
