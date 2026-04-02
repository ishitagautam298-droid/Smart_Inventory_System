package com.inventory.ui.frames;

import com.inventory.ui.panels.HeaderPanel;
import com.inventory.ui.theme.AppColors;
import com.inventory.ui.theme.AppFonts;
import com.inventory.ui.theme.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
/**
 * JFC: Reports & Analytics page with custom charts.
 * AWT: Graphics2D line chart and pie chart.
 * EBT: ActionListener on report export buttons.
 */
public class ReportFrame extends JPanel {

    public ReportFrame() {
        setBackground(AppColors.BG_APP);
        setLayout(new BorderLayout());
        add(new HeaderPanel("Reports & Analytics"), BorderLayout.NORTH);
        add(UIFactory.scrollPane(buildBody()),       BorderLayout.CENTER);
    }

    private JPanel buildBody() {
        JPanel body = new JPanel();
        body.setBackground(AppColors.BG_APP);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(UIFactory.pagePadding());

        body.add(buildKPIRow());
        body.add(Box.createVerticalStrut(22));
        body.add(buildChartRow());
        body.add(Box.createVerticalStrut(22));
        body.add(buildQuickReports());
        body.add(Box.createVerticalStrut(20));
        return body;
    }

    // ── 4 KPI Cards ──────────────────────────────────────────────────────────
    private JPanel buildKPIRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 16, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        row.add(kpiCard("Total Revenue",   "$84,200","+8.3%",  true,  AppColors.SUCCESS, AppColors.SUCCESS));
        row.add(kpiCard("Orders Placed",   "142",    "+5 week",true,  AppColors.INFO,    AppColors.INFO));
        row.add(kpiCard("Items Sold",      "1,043",  "+12.1%", true,  AppColors.PRIMARY, AppColors.PRIMARY));
        row.add(kpiCard("Avg Order Value", "$593",   "-2.4%",  false, AppColors.WARNING, AppColors.WARNING));
        return row;
    }

    private JPanel kpiCard(String label, String val, String trend, boolean up, Color fg, Color bar) {
        JPanel p = new JPanel(new BorderLayout(0, 4)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.BG_CARD);
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                g2.setColor(bar); g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(2, 12, 2, getHeight() - 12);
                g2.dispose(); super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JLabel lblTop = new JLabel(label.toUpperCase());
        lblTop.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblTop.setForeground(AppColors.TEXT_MUTED);

        JLabel valLbl = new JLabel(val);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valLbl.setForeground(AppColors.TEXT_PRIMARY);

        JPanel trendRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        trendRow.setOpaque(false);
        JLabel arr = new JLabel(up ? "\u2191" : "\u2193");
        arr.setFont(AppFonts.SMALL_BOLD); arr.setForeground(up ? AppColors.SUCCESS : AppColors.DANGER);
        JLabel trendLbl = new JLabel(trend); trendLbl.setFont(AppFonts.SMALL); trendLbl.setForeground(AppColors.TEXT_MUTED);
        trendRow.add(arr); trendRow.add(trendLbl);

        p.add(lblTop,    BorderLayout.NORTH);
        p.add(valLbl,    BorderLayout.CENTER);
        p.add(trendRow,  BorderLayout.SOUTH);
        return p;
    }

    // ── Charts Row ────────────────────────────────────────────────────────────
private JPanel buildChartRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 16, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        row.add(buildLineChartCard());
        row.add(buildPieChartCard());
        return row;
    }

    private JPanel buildLineChartCard() {
        JPanel card = buildCard();
        card.setLayout(new BorderLayout(0, 8));
        JLabel title = new JLabel("Revenue Trend (8 Months)");
        title.setFont(AppFonts.CARD_TITLE); title.setForeground(AppColors.TEXT_PRIMARY);
        card.add(title, BorderLayout.NORTH);

        // AWT: Custom Graphics2D line chart
        JPanel chart = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawLineChart((Graphics2D) g);
            }
        };
        chart.setOpaque(false);
        card.add(chart, BorderLayout.CENTER);
        return card;
    }

    private void drawLineChart(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int[] vals = {52000, 61000, 58000, 73000, 69000, 78000, 84200, 79000};
        String[] mths = {"Jul","Aug","Sep","Oct","Nov","Dec","Jan","Feb"};
        int max = 90000, startX = 40, bottomY = 170, chartH = 130, step = 70;

        // Grid
        g2.setColor(new Color(229,231,235,90));
        g2.setStroke(new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[]{3},0));
        for (int i = 1; i <= 4; i++) {
            int y = bottomY - (chartH * i / 4);
            g2.drawLine(startX, y, startX + (vals.length-1)*step, y);
        }

        int[] xP = new int[vals.length], yP = new int[vals.length];
        for (int i = 0; i < vals.length; i++) {
            xP[i] = startX + i * step;
            yP[i] = bottomY - (int)((vals[i]/(double)max)*chartH);
        }

        // Fill under line
        int[] fx = Arrays.copyOf(xP, vals.length+2), fy = Arrays.copyOf(yP, vals.length+2);
        fx[vals.length] = xP[vals.length-1]; fy[vals.length] = bottomY;
        fx[vals.length+1] = xP[0];           fy[vals.length+1] = bottomY;
        g2.setColor(new Color(99,102,241,28));
        g2.fillPolygon(fx, fy, vals.length+2);

        // Line
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(AppColors.PRIMARY);
        for (int i = 0; i < vals.length-1; i++) g2.drawLine(xP[i], yP[i], xP[i+1], yP[i+1]);

        // Dots + labels
        for (int i = 0; i < vals.length; i++) {
            g2.setColor(Color.WHITE); g2.fillOval(xP[i]-5, yP[i]-5, 10, 10);
            g2.setColor(AppColors.PRIMARY); g2.setStroke(new BasicStroke(2)); g2.drawOval(xP[i]-5, yP[i]-5, 10, 10);
            g2.setColor(AppColors.TEXT_MUTED); g2.setFont(AppFonts.MICRO);
            g2.drawString(mths[i], xP[i]-8, bottomY+14);
        }
    }

    private JPanel buildPieChartCard() {
        JPanel card = buildCard();
        card.setLayout(new BorderLayout(0, 8));
        JLabel title = new JLabel("Stock by Category");
        title.setFont(AppFonts.CARD_TITLE); title.setForeground(AppColors.TEXT_PRIMARY);
        card.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(1, 2, 10, 0));
        content.setOpaque(false);

        // AWT: Custom pie chart
        JPanel pie = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawPieChart((Graphics2D) g);
            }
        };
        pie.setOpaque(false);

        // Legend
        JPanel legend = new JPanel();
        legend.setOpaque(false);
        legend.setLayout(new BoxLayout(legend, BoxLayout.Y_AXIS));
        legend.setBorder(BorderFactory.createEmptyBorder(28, 0, 0, 0));
