package com.inventory.ui;

import javax.swing.*;
import java.awt.*;

import com.inventory.controller.ReportController;

public class ReportPanel extends JFrame {
    private final ReportController controller = new ReportController();
    private final DashboardFrame parent;
    private JTextArea reportArea;

    public ReportPanel(DashboardFrame parent) {
        this.parent = parent;
        setTitle("Reports");
        setSize(760, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(196, 181, 253), 2),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        toolbar.setBackground(Color.WHITE);
        JLabel title = new JLabel("Inventory Reports");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(17, 24, 39));
        JPanel buttonGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonGroup.setOpaque(false);
        JButton refreshButton = new JButton("Refresh Report");
        JButton copyButton = new JButton("Copy Report");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setBackground(new Color(139, 92, 246));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setOpaque(true);
        refreshButton.setContentAreaFilled(true);
        refreshButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(109, 40, 217), 2),
            BorderFactory.createEmptyBorder(10, 22, 10, 22)
        ));
        copyButton.setFont(new Font("Arial", Font.BOLD, 14));
        copyButton.setBackground(new Color(167, 139, 250));
        copyButton.setForeground(Color.WHITE);
        copyButton.setFocusPainted(false);
        copyButton.setOpaque(true);
        copyButton.setContentAreaFilled(true);
        copyButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(109, 40, 217), 2),
            BorderFactory.createEmptyBorder(10, 22, 10, 22)
        ));
        buttonGroup.add(refreshButton);
        buttonGroup.add(copyButton);
        toolbar.add(title, BorderLayout.WEST);
        toolbar.add(buttonGroup, BorderLayout.EAST);

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        reportArea.setForeground(new Color(17, 24, 39));
        reportArea.setBackground(new Color(250, 245, 255));
        reportArea.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        try {
            reportArea.setText(controller.getInventoryReport());
        } catch (Exception e) {
            reportArea.setText("Error loading reports!");
        }

        root.add(toolbar, BorderLayout.NORTH);
        root.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        add(root);

        refreshButton.addActionListener(e -> refreshReport());
        copyButton.addActionListener(e -> {
            reportArea.selectAll();
            reportArea.copy();
            reportArea.select(0, 0);
            JOptionPane.showMessageDialog(this, "Report copied to clipboard.");
        });
    }

    private void refreshReport() {
        reportArea.setText(controller.getInventoryReport());
        if (parent != null) {
            parent.refreshDashboard();
        }
    }
}
