package com.inventory.ui.frames;

import com.inventory.ui.panels.HeaderPanel;
import com.inventory.ui.theme.AppColors;
import com.inventory.ui.theme.AppFonts;
import com.inventory.ui.theme.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
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
        row.add(kpiCard("Total Revenue",   "$84,200", "+8.3%",  true,  AppColors.SUCCESS));
        row.add(kpiCard("Orders Placed",   "142",     "+5 week",true,  AppColors.INFO));
        row.add(kpiCard("Items Sold",      "1,043",   "+12.1%", true,  AppColors.PRIMARY));
        row.add(kpiCard("Avg Order Value", "$593",    "-2.4%",  false, AppColors.WARNING));
        return row;
    }

    private JPanel kpiCard(String label, String val, String trend,
                            boolean up, Color bar) {
        JPanel p = new JPanel(new BorderLayout(0, 4)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.setColor(bar);
                g2.setStroke(new BasicStroke(3.5f,
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(2, 12, 2, getHeight()-12);
                g2.dispose();
                super.paintComponent(g);
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
        arr.setFont(AppFonts.SMALL_BOLD);
        arr.setForeground(up ? AppColors.SUCCESS : AppColors.DANGER);
        JLabel trendLbl = new JLabel(trend);
        trendLbl.setFont(AppFonts.SMALL);
        trendLbl.setForeground(AppColors.TEXT_MUTED);
        trendRow.add(arr);
        trendRow.add(trendLbl);

        p.add(lblTop,   BorderLayout.NORTH);
        p.add(valLbl,   BorderLayout.CENTER);
        p.add(trendRow, BorderLayout.SOUTH);
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
        title.setFont(AppFonts.CARD_TITLE);
        title.setForeground(AppColors.TEXT_PRIMARY);
        card.add(title, BorderLayout.NORTH);
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
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int[] vals  = {52000, 61000, 58000, 73000, 69000, 78000, 84200, 79000};
        String[] mths = {"Jul","Aug","Sep","Oct","Nov","Dec","Jan","Feb"};
        int max = 90000, startX = 40, bottomY = 170, chartH = 130, step = 70;

        g2.setColor(new Color(229, 231, 235, 90));
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{3}, 0));
        for (int i = 1; i <= 4; i++) {
            int y = bottomY - (chartH * i / 4);
            g2.drawLine(startX, y, startX + (vals.length-1)*step, y);
        }

        int[] xP = new int[vals.length];
        int[] yP = new int[vals.length];
        for (int i = 0; i < vals.length; i++) {
            xP[i] = startX + i * step;
            yP[i] = bottomY - (int)((vals[i]/(double)max)*chartH);
        }

        int[] fx = Arrays.copyOf(xP, vals.length+2);
        int[] fy = Arrays.copyOf(yP, vals.length+2);
        fx[vals.length]   = xP[vals.length-1]; fy[vals.length]   = bottomY;
        fx[vals.length+1] = xP[0];             fy[vals.length+1] = bottomY;
        g2.setColor(new Color(99, 102, 241, 28));
        g2.fillPolygon(fx, fy, vals.length+2);

        g2.setStroke(new BasicStroke(2.5f,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(AppColors.PRIMARY);
        for (int i = 0; i < vals.length-1; i++)
            g2.drawLine(xP[i], yP[i], xP[i+1], yP[i+1]);

        for (int i = 0; i < vals.length; i++) {
            g2.setColor(Color.WHITE);
            g2.fillOval(xP[i]-5, yP[i]-5, 10, 10);
            g2.setColor(AppColors.PRIMARY);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(xP[i]-5, yP[i]-5, 10, 10);
            g2.setColor(AppColors.TEXT_MUTED);
            g2.setFont(AppFonts.MICRO);
            g2.drawString(mths[i], xP[i]-8, bottomY+14);
        }
    }

    private JPanel buildPieChartCard() {
        JPanel card = buildCard();
        card.setLayout(new BorderLayout(0, 8));
        JLabel title = new JLabel("Stock by Category");
        title.setFont(AppFonts.CARD_TITLE);
        title.setForeground(AppColors.TEXT_PRIMARY);
        card.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(1, 2, 10, 0));
        content.setOpaque(false);

        JPanel pie = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawPieChart((Graphics2D) g);
            }
        };
        pie.setOpaque(false);

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
            JLabel dot = new JLabel("\u25CF");
            dot.setFont(AppFonts.BODY_BOLD);
            dot.setForeground(Color.decode(c[2]));
            JLabel lbl = new JLabel(c[0] + "  " + c[1]);
            lbl.setFont(AppFonts.SMALL);
            lbl.setForeground(AppColors.TEXT_SECONDARY);
            r.add(dot); r.add(lbl);
            legend.add(r);
        }
        content.add(pie);
        content.add(legend);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private void drawPieChart(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int cx = 80, cy = 80, r = 68;
        int[] pcts = {38, 24, 16, 14, 8};
        Color[] cols = {AppColors.PRIMARY, AppColors.INFO,
                AppColors.SUCCESS, AppColors.WARNING, AppColors.DANGER};
        int angle = 0;
        for (int i = 0; i < pcts.length; i++) {
            int arc = (int)(pcts[i]/100.0*360);
            g2.setColor(cols[i]);
            g2.fillArc(cx-r, cy-r, 2*r, 2*r, angle, arc);
            angle += arc;
        }
        g2.setColor(AppColors.BG_CARD);
        g2.fillOval(cx-34, cy-34, 68, 68);
        g2.setColor(AppColors.TEXT_MUTED);
        g2.setFont(AppFonts.SMALL_BOLD);
        g2.drawString("100%", cx-16, cy+4);
    }

    // ── Quick Reports with REAL CSV Export ────────────────────────────────────
    private JPanel buildQuickReports() {
        JPanel card = buildCard();
        card.setLayout(new BorderLayout(0, 14));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel title = new JLabel("Quick Reports  \u2014  Click to Download CSV");
        title.setFont(AppFonts.CARD_TITLE);
        title.setForeground(AppColors.TEXT_PRIMARY);
        card.add(title, BorderLayout.NORTH);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        btns.setOpaque(false);

        // Each report: {icon, label, csv header, csv sample data}
        Object[][] reports = {
            {"\uD83D\uDCCA", "Inventory Summary",
                "Product ID,Name,Category,Quantity,Price,Reorder Level,Status",
                new String[]{
                    "1,Laptop 15\" Pro,Electronics,142,1299.99,20,IN STOCK",
                    "2,USB-C Hub 7-Port,Accessories,215,49.99,30,IN STOCK",
                    "3,Office Chair Ergo,Furniture,12,399.00,15,LOW STOCK",
                    "4,Mechanical Keyboard,Peripherals,87,129.00,25,IN STOCK",
                    "5,Monitor 27\" IPS,Electronics,63,459.99,10,IN STOCK",
                    "6,Wireless Mouse,Peripherals,134,59.99,40,IN STOCK",
                    "7,Printer Paper A4,Stationery,20,12.50,50,LOW STOCK",
                    "8,USB-C Cable 2m,Accessories,2,15.99,30,LOW STOCK",
                }},
            {"\uD83D\uDED2", "Order History",
                "Order ID,Product,Supplier,Quantity,Total,Date,Status",
                new String[]{
                    "1045,Laptop 15\" Pro,TechWorld Inc.,10,12999.90,28 Mar 2026,SHIPPED",
                    "1044,USB-C Hub 7-Port,AccessPro Ltd.,50,2499.50,27 Mar 2026,PROCESSING",
                    "1043,Office Chair Ergo,OfficeFurn Co.,5,1995.00,26 Mar 2026,PENDING",
                    "1042,Printer Paper A4,PaperMills Ltd.,100,1250.00,25 Mar 2026,DELIVERED",
                    "1041,Wireless Mouse,PeriphWorld.,25,1499.75,24 Mar 2026,DELIVERED",
                }},
            {"\uD83C\uDFED", "Supplier Report",
                "Supplier ID,Name,Contact,Email,Phone,Category,Lead Days,Status",
                new String[]{
                    "1,TechWorld Inc.,John Smith,john@techworld.com,555-1001,Electronics,5,ACTIVE",
                    "2,AccessPro Ltd.,Emma Johnson,emma@accesspro.com,555-1002,Accessories,3,ACTIVE",
                    "3,OfficeFurn Co.,Bob Williams,bob@officefurn.com,555-1003,Furniture,7,ACTIVE",
                    "4,PaperMills Ltd.,Alice Brown,alice@papermills.com,555-1004,Stationery,2,ACTIVE",
                }},
            {"\uD83D\uDCB0", "Revenue Report",
                "Month,Revenue,Orders,Avg Order Value",
                new String[]{
                    "July 2025,52000,88,590.90",
                    "August 2025,61000,103,592.23",
                    "September 2025,58000,98,591.84",
                    "October 2025,73000,123,593.50",
                    "November 2025,69000,116,594.83",
                    "December 2025,78000,131,595.42",
                    "January 2026,84200,142,593.00",
                    "February 2026,79000,133,594.00",
                }},
            {"\u26A0\uFE0F",  "Low Stock Report",
                "Product ID,Name,Category,Current Stock,Reorder Level,Shortage",
                new String[]{
                    "3,Office Chair Ergo,Furniture,12,15,3",
                    "7,Printer Paper A4,Stationery,20,50,30",
                    "8,USB-C Cable 2m,Accessories,2,30,28",
                    "9,Standing Desk,Furniture,8,5,0",
                    "11,Headset Noise Cancel.,Electronics,3,10,7",
                }},
            {"\uD83D\uDE9A", "Shipment Report",
                "Tracking No,Order,Product,From,To,Carrier,Est. Arrival,Status",
                new String[]{
                    "TRK-001,#1045,Laptop 15\" Pro,TechWorld Inc.,Warehouse A,FedEx,1 Apr 2026,SHIPPED",
                    "TRK-002,#1044,USB-C Hub,AccessPro Ltd.,Warehouse B,UPS,2 Apr 2026,IN TRANSIT",
                    "TRK-003,#1043,Office Chair,OfficeFurn Co.,Warehouse A,DHL,3 Apr 2026,PENDING",
                    "TRK-004,#1042,Printer Paper,PaperMills Ltd.,Warehouse C,USPS,29 Mar 2026,DELIVERED",
                }},
        };

        for (Object[] rpt : reports) {
            String icon   = (String)   rpt[0];
            String label  = (String)   rpt[1];
            String header = (String)   rpt[2];
            String[] rows = (String[]) rpt[3];

            JButton btn = new JButton(icon + "  " + label) {
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getModel().isRollover()
                            ? AppColors.PRIMARY_LIGHT : AppColors.BG_CARD);
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                    g2.setColor(getModel().isRollover()
                            ? AppColors.PRIMARY : AppColors.BORDER);
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btn.setFont(AppFonts.BODY);
            btn.setForeground(AppColors.TEXT_PRIMARY);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(175, 42));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // EBT: ActionListener — opens Save dialog and writes real CSV
            btn.addActionListener(e -> exportCSV(label, header, rows));
            btns.add(btn);
        }

        card.add(btns, BorderLayout.CENTER);

        // Info label
        JLabel info = UIFactory.mutedLabel(
            "\uD83D\uDCA1  Click any report button to download it as a CSV file");
        card.add(info, BorderLayout.SOUTH);

        return card;
    }

    // ── Real CSV Export using JFileChooser ────────────────────────────────────
    private void exportCSV(String reportName, String header, String[] rows) {
        // JFC: JFileChooser for save dialog
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save " + reportName + " as CSV");

        // Default filename
        String defaultName = reportName.replace(" ", "_") + ".csv";
        chooser.setSelectedFile(new java.io.File(defaultName));

        // File filter
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "CSV Files (*.csv)", "csv"));

        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File file = chooser.getSelectedFile();

            // Ensure .csv extension
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new java.io.File(file.getAbsolutePath() + ".csv");
            }

            // Write CSV file
            try (FileWriter fw = new FileWriter(file)) {
                fw.write(header + "\n");
                for (String row : rows) {
                    fw.write(row + "\n");
                }
                // Success message
                JOptionPane.showMessageDialog(this,
                    "✅  " + reportName + " exported successfully!\n\n" +
                    "Saved to: " + file.getAbsolutePath(),
                    "Export Successful",
                    JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "❌  Failed to save file:\n" + ex.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ── Reusable white rounded card panel ─────────────────────────────────────
    private JPanel buildCard() {
        JPanel p = new JPanel() {
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