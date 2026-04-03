package com.inventory.ui.frames;

import com.inventory.ui.panels.HeaderPanel;
import com.inventory.ui.theme.AppColors;
import com.inventory.ui.theme.AppFonts;
import com.inventory.ui.theme.UIFactory;

import javax.swing.*;
import java.awt.*;

/**
 * JFC: Settings page with DB config, app preferences and about section.
 * EBT: ActionListener on all buttons.
 * OOPs: Inheritance — extends JPanel.
 */
public class SettingsFrame extends JPanel {

    public SettingsFrame() {
        setBackground(AppColors.BG_APP);
        setLayout(new BorderLayout());
        add(new HeaderPanel("Settings"), BorderLayout.NORTH);
        add(UIFactory.scrollPane(buildBody()), BorderLayout.CENTER);
    }

    private JPanel buildBody() {
        JPanel body = new JPanel();
        body.setBackground(AppColors.BG_APP);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(UIFactory.pagePadding());

        body.add(buildDBSettings());
        body.add(Box.createVerticalStrut(20));
        body.add(buildAppSettings());
        body.add(Box.createVerticalStrut(20));
        body.add(buildAbout());
        body.add(Box.createVerticalStrut(20));
        return body;
    }

    // ── Database Settings Card ────────────────────────────────────────────────
    private JPanel buildDBSettings() {
        JPanel card = buildCard();
        card.setLayout(new BorderLayout(0, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        JLabel title = new JLabel("\uD83D\uDDC4  Database Connection");
        title.setFont(AppFonts.CARD_TITLE);
        title.setForeground(AppColors.TEXT_PRIMARY);
        card.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 2, 12, 12));
        form.setOpaque(false);

        addFormRow(form, "Host",     "localhost");
        addFormRow(form, "Port",     "3306");
        addFormRow(form, "Database", "smart_inventory");
        addFormRow(form, "Username", "root");

        card.add(form, BorderLayout.CENTER);

        // Buttons row
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setOpaque(false);

        JButton testBtn = UIFactory.secondaryButton("Test Connection");
        // EBT: ActionListener — tests DB connection
        testBtn.addActionListener(e -> {
            boolean ok = com.inventory.backend.DBConnection.testConnection();
            if (ok) {
                JOptionPane.showMessageDialog(this,
                    "\u2705  Database connected successfully!",
                    "Connection Test",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "\u274C  Could not connect to database.\n\n" +
                    "Make sure MySQL is running.\n" +
                    "Edit DBConnection.java to update credentials.\n" +
                    "Then recompile the project.",
                    "Connection Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton saveBtn = UIFactory.primaryButton("Save Settings");
        // EBT: ActionListener
        saveBtn.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "\u2705  Settings noted!\n\n" +
                "To apply DB changes permanently,\n" +
                "update DBConnection.java and recompile.",
                "Settings Saved",
                JOptionPane.INFORMATION_MESSAGE));

        btnRow.add(testBtn);
        btnRow.add(saveBtn);
        card.add(btnRow, BorderLayout.SOUTH);

        return card;
    }

    // ── App Preferences Card ──────────────────────────────────────────────────
    private JPanel buildAppSettings() {
        JPanel card = buildCard();
        card.setLayout(new BorderLayout(0, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));

        JLabel title = new JLabel("\u2699\uFE0F  Application Preferences");
        title.setFont(AppFonts.CARD_TITLE);
        title.setForeground(AppColors.TEXT_PRIMARY);
        card.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 2, 12, 12));
        form.setOpaque(false);

        // Row 1: Low stock threshold
        JLabel lbl1 = UIFactory.formLabel("Low Stock Alert Threshold");
        JTextField threshold = new JTextField("10");
        threshold.setFont(AppFonts.INPUT);
        threshold.setBorder(UIFactory.inputBorder());
        form.add(lbl1);
        form.add(threshold);

        // Row 2: Company name
        JLabel lbl2 = UIFactory.formLabel("Company Name");
        JTextField company = new JTextField("SmartStock Inc.");
        company.setFont(AppFonts.INPUT);
        company.setBorder(UIFactory.inputBorder());
        form.add(lbl2);
        form.add(company);

        // Row 3: Currency
        JLabel lbl3 = UIFactory.formLabel("Currency");
        JComboBox<String> currency = UIFactory.comboBox(
            new String[]{"USD ($)", "EUR (\u20AC)", "GBP (\u00A3)",
                         "INR (\u20B9)", "JPY (\u00A5)"});
        form.add(lbl3);
        form.add(currency);

        card.add(form, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnRow.setOpaque(false);
        JButton saveBtn = UIFactory.primaryButton("Save Preferences");
        // EBT: ActionListener
        saveBtn.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "\u2705  Preferences saved successfully!",
                "Preferences Saved",
                JOptionPane.INFORMATION_MESSAGE));
        btnRow.add(saveBtn);
        card.add(btnRow, BorderLayout.SOUTH);

        return card;
    }

    // ── About Card ────────────────────────────────────────────────────────────
    private JPanel buildAbout() {
        JPanel card = buildCard();
        card.setLayout(new BorderLayout(0, 12));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel title = new JLabel("\uD83D\uDCCB  About SmartStock");
        title.setFont(AppFonts.CARD_TITLE);
        title.setForeground(AppColors.TEXT_PRIMARY);
        card.add(title, BorderLayout.NORTH);

        JPanel info = new JPanel(new GridLayout(5, 2, 8, 10));
        info.setOpaque(false);

        addInfoRow(info, "Application",
            "SmartStock \u2014 Inventory & Supply Chain");
        addInfoRow(info, "Version",    "2.0.0");
        addInfoRow(info, "Developer",  "Vansheeka Jain");
        addInfoRow(info, "Built With", "Java JFC/Swing + AWT + JDBC");
        addInfoRow(info, "Database",   "MySQL 8.x");

        card.add(info, BorderLayout.CENTER);
        return card;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JTextField addFormRow(JPanel panel, String labelText, String value) {
        JLabel lbl = UIFactory.formLabel(labelText);
        JTextField tf = new JTextField(value);
        tf.setFont(AppFonts.INPUT);
        tf.setBorder(UIFactory.inputBorder());
        panel.add(lbl);
        panel.add(tf);
        return tf;
    }

    private void addInfoRow(JPanel panel, String key, String value) {
        JLabel k = new JLabel(key + ":");
        k.setFont(AppFonts.LABEL_BOLD);
        k.setForeground(AppColors.TEXT_MUTED);
        JLabel v = new JLabel(value);
        v.setFont(AppFonts.BODY);
        v.setForeground(AppColors.TEXT_PRIMARY);
        panel.add(k);
        panel.add(v);
    }

    // Reusable white rounded card panel
    private JPanel buildCard() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.setColor(AppColors.BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        return p;
    }
} 
