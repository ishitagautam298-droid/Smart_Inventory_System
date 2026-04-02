package com.inventory.ui.frames;

import com.inventory.backend.ShipmentDAO;
import com.inventory.model.ShipmentBean;
import com.inventory.ui.panels.HeaderPanel;
import com.inventory.ui.theme.AppColors;
import com.inventory.ui.theme.AppFonts;
import com.inventory.ui.theme.UIFactory;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * JFC: Shipment tracking page with status overview chips.
 * JDBC: ShipmentDAO integration.
 */
public class ShipmentFrame extends JPanel {

    private final ShipmentDAO shipmentDAO = new ShipmentDAO();
    private List<ShipmentBean> allShipments = new ArrayList<>();
    private DefaultTableModel model;

    private static final String[] COLS = {
        "Tracking #","Order","Product","From","To","Carrier","Est. Arrival","Status"
    };

    public ShipmentFrame() {
        setBackground(AppColors.BG_APP);
        setLayout(new BorderLayout());
        add(new HeaderPanel("Shipments"), BorderLayout.NORTH);
        add(buildBody(),                  BorderLayout.CENTER);
        loadFromDB();
    }

    private void loadFromDB() {
        try {
            allShipments = shipmentDAO.getAllShipments();
            if (allShipments.isEmpty()) loadSampleData();
        } catch (Exception e) { loadSampleData(); }
        refreshTable(allShipments);
    }

    private void loadSampleData() {
        allShipments.clear();
        allShipments.add(new ShipmentBean(1,"TRK-20260328-001",1045,"Laptop 15\" Pro",   "TechWorld Inc.","Warehouse A","FedEx","1 Apr 2026","SHIPPED"));
        allShipments.add(new ShipmentBean(2,"TRK-20260327-002",1044,"USB-C Hub 7-Port",  "AccessPro Ltd.","Warehouse B","UPS",  "2 Apr 2026","IN TRANSIT"));
        allShipments.add(new ShipmentBean(3,"TRK-20260326-003",1043,"Office Chair Ergo", "OfficeFurn Co.","Warehouse A","DHL",  "3 Apr 2026","PENDING"));
        allShipments.add(new ShipmentBean(4,"TRK-20260325-004",1042,"Printer Paper A4",  "PaperMills Ltd.","Warehouse C","USPS","29 Mar 2026","DELIVERED"));
        allShipments.add(new ShipmentBean(5,"TRK-20260324-005",1041,"Wireless Mouse",    "PeriphWorld.","Warehouse B","FedEx","28 Mar 2026","DELIVERED"));
        allShipments.add(new ShipmentBean(6,"TRK-20260323-006",1040,"Monitor 27\" IPS",  "DisplayTech.","Warehouse A","DHL",  "27 Mar 2026","DELIVERED"));
        allShipments.add(new ShipmentBean(7,"TRK-20260322-007",1038,"Standing Desk",     "DeskPro Inc.","Warehouse C","UPS",  "4 Apr 2026","IN TRANSIT"));
        allShipments.add(new ShipmentBean(8,"TRK-20260320-008",1037,"Webcam 1080p",      "VisionTech.","Warehouse A","USPS", "26 Mar 2026","DELIVERED"));
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(0, 14));
        body.setBackground(AppColors.BG_APP);
        body.setBorder(UIFactory.pagePadding());
        body.add(buildTopCards(),  BorderLayout.NORTH);
        body.add(buildTableCard(), BorderLayout.CENTER);
        return body;
    }

    private JPanel buildTopCards() {
        JPanel row = new JPanel(new GridLayout(1, 4, 14, 0));
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 82));

        row.add(shipCard("Pending",     "1", AppColors.ORANGE,  AppColors.ORANGE_BG,  "\uD83D\uDCEA"));
        row.add(shipCard("In Transit",  "2", AppColors.INFO,    AppColors.INFO_BG,    "\uD83D\uDE9A"));
        row.add(shipCard("Shipped",     "1", AppColors.PRIMARY, AppColors.PRIMARY_LIGHT,"\uD83D\uDCE6"));
        row.add(shipCard("Delivered",   "4", AppColors.SUCCESS, AppColors.SUCCESS_BG,  "\u2705"));
        return row;
    }

    private JPanel shipCard(String label, String val, Color fg, Color bg, String icon) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 12)) {
            protected void paintComponent(Graphics g) {
Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.BG_CARD); g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                g2.setColor(AppColors.BORDER);  g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                g2.dispose(); super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        JLabel ico = new JLabel(icon); ico.setFont(new Font("Segoe UI Emoji",Font.PLAIN,22));
        JPanel txt = new JPanel(); txt.setOpaque(false); txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        JLabel num = new JLabel(val); num.setFont(new Font("Segoe UI",Font.BOLD,22)); num.setForeground(AppColors.TEXT_PRIMARY);
        JLabel lbl = new JLabel(label); lbl.setFont(AppFonts.SMALL); lbl.setForeground(AppColors.TEXT_MUTED);
        txt.add(num); txt.add(lbl);
        p.add(ico); p.add(txt);
        return p;
    }

    private JPanel buildTableCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(AppColors.BG_CARD);
        card.setBorder(UIFactory.cardBorder());

        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(AppColors.BG_CARD);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,AppColors.BORDER),
            BorderFactory.createEmptyBorder(12,18,12,18)));
        JLabel title = new JLabel("Shipment Tracker");
        title.setFont(AppFonts.CARD_TITLE); title.setForeground(AppColors.TEXT_PRIMARY);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        JButton refreshBtn = UIFactory.secondaryButton("\u21BB Refresh");
        refreshBtn.addActionListener(e -> loadFromDB());
        right.add(UIFactory.primaryButton("+ New Shipment"));
        right.add(refreshBtn);

        bar.add(title, BorderLayout.WEST); bar.add(right, BorderLayout.EAST);
        card.add(bar, BorderLayout.NORTH);

        model = new DefaultTableModel(COLS, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        UIFactory.styleTable(table);

        int[] widths = {145,60,145,130,100,70,105,100};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        DefaultTableCellRenderer alt = UIFactory.altRowRenderer();
        for (int i = 0; i < 7; i++) table.getColumnModel().getColumn(i).setCellRenderer(alt);
        table.getColumnModel().getColumn(7).setCellRenderer(UIFactory.badgeRenderer());

        card.add(UIFactory.scrollPane(table), BorderLayout.CENTER);
        return card;
    }

    private void refreshTable(List<ShipmentBean> list) {
        model.setRowCount(0);
        for (ShipmentBean s : list) {
            model.addRow(new Object[]{
                s.getTrackingNumber(), "#" + s.getOrderId(), s.getProductName(),
                s.getFromLocation(), s.getToLocation(), s.getCarrier(),
                s.getEstimatedArrival(), s.getStatus()
            });
        }
    }
}
