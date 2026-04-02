package com.inventory.ui.panels;

import com.inventory.ui.theme.AppColors;
import com.inventory.ui.theme.AppFonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * JFC: Reusable top header bar for every page.
 * EBT: FocusListener on search field.
 * AWT: BorderLayout, FlowLayout, Color, Font.
 */
public class HeaderPanel extends JPanel {

    private final String title;

    public HeaderPanel(String title) {
        this.title = title;
        setBackground(AppColors.BG_HEADER);
        setPreferredSize(new Dimension(0, 68));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, AppColors.BORDER),
            BorderFactory.createEmptyBorder(0, 28, 0, 24)));
        setLayout(new BorderLayout());
        add(buildLeft(),  BorderLayout.WEST);
        add(buildRight(), BorderLayout.EAST);
    }

    private JPanel buildLeft() {
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(AppFonts.PAGE_TITLE);
        titleLbl.setForeground(AppColors.TEXT_PRIMARY);

        JLabel crumb = new JLabel("Home  \u203A  " + title);
        crumb.setFont(AppFonts.SMALL);
        crumb.setForeground(AppColors.TEXT_MUTED);

        left.add(Box.createVerticalGlue());
        left.add(titleLbl);
        left.add(crumb);
        left.add(Box.createVerticalGlue());
        return left;
    }

    private JPanel buildRight() {
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        // Search box
        JPanel search = new JPanel(new BorderLayout(6, 0));
        search.setBackground(AppColors.BG_APP);
        search.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppColors.BORDER, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        JLabel icon = new JLabel("\uD83D\uDD0D");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));

        JTextField tf = new JTextField(16);
        tf.setBorder(null);
        tf.setBackground(AppColors.BG_APP);
        tf.setFont(AppFonts.INPUT);
        tf.setForeground(AppColors.TEXT_MUTED);
        tf.setText("Search...");
        // EBT: FocusListener
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals("Search...")) {
                    tf.setText("");
                    tf.setForeground(AppColors.TEXT_PRIMARY);
                }
            }
            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) {
                    tf.setText("Search...");
                    tf.setForeground(AppColors.TEXT_MUTED);
                }
            }
        });
        search.add(icon, BorderLayout.WEST);
        search.add(tf,   BorderLayout.CENTER);

        // Date chip
        String today = new SimpleDateFormat("EEE, dd MMM yyyy").format(new Date());
        JLabel dateLbl = new JLabel("\uD83D\uDCC5  " + today);
        dateLbl.setFont(AppFonts.SMALL);
        dateLbl.setForeground(AppColors.TEXT_SECONDARY);
        dateLbl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppColors.BORDER, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        dateLbl.setBackground(AppColors.BG_APP);
        dateLbl.setOpaque(true);

        right.add(search);
        right.add(dateLbl);
        right.add(circleBtn("\uD83D\uDD14", AppColors.WARNING_BG, AppColors.WARNING));
        right.add(circleBtn("?",            AppColors.INFO_BG,    AppColors.INFO));
        return right;
    }
private JButton circleBtn(String lbl, Color bg, Color fg) {
        JButton btn = new JButton(lbl) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btn.setForeground(fg);
        btn.setPreferredSize(new Dimension(34, 34));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
