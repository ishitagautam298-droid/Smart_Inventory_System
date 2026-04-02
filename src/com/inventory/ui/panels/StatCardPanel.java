package com.inventory.ui.panels;

import com.inventory.ui.theme.AppColors;
import com.inventory.ui.theme.AppFonts;

import javax.swing.*;
import java.awt.*;

/**
 * JFC: Custom JPanel drawn with Graphics2D.
 * OOPs: Encapsulation of KPI card data and rendering.
 * AWT: Graphics2D, GradientPaint, RenderingHints.
 */
public class StatCardPanel extends JPanel {

    private final String icon, value, label, trend;
    private final boolean trendUp;
    private final Color accentColor, accentBg;

    public StatCardPanel(String icon, String value, String label,
                         String trend, boolean trendUp,
                         Color accentColor, Color accentBg) {
        this.icon = icon; this.value = value; this.label = label;
        this.trend = trend; this.trendUp = trendUp;
        this.accentColor = accentColor; this.accentBg = accentBg;

        setOpaque(false);
        setLayout(new BorderLayout(0, 6));
        setPreferredSize(new Dimension(200, 120));
        setBorder(BorderFactory.createEmptyBorder(18, 20, 16, 20));
        buildUI();
    }

    private void buildUI() {
        // Top: label + icon badge
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel lbl = new JLabel(label.toUpperCase());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(AppColors.TEXT_MUTED);

        // JFC: Custom painted icon badge
        JLabel iconLbl = new JLabel(icon, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accentBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLbl.setPreferredSize(new Dimension(42, 42));

        top.add(lbl,     BorderLayout.CENTER);
        top.add(iconLbl, BorderLayout.EAST);

        // Value
        JLabel valLbl = new JLabel(value);
        valLbl.setFont(AppFonts.STAT_VAL);
        valLbl.setForeground(AppColors.TEXT_PRIMARY);

        // Trend
        JPanel trendRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        trendRow.setOpaque(false);
        JLabel arrow = new JLabel(trendUp ? "\u2191" : "\u2193");
        arrow.setFont(AppFonts.SMALL_BOLD);
        arrow.setForeground(trendUp ? AppColors.SUCCESS : AppColors.DANGER);
        JLabel trendLbl = new JLabel(trend);
        trendLbl.setFont(AppFonts.SMALL);
        trendLbl.setForeground(AppColors.TEXT_MUTED);
        trendRow.add(arrow);
        trendRow.add(trendLbl);

        add(top,      BorderLayout.NORTH);
        add(valLbl,   BorderLayout.CENTER);
        add(trendRow, BorderLayout.SOUTH);
    }

    // AWT: Custom painting with shadow + accent bar
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Drop shadow
        g2.setColor(new Color(0, 0, 0, 14));
        g2.fillRoundRect(3, 5, getWidth() - 4, getHeight() - 4, 12, 12);
        // White card
        g2.setColor(AppColors.BG_CARD);
        g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 4, 12, 12);
        // Left accent bar
        g2.setColor(accentColor);
        g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(2, 18, 2, getHeight() - 18);
        g2.dispose();
        super.paintComponent(g);
    }
}
