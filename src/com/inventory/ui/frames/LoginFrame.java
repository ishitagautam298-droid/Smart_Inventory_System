package com.inventory.ui.frames;

import com.inventory.backend.UserDAO;
import com.inventory.model.UserBean;
import com.inventory.ui.theme.AppColors;
import com.inventory.ui.theme.AppFonts;
import com.inventory.ui.theme.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * JFC: JFrame — split login screen.
 * EBT: ActionListener (login button), KeyAdapter (Enter key), FocusAdapter.
 * JDBC: Calls UserDAO.authenticate().
 * OOPs: Encapsulates login logic.
 */
public class LoginFrame extends JFrame {

    private JTextField    usernameField;
    private JPasswordField passwordField;
    private JLabel         errorLabel;
    private final UserDAO  userDAO = new UserDAO();  // JDBC

    public LoginFrame() {
        setTitle("SmartStock — Sign In");
        setSize(920, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2, 0, 0));

        add(buildLeftPanel());
        add(buildRightPanel());
        setVisible(true);
    }

    // ── Left brand panel (AWT: GradientPaint) ───────────────────────────────
    private JPanel buildLeftPanel() {
        JPanel left = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(49, 46, 129),
                    getWidth(), getHeight(), new Color(99, 102, 241));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Decorative circles
                g2.setColor(new Color(255, 255, 255, 12));
                g2.fillOval(-60, -60, 280, 280);
                g2.fillOval(getWidth() - 90, getHeight() - 90, 240, 240);
            }
        };
        left.setLayout(new GridBagLayout());

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

        JLabel logoIcon = new JLabel("\uD83D\uDCE6");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        logoIcon.setAlignmentX(CENTER_ALIGNMENT);

        JLabel appName = new JLabel("SmartStock");
        appName.setFont(AppFonts.LOGIN_BRAND);
        appName.setForeground(Color.WHITE);
        appName.setAlignmentX(CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("Inventory & Supply Chain");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tagline.setForeground(new Color(199, 210, 254));
        tagline.setAlignmentX(CENTER_ALIGNMENT);

        inner.add(logoIcon);
        inner.add(Box.createVerticalStrut(8));
        inner.add(appName);
        inner.add(Box.createVerticalStrut(4));
        inner.add(tagline);
        inner.add(Box.createVerticalStrut(28));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 40));
        sep.setMaximumSize(new Dimension(200, 1));
        sep.setAlignmentX(CENTER_ALIGNMENT);
        inner.add(sep);
        inner.add(Box.createVerticalStrut(22));

        String[][] features = {
            {"\uD83D\uDCCA", "Live inventory dashboard"},
            {"\uD83D\uDD14", "Smart low-stock alerts"},
            {"\uD83D\uDE9A", "Order & shipment tracking"},
            {"\uD83C\uDFED", "Supplier management portal"},
            {"\uD83D\uDCC8", "Reports & analytics"},
        };
        for (String[] f : features) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 3));
row.setOpaque(false); row.setAlignmentX(LEFT_ALIGNMENT);
            JLabel ico = new JLabel(f[0]);
            ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
            JLabel txt = new JLabel(f[1]);
            txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            txt.setForeground(new Color(224, 231, 255));
            row.add(ico); row.add(txt);
            inner.add(row);
        }

        left.add(inner);
        return left;
    }

    // ── Right form panel ─────────────────────────────────────────────────────
    private JPanel buildRightPanel() {
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(Color.WHITE);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(380, 480));
        form.setBorder(BorderFactory.createEmptyBorder(0, 48, 0, 48));

        JLabel welcome = new JLabel("Welcome back \uD83D\uDC4B");
        welcome.setFont(AppFonts.LOGIN_TITLE);
        welcome.setForeground(AppColors.TEXT_PRIMARY);
        welcome.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Sign in to your account to continue");
        sub.setFont(AppFonts.BODY);
        sub.setForeground(AppColors.TEXT_MUTED);
        sub.setAlignmentX(LEFT_ALIGNMENT);

        form.add(welcome);
        form.add(Box.createVerticalStrut(4));
        form.add(sub);
        form.add(Box.createVerticalStrut(32));

        // Username field
        JLabel userLbl = UIFactory.formLabel("Username");
        userLbl.setAlignmentX(LEFT_ALIGNMENT);
        usernameField = UIFactory.textField("Enter your username");
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        usernameField.setAlignmentX(LEFT_ALIGNMENT);

        // Password field
        JLabel passLbl = UIFactory.formLabel("Password");
        passLbl.setAlignmentX(LEFT_ALIGNMENT);
        passwordField = UIFactory.passwordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        passwordField.setAlignmentX(LEFT_ALIGNMENT);

        // EBT: KeyAdapter — login on Enter key
        KeyAdapter enterKey = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) authenticate();
            }
        };
        usernameField.addKeyListener(enterKey);
        passwordField.addKeyListener(enterKey);

        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(AppFonts.SMALL);
        errorLabel.setForeground(AppColors.DANGER);
        errorLabel.setAlignmentX(LEFT_ALIGNMENT);

        // Login button
        JButton loginBtn = new JButton("Sign In  \u2192") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0,
                    getModel().isRollover() ? AppColors.PRIMARY_DARK : AppColors.PRIMARY,
                    getWidth(), 0, new Color(139, 92, 246));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loginBtn.setAlignmentX(LEFT_ALIGNMENT);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // EBT: ActionListener
        loginBtn.addActionListener(e -> authenticate());
// Demo hint
        JPanel hint = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        hint.setBackground(AppColors.INFO_BG);
        hint.setBorder(BorderFactory.createLineBorder(AppColors.INFO, 1));
        hint.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        hint.setAlignmentX(LEFT_ALIGNMENT);
        JLabel hintLbl = new JLabel("\uD83D\uDCA1  Demo: admin / admin123");
        hintLbl.setFont(AppFonts.SMALL);
        hintLbl.setForeground(AppColors.INFO_TEXT);
        hint.add(hintLbl);

        form.add(userLbl);
        form.add(Box.createVerticalStrut(5));
        form.add(usernameField);
        form.add(Box.createVerticalStrut(14));
        form.add(passLbl);
        form.add(Box.createVerticalStrut(5));
        form.add(passwordField);
        form.add(Box.createVerticalStrut(8));
        form.add(errorLabel);
        form.add(Box.createVerticalStrut(10));
        form.add(loginBtn);
        form.add(Box.createVerticalStrut(18));
        form.add(hint);

        right.add(form);
        return right;
    }

    // JDBC authentication
    private void authenticate() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (user.isEmpty() || user.equals("Enter your username")) {
            showError("Please enter your username."); return;
        }
        if (pass.isEmpty()) {
            showError("Please enter your password."); return;
        }

        // Try real DB first; fallback to demo credentials
        UserBean loggedIn = userDAO.authenticate(user, pass);
        if (loggedIn != null || (user.equals("admin") && pass.equals("admin123"))) {
            dispose();
            new MainFrame();
        } else {
            showError("Invalid username or password.");
            passwordField.setText("");
        }
    }

    private void showError(String msg) {
        errorLabel.setText("  \u26A0  " + msg);
    }
}
