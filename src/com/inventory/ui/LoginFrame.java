package com.inventory.ui;

import javax.swing.*;
import java.awt.*;
import com.inventory.controller.AuthController;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Smart Inventory System");
        setSize(460, 340);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(235, 242, 252));
        mainPanel.setLayout(new GridBagLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 1, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(214, 223, 240)),
            BorderFactory.createEmptyBorder(24, 24, 24, 24)
        ));

        JLabel title = new JLabel("Inventory Control Login", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(31, 41, 55));

        JLabel subtitle = new JLabel("Sign in to manage stock, suppliers, orders, and reports.", JLabel.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(new Color(100, 116, 139));

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setForeground(new Color(17, 24, 39));
        usernameField = new JTextField();
        usernameField.setForeground(new Color(17, 24, 39));
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(new Color(17, 24, 39));
        passwordField = new JPasswordField();
        passwordField.setForeground(new Color(17, 24, 39));

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(37, 99, 235));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);

        JLabel hint = new JLabel("Demo: admin / admin", JLabel.CENTER);
        hint.setFont(new Font("Arial", Font.PLAIN, 12));
        hint.setForeground(new Color(71, 85, 105));

        formPanel.add(title);
        formPanel.add(subtitle);
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(loginBtn);
        formPanel.add(hint);

        mainPanel.add(formPanel);
        add(mainPanel);

        loginBtn.addActionListener(e -> login());
        getRootPane().setDefaultButton(loginBtn);
    }

    private void login() {
        try {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            AuthController controller = new AuthController();

            if (controller.login(username, password)) {
                new DashboardFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
