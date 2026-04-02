package com.inventory.ui.theme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;

/**
 * JFC/Swing: Factory that builds consistently styled components.
 * OOPs: Abstraction — hides Swing boilerplate behind simple method calls.
 */
public class UIFactory {

    // ── Buttons ──────────────────────────────────────────────────────────────

    public static JButton primaryButton(String text) {
        return colorButton(text, AppColors.PRIMARY, Color.WHITE);
    }
    public static JButton successButton(String text) {
        return colorButton(text, AppColors.SUCCESS, Color.WHITE);
    }
    public static JButton dangerButton(String text) {
        return colorButton(text, AppColors.DANGER, Color.WHITE);
    }
    public static JButton secondaryButton(String text) {
        return colorButton(text, new Color(71,85,105), Color.WHITE);
    }

    private static JButton colorButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed()  ? bg.darker() :
                            getModel().isRollover() ? bg.brighter() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(AppFonts.BUTTON);
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 22, 36));
        return btn;
    }

    public static JButton outlineButton(String text, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover()
                    ? new Color(fg.getRed(),fg.getGreen(),fg.getBlue(),20)
                    : new Color(0,0,0,0));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(fg);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(AppFonts.BUTTON);
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 20, 36));
        return btn;
    }

    // ── Inputs ───────────────────────────────────────────────────────────────

    public static JTextField textField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(AppFonts.INPUT);
        tf.setForeground(AppColors.TEXT_MUTED);
        tf.setText(placeholder);
        tf.setBorder(inputBorder());
        // EBT: FocusListener for placeholder behaviour
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) {
                    tf.setText("");
                    tf.setForeground(AppColors.TEXT_PRIMARY);
}
                tf.setBorder(inputBorderFocus());
            }
            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) {
                    tf.setText(placeholder);
                    tf.setForeground(AppColors.TEXT_MUTED);
                }
                tf.setBorder(inputBorder());
            }
        });
        return tf;
    }

    public static JPasswordField passwordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(AppFonts.INPUT);
        pf.setBorder(inputBorder());
        pf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { pf.setBorder(inputBorderFocus()); }
            public void focusLost (FocusEvent e)  { pf.setBorder(inputBorder()); }
        });
        return pf;
    }

    public static JComboBox<String> comboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(AppFonts.INPUT);
        cb.setBackground(Color.WHITE);
        return cb;
    }

    // ── Labels ───────────────────────────────────────────────────────────────

    public static JLabel formLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(AppFonts.LABEL_BOLD);
        l.setForeground(AppColors.TEXT_PRIMARY);
        return l;
    }

    public static JLabel mutedLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(AppFonts.SMALL);
        l.setForeground(AppColors.TEXT_MUTED);
        return l;
    }

    // ── Badges ───────────────────────────────────────────────────────────────

    public static JLabel badge(String text, Color fg, Color bg) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lbl.setFont(AppFonts.SMALL_BOLD);
        lbl.setForeground(fg);
        lbl.setOpaque(false);
        lbl.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        return lbl;
    }

    public static JLabel statusBadge(String status) {
        switch (status) {
            case "DELIVERED":   case "IN STOCK": case "ACTIVE":
                return badge(status, AppColors.SUCCESS_TEXT, AppColors.SUCCESS_BG);
            case "SHIPPED":     case "IN TRANSIT":
                return badge(status, AppColors.INFO_TEXT, AppColors.INFO_BG);
            case "PROCESSING":
                return badge(status, AppColors.WARNING_TEXT, AppColors.WARNING_BG);
            case "PENDING":
                return badge(status, AppColors.ORANGE_TEXT, AppColors.ORANGE_BG);
            case "CANCELLED":   case "OUT OF STOCK": case "INACTIVE":
                return badge(status, AppColors.DANGER_TEXT, AppColors.DANGER_BG);
            case "LOW STOCK":
                return badge(status, AppColors.WARNING_TEXT, AppColors.WARNING_BG);
            default:
                return badge(status, AppColors.TEXT_SECONDARY, AppColors.BG_ROW_ALT);
        }
    }

    // ── Table ────────────────────────────────────────────────────────────────

    public static void styleTable(JTable table) {
        table.setFont(AppFonts.TABLE_CELL);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(AppColors.TABLE_SEL_BG);
        table.setBackground(AppColors.BG_CARD);
        table.setFillsViewportHeight(true);
JTableHeader th = table.getTableHeader();
        th.setFont(AppFonts.TABLE_HDR);
        th.setBackground(AppColors.TABLE_HDR_BG);
        th.setForeground(AppColors.TABLE_HDR_FG);
        th.setBorder(null);
        th.setPreferredSize(new Dimension(0, 42));
        th.setReorderingAllowed(false);
    }

    // Standard alternating-row cell renderer
    public static javax.swing.table.DefaultTableCellRenderer altRowRenderer() {
        return new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) setBackground(row % 2 == 0 ? Color.WHITE : AppColors.BG_ROW_ALT);
                setForeground(AppColors.TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                setFont(AppFonts.TABLE_CELL);
                return this;
            }
        };
    }

    // Badge renderer for table status columns
    public static javax.swing.table.TableCellRenderer badgeRenderer() {
        return (t, v, sel, foc, row, col) -> {
            JLabel b = statusBadge(String.valueOf(v));
            JPanel w = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
            w.setBackground(row % 2 == 0 ? Color.WHITE : AppColors.BG_ROW_ALT);
            w.add(b);
            return w;
        };
    }

    // ── ScrollPane ───────────────────────────────────────────────────────────

    public static JScrollPane scrollPane(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(null);
        sp.getViewport().setBackground(AppColors.BG_CARD);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    // ── Borders ──────────────────────────────────────────────────────────────

    public static Border inputBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppColors.BORDER, 1),
            BorderFactory.createEmptyBorder(7, 12, 7, 12));
    }
    public static Border inputBorderFocus() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppColors.BORDER_FOCUS, 2),
            BorderFactory.createEmptyBorder(6, 11, 6, 11));
    }
    public static Border cardBorder() {
        return BorderFactory.createLineBorder(AppColors.BORDER, 1);
    }
    public static Border pagePadding() {
        return BorderFactory.createEmptyBorder(24, 28, 24, 28);
    }
}
