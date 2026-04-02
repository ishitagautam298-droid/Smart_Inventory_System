package com.inventory.ui.frames;

import com.inventory.ui.panels.AlertPanel;
import com.inventory.ui.panels.AlertPanel.AlertItem;
import com.inventory.ui.panels.HeaderPanel;
import com.inventory.ui.panels.StatCardPanel;
import com.inventory.ui.theme.AppColors;
import com.inventory.ui.theme.AppFonts;
import com.inventory.ui.theme.UIFactory;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * JFC: Dashboard page — stat cards, bar chart, alerts, recent orders table.
 * AWT: Graphics2D custom bar chart drawn with GradientPaint.
 * OOPs: Inheritance — extends JPanel.
 */
public class DashboardFrame extends JPanel {

    public DashboardFrame() {
        setBackground(AppColors.BG_APP);
        setLayout(new BorderLayout());
        add(new HeaderPanel("Dashboard"), BorderLayout.NORTH);
        add(buildBody(),                  BorderLayout.CENTER);
    }

    private JScrollPane buildBody() {
        JPanel body = new JPanel();
        body.setBackground(AppColors.BG_APP);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(UIFactory.pagePadding());

        body.add(buildStatCards());
        body.add(Box.createVerticalStrut(22));
        body.add(buildMiddleRow());
        body.add(Box.createVerticalStrut(22));
        body.add(buildBottomRow());
        body.add(Box.createVerticalStrut(20));

        return UIFactory.scrollPane(body);
    }

    // ── 4 KPI Stat Cards ─────────────────────────────────────────────────────
    private JPanel buildStatCards() {
        JPanel row = new JPanel(new GridLayout(1, 4, 16, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 125));

        row.add(new StatCardPanel("\uD83D\uDCE6", "247",
                "Total Products",  "12 added this month", true,
                AppColors.PRIMARY, AppColors.PRIMARY_LIGHT));

        row.add(new StatCardPanel("\uD83D\uDED2", "38",
                "Active Orders",   "5 since yesterday",   true,
                AppColors.INFO,    AppColors.INFO_BG));

        row.add(new StatCardPanel("\u26A0\uFE0F", "9",
                "Low Stock Items", "Needs attention",     false,
                AppColors.DANGER,  AppColors.DANGER_BG));

        row.add(new StatCardPanel("\uD83D\uDCB0", "$84,200",
                "Monthly Revenue", "8.3% vs last month",  true,
                AppColors.SUCCESS, AppColors.SUCCESS_BG));

        return row;
    }

    // ── Middle: Chart + Alerts ────────────────────────────────────────────────
    private JPanel buildMiddleRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 16, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        row.add(buildBarChartCard());
        row.add(buildAlertsCard());
        return row;
    }

    private JPanel buildBarChartCard() {
        JPanel card = makeCard();
        card.setLayout(new BorderLayout(0, 8));

        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);
        JLabel title = new JLabel("Monthly Orders Overview");
        title.setFont(AppFonts.CARD_TITLE);
        title.setForeground(AppColors.TEXT_PRIMARY);
        JLabel sub = UIFactory.mutedLabel("Last 7 months");
        hdr.add(title, BorderLayout.WEST);
        hdr.add(sub,   BorderLayout.EAST);
        card.add(hdr, BorderLayout.NORTH);

        // AWT: Custom Graphics2D bar chart
        JPanel chart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBarChart((Graphics2D) g);
            }
        };
        chart.setOpaque(false);
        card.add(chart, BorderLayout.CENTER);
        return card;
    }

    // AWT: Graphics2D drawing
    private void drawBarChart(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
int[] vals   = {28, 42, 35, 55, 48, 62, 38};
        String[] mths = {"Aug","Sep","Oct","Nov","Dec","Jan","Feb"};
        int max = 70, startX = 30, bottomY = 145,
            chartH = 100, barW = 32, gap = 22;

        // Grid lines
        g2.setColor(new Color(229, 231, 235, 100));
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0));
        for (int i = 1; i <= 4; i++) {
            int y = bottomY - (chartH * i / 4);
            g2.drawLine(startX, y, startX + vals.length * (barW + gap), y);
        }
        g2.setStroke(new BasicStroke(1));

        for (int i = 0; i < vals.length; i++) {
            int barH = (int)((vals[i] / (double) max) * chartH);
            int x    = startX + i * (barW + gap);
            int y    = bottomY - barH;

            // Shadow
            g2.setColor(new Color(99, 102, 241, 20));
            g2.fillRoundRect(x + 2, y + 3, barW, barH, 6, 6);

            // Gradient bar
            GradientPaint gp = new GradientPaint(
                    x, y, new Color(129, 140, 248),
                    x, bottomY, AppColors.PRIMARY_DARK);
            g2.setPaint(gp);
            g2.fillRoundRect(x, y, barW, barH, 6, 6);

            // Labels
            g2.setColor(AppColors.TEXT_SECONDARY);
            g2.setFont(AppFonts.MICRO);
            g2.drawString(String.valueOf(vals[i]), x + barW / 2 - 5, y - 4);
            g2.setColor(AppColors.TEXT_MUTED);
            g2.drawString(mths[i], x + barW / 2 - 9, bottomY + 14);
        }

        // X axis
        g2.setColor(AppColors.BORDER);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(startX - 5, bottomY,
                startX + vals.length * (barW + gap) + 5, bottomY);
    }

    private JPanel buildAlertsCard() {
        JPanel card = makeCard();
        card.setLayout(new BorderLayout());

        AlertItem[] alerts = {
            new AlertItem("danger",  "Critical: USB-C Cable 2m",
                "Only 2 units — reorder now",    "2m ago"),
            new AlertItem("danger",  "Critical: HDMI Cables",
                "Out of stock — 0 units left",   "10m ago"),
            new AlertItem("warning", "Low: Office Chair Ergo",
                "12 left — reorder level 15",    "1h ago"),
            new AlertItem("warning", "Low: Printer Paper A4",
                "20 reams — recommend reorder",  "2h ago"),
            new AlertItem("info",    "Order #1042 Shipped",
                "Estimated delivery in 2 days",  "3h ago"),
        };
        card.add(new AlertPanel(alerts), BorderLayout.CENTER);
        return card;
    }

    // ── Bottom: Recent Orders + Top Products ──────────────────────────────────
    private JPanel buildBottomRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 16, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 270));
        row.add(buildRecentOrdersCard());
        row.add(buildTopProductsCard());
        return row;
    }

    private JPanel buildRecentOrdersCard() {
        JPanel card = makeCard();
        card.setLayout(new BorderLayout(0, 10));

        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);
        JLabel title = new JLabel("Recent Orders");
        title.setFont(AppFonts.CARD_TITLE);
        title.setForeground(AppColors.TEXT_PRIMARY);
        JLabel viewAll = new JLabel("View all \u2192");
        viewAll.setFont(AppFonts.SMALL);
        viewAll.setForeground(AppColors.PRIMARY);
        viewAll.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        hdr.add(title,   BorderLayout.WEST);
        hdr.add(viewAll, BorderLayout.EAST);
        card.add(hdr, BorderLayout.NORTH);

        String[] cols = {"Order", "Product", "Qty", "Status"};
