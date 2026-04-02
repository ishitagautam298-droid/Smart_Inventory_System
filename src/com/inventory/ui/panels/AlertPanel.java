package com.inventory.ui.panels;

import com.inventory.ui.theme.AppColors;
import com.inventory.ui.theme.AppFonts;
import com.inventory.ui.theme.UIFactory;

import javax.swing.*;
import java.awt.*;

/**
 * JFC: Custom scrollable alerts list.
 * OOPs: Inner class AlertItem is a Bean.
 */
public class AlertPanel extends JPanel {

    // OOPs: Inner static class as data carrier (Bean)
    public static class AlertItem {
        public String type, title, message, time;
        public AlertItem(String type, String title, String message, String time) {
            this.type = type; this.title = title;
            this.message = message; this.time = time;
        }
    }

    private final AlertItem[] alerts;

    public AlertPanel(AlertItem[] alerts) {
        this.alerts = alerts;
        setOpaque(false);
        setLayout(new BorderLayout(0, 10));
        buildUI();
    }

    private void buildUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("\uD83D\uDD14  Active Alerts");
        title.setFont(AppFonts.CARD_TITLE);
        title.setForeground(AppColors.TEXT_PRIMARY);
        JLabel countBadge = UIFactory.badge(alerts.length + " alerts",
            AppColors.DANGER_TEXT, AppColors.DANGER_BG);
        header.add(title,      BorderLayout.WEST);
        header.add(countBadge, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Alert list
        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        for (AlertItem a : alerts) {
            list.add(buildRow(a));
            list.add(Box.createVerticalStrut(6));
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildRow(AlertItem a) {
        Color fg, bg, bar;
        String icon;
        switch (a.type) {
            case "danger":
                fg = AppColors.DANGER_TEXT;  bg = AppColors.DANGER_BG;
                bar = AppColors.DANGER;  icon = "\u26D4"; break;
            case "warning":
                fg = AppColors.WARNING_TEXT; bg = AppColors.WARNING_BG;
                bar = AppColors.WARNING; icon = "\u26A0"; break;
            case "success":
                fg = AppColors.SUCCESS_TEXT; bg = AppColors.SUCCESS_BG;
                bar = AppColors.SUCCESS; icon = "\u2705"; break;
            default:
                fg = AppColors.INFO_TEXT;    bg = AppColors.INFO_BG;
                bar = AppColors.INFO;    icon = "\u2139"; break;
        }

        // AWT: Custom Graphics2D rendering
        JPanel row = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(bar);
                g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(1, 6, 1, getHeight() - 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        row.setAlignmentX(LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 10));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        iconLbl.setForeground(bar);
JPanel texts = new JPanel();
        texts.setOpaque(false);
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        JLabel titleLbl = new JLabel(a.title);
        titleLbl.setFont(AppFonts.SMALL_BOLD);
        titleLbl.setForeground(fg);
        JLabel msgLbl = new JLabel(a.message);
        msgLbl.setFont(AppFonts.SMALL);
        msgLbl.setForeground(new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 165));
        texts.add(titleLbl);
        texts.add(msgLbl);

        JLabel timeLbl = new JLabel(a.time);
        timeLbl.setFont(AppFonts.MICRO);
        timeLbl.setForeground(AppColors.TEXT_MUTED);

        row.add(iconLbl, BorderLayout.WEST);
        row.add(texts,   BorderLayout.CENTER);
        row.add(timeLbl, BorderLayout.EAST);
        return row;
    }
}
