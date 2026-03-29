package com.inventory.ui;

import javax.swing.*;
import java.awt.*;

import com.inventory.controller.SupplierController;
import com.inventory.model.Supplier;

public class SupplierPanel extends JFrame {
    private JTextField nameField, contactField;

    public SupplierPanel() {
        setTitle("Supplier Management");
        setSize(400, 250);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("Supplier Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Contact:"));
        contactField = new JTextField();
        panel.add(contactField);

        JButton addBtn = new JButton("Add Supplier");
        panel.add(new JLabel());
        panel.add(addBtn);

        add(panel);
        setVisible(true);

        addBtn.addActionListener(e -> addSupplier());
    }

    private void addSupplier() {
        try {
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();

            if (name.isEmpty() || contact.isEmpty()) {
                throw new Exception("All fields are required!");
            }

            Supplier s = new Supplier();
            s.setName(name);
            s.setContact(contact);

            new SupplierController().addSupplier(s);

            JOptionPane.showMessageDialog(this, "Supplier Added!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