Object[][] data = {
            {"#1045", "Laptop 15\" Pro",  "10", "SHIPPED"},
            {"#1044", "USB-C Hub",        "50", "PROCESSING"},
            {"#1043", "Office Chairs",    "5",  "PENDING"},
            {"#1042", "Printer Paper",   "100", "DELIVERED"},
            {"#1041", "Wireless Mouse",   "25", "DELIVERED"},
        };

        JTable table = new JTable(new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        UIFactory.styleTable(table);

        // Alternating row renderer
        DefaultTableCellRenderer alt = UIFactory.altRowRenderer();
        table.getColumnModel().getColumn(0).setCellRenderer(alt);
        table.getColumnModel().getColumn(1).setCellRenderer(alt);
        table.getColumnModel().getColumn(2).setCellRenderer(alt);

        // Status badge renderer
        table.getColumnModel().getColumn(3).setCellRenderer(
                UIFactory.badgeRenderer());

        table.getColumnModel().getColumn(0).setPreferredWidth(55);
        table.getColumnModel().getColumn(1).setPreferredWidth(145);
        table.getColumnModel().getColumn(2).setPreferredWidth(40);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);

        card.add(UIFactory.scrollPane(table), BorderLayout.CENTER);
        return card;
    }

    private JPanel buildTopProductsCard() {
        JPanel card = makeCard();
        card.setLayout(new BorderLayout(0, 10));

        JLabel title = new JLabel("Top Products by Stock");
        title.setFont(AppFonts.CARD_TITLE);
        title.setForeground(AppColors.TEXT_PRIMARY);
        card.add(title, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        String[][] products = {
            {"Laptop 15\" Pro",  "142", "71"},
            {"USB-C Hub 7-Port", "215", "90"},
            {"Wireless Mouse",   "134", "67"},
            {"Mechanical Keybd", "87",  "43"},
            {"Monitor 27\" IPS", "63",  "31"},
        };
        for (String[] p : products) {
            list.add(buildProgressRow(p[0], p[1], Integer.parseInt(p[2])));
            list.add(Box.createVerticalStrut(9));
        }
        card.add(list, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildProgressRow(String name, String qty, int pct) {
        JPanel row = new JPanel(new BorderLayout(0, 3));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(AppFonts.SMALL_BOLD);
        nameLbl.setForeground(AppColors.TEXT_PRIMARY);

        JLabel qtyLbl = new JLabel(qty + " units");
        qtyLbl.setFont(AppFonts.SMALL);
        qtyLbl.setForeground(AppColors.TEXT_MUTED);

        top.add(nameLbl, BorderLayout.WEST);
        top.add(qtyLbl,  BorderLayout.EAST);

        Color barC = pct < 30 ? AppColors.DANGER
                   : pct < 60 ? AppColors.WARNING
                   : AppColors.SUCCESS;

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(pct);
        bar.setForeground(barC);
        bar.setBackground(AppColors.BORDER);
        bar.setBorderPainted(false);
        bar.setStringPainted(false);
        bar.setPreferredSize(new Dimension(0, 5));

        row.add(top, BorderLayout.NORTH);
        row.add(bar, BorderLayout.CENTER);
        return row;
    }

    // ── Reusable white rounded card panel ─────────────────────────────────────
    // Named makeCard() to avoid any ambiguity with UIFactory.card()
    private JPanel makeCard() {
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

