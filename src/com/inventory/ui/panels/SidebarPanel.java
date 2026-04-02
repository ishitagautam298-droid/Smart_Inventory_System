package com.inventory.ui.panels;

import com.inventory.ui.theme.AppColors;
import com.inventory.ui.theme.AppFonts;
import com.inventory.ui.frames.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * JFC: JPanel — Dark sidebar navigation.
 * EBT: MouseListener on each menu item.
 * OOPs: Encapsulation of navigation logic.
 */
public class SidebarPanel extends JPanel {

    private final JPanel contentArea;
    private String activeLabel = "Dashboard";

    // OOPs: Data encapsulated in array
    private static final String[][] MENU = {
        {"\uD83D\uDCCA", "Dashboard"},
        {"\uD83D\uDCE6", "Inventory"},
        {"\uD83D\uDED2", "Orders"},
        {"\uD83C\uDFED", "Suppliers"},
        {"\uD83D\uDE9A", "Shipments"},
        {"\uD83D\uDCC8", "Reports"},
    };

    public SidebarPanel(JPanel contentArea) {
        this.contentArea = contentArea;
        setBackground(AppColors.SIDEBAR_BG);
        setPreferredSize(new Dimension(230, 0));
        setLayout(new BorderLayout());
        add(buildLogo(),   BorderLayout.NORTH);
        add(buildMenu(),   BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildLogo() {
        JPanel logo = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 16));
        logo.setBackground(AppColors.SIDEBAR_BG);
        logo.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(31, 41, 55)));

        JLabel icon = new JLabel("\uD83D\uDCE6");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel name = new JLabel("SmartStock");
        name.setFont(AppFonts.APP_NAME);
        name.setForeground(AppColors.TEXT_WHITE);
        JLabel sub  = new JLabel("Inventory Suite v2.0");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        sub.setForeground(AppColors.SIDEBAR_SECT);
        text.add(name);
        text.add(sub);

        logo.add(icon);
        logo.add(text);
        return logo;
    }

    private JPanel buildMenu() {
        JPanel menu = new JPanel();
        menu.setBackground(AppColors.SIDEBAR_BG);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

        addSectionLabel(menu, "MAIN MENU");

        for (String[] entry : MENU) {
            menu.add(buildItem(entry[0], entry[1]));
            menu.add(Box.createVerticalStrut(2));
        }

        menu.add(Box.createVerticalStrut(14));
        addSectionLabel(menu, "ACCOUNT");
        menu.add(buildItem("\u2699", "Settings"));
        menu.add(Box.createVerticalStrut(2));
        menu.add(buildItem("\uD83D\uDEAA", "Logout"));

        return menu;
    }

    private void addSectionLabel(JPanel menu, String text) {
        JLabel l = new JLabel("  " + text);
        l.setFont(AppFonts.SIDEBAR_SEC);
        l.setForeground(AppColors.SIDEBAR_SECT);
        l.setBorder(BorderFactory.createEmptyBorder(10, 10, 6, 0));
        l.setAlignmentX(LEFT_ALIGNMENT);
        menu.add(l);
    }

    private JPanel buildItem(String icon, String label) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (label.equals(activeLabel)) {
                    g2.setColor(AppColors.SIDEBAR_ACTIVE);
                    g2.fillRoundRect(8, 2, getWidth() - 16, getHeight() - 4, 8, 8);
                    // Left accent bar
                    g2.setColor(new Color(165, 180, 252));
g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(3, 8, 3, getHeight() - 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        item.setOpaque(false);
        item.setMaximumSize(new Dimension(230, 44));
        item.setAlignmentX(LEFT_ALIGNMENT);
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        boolean active = label.equals(activeLabel);

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        iconLbl.setForeground(active ? AppColors.TEXT_WHITE : AppColors.SIDEBAR_TEXT);

        JLabel textLbl = new JLabel(label);
        textLbl.setFont(active ? new Font("Segoe UI", Font.BOLD, 13) : AppFonts.SIDEBAR);
        textLbl.setForeground(active ? AppColors.TEXT_WHITE : AppColors.SIDEBAR_TEXT);

        item.add(iconLbl);
        item.add(textLbl);

        // EBT: MouseListener for navigation and hover
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ("Logout".equals(label))   { handleLogout(); return; }
                if ("Settings".equals(label)) { return; }
                activeLabel = label;
                navigateTo(label);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!label.equals(activeLabel))
                    item.setBackground(AppColors.SIDEBAR_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(AppColors.SIDEBAR_BG);
            }
        });

        return item;
    }

    // OOPs: Polymorphism — each case returns a different JPanel subclass
    private void navigateTo(String label) {
        JPanel page;
        switch (label) {
            case "Dashboard": page = new DashboardFrame(); break;
            case "Inventory": page = new InventoryFrame(); break;
            case "Orders":    page = new OrderFrame();     break;
            case "Suppliers": page = new SupplierFrame();  break;
            case "Shipments": page = new ShipmentFrame();  break;
            case "Reports":   page = new ReportFrame();    break;
            default: return;
        }
        // Rebuild sidebar to update active highlight
        removeAll();
        add(buildLogo(),   BorderLayout.NORTH);
        add(buildMenu(),   BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
        revalidate(); repaint();

        contentArea.removeAll();
        contentArea.add(page, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?", "Confirm Logout",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            SwingUtilities.getWindowAncestor(this).dispose();
            new LoginFrame();
        }
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 12));
        footer.setBackground(new Color(10, 14, 23));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(31, 41, 55)));

        // AWT: Drawing avatar circle using Graphics
        JLabel avatar = new JLabel("A", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
g2.setColor(AppColors.PRIMARY);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        avatar.setForeground(Color.WHITE);
        avatar.setPreferredSize(new Dimension(36, 36));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel name = new JLabel("Admin User");
        name.setFont(AppFonts.BODY_BOLD);
        name.setForeground(AppColors.TEXT_WHITE);

        JLabel role = new JLabel("Super Administrator");
        role.setFont(AppFonts.SMALL);
        role.setForeground(AppColors.SIDEBAR_TEXT);

        JLabel dot = new JLabel("\u2022 Online");
        dot.setFont(AppFonts.MICRO);
        dot.setForeground(AppColors.SUCCESS);

        info.add(name);
        info.add(role);
        info.add(dot);

        footer.add(avatar);
        footer.add(info);
        return footer;
    }
}