String[][] cats = {
            {"Electronics","38%","#6366F1"},
            {"Accessories","24%","#3B82F6"},
            {"Furniture",  "16%","#10B981"},
            {"Peripherals","14%","#F59E0B"},
            {"Stationery", "8%", "#EF4444"},
        };
        for (String[] c : cats) {
            JPanel r = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 3));
            r.setOpaque(false);
            JLabel dot = new JLabel("\u25CF"); dot.setFont(AppFonts.BODY_BOLD); dot.setForeground(Color.decode(c[2]));
            JLabel lbl = new JLabel(c[0] + "  " + c[1]); lbl.setFont(AppFonts.SMALL); lbl.setForeground(AppColors.TEXT_SECONDARY);
            r.add(dot); r.add(lbl);
            legend.add(r);
        }

        content.add(pie);
        content.add(legend);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private void drawPieChart(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int cx = 80, cy = 80, r = 68;
        int[] pcts = {38, 24, 16, 14, 8};
        Color[] cols = {AppColors.PRIMARY, AppColors.INFO, AppColors.SUCCESS, AppColors.WARNING, AppColors.DANGER};
        int angle = 0;
        for (int i = 0; i < pcts.length; i++) {
            int arc = (int)(pcts[i]/100.0*360);
            g2.setColor(cols[i]);
            g2.fillArc(cx-r, cy-r, 2*r, 2*r, angle, arc);
            angle += arc;
        }
        // Donut hole
        g2.setColor(AppColors.BG_CARD);
        g2.fillOval(cx-34, cy-34, 68, 68);
        g2.setColor(AppColors.TEXT_MUTED); g2.setFont(AppFonts.SMALL_BOLD);
        g2.drawString("100%", cx-16, cy+4);
    }

    // ── Quick Report Export Buttons ───────────────────────────────────────────
    private JPanel buildQuickReports() {
        JPanel card = buildCard();
        card.setLayout(new BorderLayout(0, 14));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        JLabel title = new JLabel("Quick Reports  \u2014  Export or Print");
        title.setFont(AppFonts.CARD_TITLE); title.setForeground(AppColors.TEXT_PRIMARY);
        card.add(title, BorderLayout.NORTH);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        btns.setOpaque(false);

        String[][] reports = {
            {"\uD83D\uDCCA","Inventory Summary"},
            {"\uD83D\uDED2","Order History"},
            {"\uD83C\uDFED","Supplier Report"},
            {"\uD83D\uDCB0","Revenue Report"},
            {"\u26A0\uFE0F", "Low Stock Report"},
            {"\uD83D\uDE9A","Shipment Report"},
        };

        for (String[] rpt : reports) {
            JButton btn = new JButton(rpt[0] + "  " + rpt[1]) {
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getModel().isRollover() ? AppColors.BG_APP : AppColors.BG_CARD);
                    g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);
                    g2.setColor(AppColors.BORDER); g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);
                    g2.dispose(); super.paintComponent(g);
                }
            };
            btn.setFont(AppFonts.BODY);
            btn.setForeground(AppColors.TEXT_PRIMARY);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(185, 42));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            // EBT: ActionListener
            btn.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                    "Generating " + rpt[1] + "...\nConnect to ReportDAO to export PDF/CSV.",
                    "Report", JOptionPane.INFORMATION_MESSAGE));
            btns.add(btn);
        }

        card.add(btns, BorderLayout.CENTER);
        return card;
    }
// Reusable white rounded card panel
    private JPanel buildCard() {
        JPanel p = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.BG_CARD);
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);
                g2.setColor(AppColors.BORDER);
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);
                g2.dispose(); super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        return p;
    }
}
