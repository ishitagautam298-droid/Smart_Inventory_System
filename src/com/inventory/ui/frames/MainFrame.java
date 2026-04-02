package com.inventory.ui.frames;

import com.inventory.ui.panels.SidebarPanel;
import com.inventory.ui.theme.AppColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * JFC: Main JFrame shell — holds sidebar + content area.
 * EBT: WindowAdapter for clean DB connection close on exit.
 * OOPs: Composition — MainFrame HAS-A SidebarPanel.
 */
public class MainFrame extends JFrame {

    private final JPanel contentArea;

    public MainFrame() {
        setTitle("SmartStock — Inventory & Supply Chain Management");
        setSize(1280, 760);
        setMinimumSize(new Dimension(960, 620));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(AppColors.BG_APP);

        SidebarPanel sidebar = new SidebarPanel(contentArea);
        contentArea.add(new DashboardFrame(), BorderLayout.CENTER);

        add(sidebar,     BorderLayout.WEST);
        add(contentArea, BorderLayout.CENTER);

        // EBT: WindowAdapter — close DB connection cleanly
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int opt = JOptionPane.showConfirmDialog(MainFrame.this,
                    "Exit SmartStock?", "Confirm Exit",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (opt == JOptionPane.YES_OPTION) {
                    com.inventory.backend.DBConnection.closeConnection();
                    dispose();
                    System.exit(0);
                }
            }
        });

        setVisible(true);
    }

    public void navigate(JPanel page) {
        contentArea.removeAll();
        contentArea.add(page, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }
}
