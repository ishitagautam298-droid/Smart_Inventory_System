package com.inventory.ui.frames;

import com.inventory.backend.OrderDAO;
import com.inventory.model.OrderBean;
import com.inventory.ui.dialogs.AddOrderDialog;
import com.inventory.ui.dialogs.ConfirmDialog;
import com.inventory.ui.panels.HeaderPanel;
import com.inventory.ui.theme.AppColors;
import com.inventory.ui.theme.AppFonts;
import com.inventory.ui.theme.UIFactory;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JFC: Order management page.
 * JDBC: Loads orders via OrderDAO.
 * EBT: ActionListener (buttons), KeyListener (search).
 * Runtime fix: displayedOrders tracks exactly what is shown in table.
 */
public class OrderFrame extends JPanel {

    private JTable            table;
    private DefaultTableModel model;
    private JTextField        searchField;
    private JComboBox<String> statusFilter;

    // FIXED: separate displayed list
    private List<OrderBean> allOrders       = new ArrayList<>();
    private List<OrderBean> displayedOrders = new ArrayList<>();

    private final OrderDAO orderDAO = new OrderDAO();

    private static final String[] COLS = {
        "Order ID","Product","Supplier","Qty","Total ($)","Date","Status","Actions"
    };

    public OrderFrame() {
        setBackground(AppColors.BG_APP);
        setLayout(new BorderLayout());
        add(new HeaderPanel("Orders"), BorderLayout.NORTH);
        add(buildBody(),               BorderLayout.CENTER);
        loadFromDB();
    }

    private void loadFromDB() {
        try {
            allOrders = orderDAO.getAllOrders();
            if (allOrders.isEmpty()) loadSampleData();
        } catch (Exception e) { loadSampleData(); }
        applyFilters();
    }

    private void loadSampleData() {
        allOrders.clear();
        allOrders.add(new OrderBean(1045,1,"Laptop 15\" Pro",    1,"TechWorld Inc.", 10,12999.90,"28 Mar 2026","SHIPPED"));
        allOrders.add(new OrderBean(1044,2,"USB-C Hub 7-Port",   2,"AccessPro Ltd.", 50, 2499.50,"27 Mar 2026","PROCESSING"));
        allOrders.add(new OrderBean(1043,3,"Office Chair Ergo",  3,"OfficeFurn Co.",  5, 1995.00,"26 Mar 2026","PENDING"));
        allOrders.add(new OrderBean(1042,7,"Printer Paper A4",   4,"PaperMills Ltd.",100,1250.00,"25 Mar 2026","DELIVERED"));
        allOrders.add(new OrderBean(1041,6,"Wireless Mouse",     5,"PeriphWorld.",   25, 1499.75,"24 Mar 2026","DELIVERED"));
        allOrders.add(new OrderBean(1040,5,"Monitor 27\" IPS",   6,"DisplayTech.",    8, 3679.92,"23 Mar 2026","DELIVERED"));
        allOrders.add(new OrderBean(1039,4,"Mechanical Keyboard",7,"KeyTech Ltd.",   15, 1935.00,"22 Mar 2026","CANCELLED"));
        allOrders.add(new OrderBean(1038,9,"Standing Desk",      8,"DeskPro Inc.",    3, 1947.00,"21 Mar 2026","SHIPPED"));
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(0, 14));
        body.setBackground(AppColors.BG_APP);
        body.setBorder(UIFactory.pagePadding());
        body.add(buildSummaryChips(), BorderLayout.NORTH);
        body.add(buildTableCard(),    BorderLayout.CENTER);
        return body;
    }

    private JPanel buildSummaryChips() {
        JPanel row = new JPanel(new GridLayout(1, 5, 12, 0));
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 76));
        row.add(chip("All Orders",  "38", AppColors.PRIMARY, AppColors.PRIMARY_LIGHT));
        row.add(chip("Pending",     "7",  AppColors.ORANGE,  AppColors.ORANGE_BG));
        row.add(chip("Processing",  "5",  AppColors.WARNING, AppColors.WARNING_BG));
        row.add(chip("In Transit",  "12", AppColors.INFO,    AppColors.INFO_BG));
        row.add(chip("Delivered",   "14", AppColors.SUCCESS, AppColors.SUCCESS_BG));
        return row;
    }
private JPanel chip(String label, String val, Color fg, Color bg) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                g2.setColor(fg);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        JLabel dot = new JLabel("\u25CF");
        dot.setFont(new Font("Segoe UI",Font.BOLD,18));
        dot.setForeground(fg);
        JPanel txt = new JPanel();
        txt.setOpaque(false);
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        JLabel num = new JLabel(val);
        num.setFont(new Font("Segoe UI",Font.BOLD,18));
        num.setForeground(AppColors.TEXT_PRIMARY);
        JLabel lbl = new JLabel(label);
        lbl.setFont(AppFonts.SMALL);
        lbl.setForeground(AppColors.TEXT_MUTED);
        txt.add(num); txt.add(lbl);
        p.add(dot); p.add(txt);
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

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        JLabel title = new JLabel("All Orders");
        title.setFont(AppFonts.CARD_TITLE);
        title.setForeground(AppColors.TEXT_PRIMARY);

        searchField = new JTextField(16);
        searchField.setFont(AppFonts.INPUT);
        searchField.setPreferredSize(new Dimension(200, 34));
        searchField.setBorder(UIFactory.inputBorder());
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { applyFilters(); }
        });

        String[] statuses = {"All Status","PENDING","PROCESSING","SHIPPED","DELIVERED","CANCELLED"};
        statusFilter = UIFactory.comboBox(statuses);
        statusFilter.setPreferredSize(new Dimension(140, 34));
        statusFilter.addActionListener(e -> applyFilters());

        left.add(title);
        left.add(Box.createHorizontalStrut(16));
        left.add(searchField);
        left.add(statusFilter);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        JButton addBtn = UIFactory.primaryButton("+ New Order");
        addBtn.addActionListener(e -> openAddDialog());
        JButton refreshBtn = UIFactory.secondaryButton("\u21BB Refresh");
        refreshBtn.addActionListener(e -> loadFromDB());
        right.add(refreshBtn);
        right.add(addBtn);

        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        card.add(bar, BorderLayout.NORTH);

        model = new DefaultTableModel(COLS, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        UIFactory.styleTable(table);

        int[] widths = {70,155,130,50,100,105,105,110};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        DefaultTableCellRenderer alt = UIFactory.altRowRenderer();
for (int i = 0; i < 6; i++) table.getColumnModel().getColumn(i).setCellRenderer(alt);
        table.getColumnModel().getColumn(6).setCellRenderer(UIFactory.badgeRenderer());
        table.getColumnModel().getColumn(7).setCellRenderer(new ActRenderer());
        table.getColumnModel().getColumn(7).setCellEditor(new ActEditor(this));

        card.add(UIFactory.scrollPane(table), BorderLayout.CENTER);
        return card;
    }

    // FIXED: also updates displayedOrders
    private void refreshTable(List<OrderBean> list) {
        displayedOrders = new ArrayList<>(list); // FIXED
        model.setRowCount(0);
        for (OrderBean o : list) {
            model.addRow(new Object[]{
                "#" + o.getOrderId(), o.getProductName(), o.getSupplierName(),
                o.getQuantity(), String.format("%.2f", o.getTotalAmount()),
                o.getOrderDate(), o.getStatus(), "ACTIONS"
            });
        }
    }

    private void applyFilters() {
        String q   = searchField  != null ? searchField.getText().toLowerCase() : "";
        String sel = statusFilter != null ? (String) statusFilter.getSelectedItem() : "All Status";

        List<OrderBean> filtered = new ArrayList<>();
        for (OrderBean o : allOrders) {
            boolean mQ = o.getProductName().toLowerCase().contains(q)
                      || o.getSupplierName().toLowerCase().contains(q);
            boolean mS = "All Status".equals(sel) || o.getStatus().equals(sel);
            if (mQ && mS) filtered.add(o);
        }
        refreshTable(filtered);
    }

    private void openAddDialog() {
        AddOrderDialog dlg = new AddOrderDialog(SwingUtilities.getWindowAncestor(this));
        dlg.setVisible(true);
        if (dlg.isConfirmed())
            JOptionPane.showMessageDialog(this,
                "Order placed! Connect DAO for persistence.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // FIXED: uses displayedOrders.get(row)
    void viewOrder(int row) {
        if (row < 0 || row >= displayedOrders.size()) return;
        OrderBean o = displayedOrders.get(row); // FIXED
        JOptionPane.showMessageDialog(this,
            "Order #"    + o.getOrderId()
            + "\nProduct:  " + o.getProductName()
            + "\nSupplier: " + o.getSupplierName()
            + "\nQty:      " + o.getQuantity()
            + "\nTotal:    $" + String.format("%.2f", o.getTotalAmount())
            + "\nDate:     "  + o.getOrderDate()
            + "\nStatus:   "  + o.getStatus(),
            "Order Details", JOptionPane.INFORMATION_MESSAGE);
    }

    // FIXED: uses displayedOrders.get(row)
    void cancelOrder(int row) {
        if (row < 0 || row >= displayedOrders.size()) return;
        OrderBean o = displayedOrders.get(row); // FIXED
        if (!o.isCancellable()) {
            JOptionPane.showMessageDialog(this,
                "Cannot cancel a " + o.getStatus() + " order.",
                "Not Allowed", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ConfirmDialog dlg = new ConfirmDialog(
            SwingUtilities.getWindowAncestor(this),
            "Cancel Order", "Cancel order #" + o.getOrderId() + "?");
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            orderDAO.updateOrderStatus(o.getOrderId(), "CANCELLED");
            // Update in master list
            for (OrderBean x : allOrders)
                if (x.getOrderId() == o.getOrderId()) { x.setStatus("CANCELLED"); break; }
            applyFilters();
        }
    }

    static class ActRenderer implements TableCellRenderer {
        JPanel p;
        ActRenderer() {
            p = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 7));
            JButton v = new JButton("View");
            v.setFont(AppFonts.SMALL_BOLD); v.setForeground(AppColors.INFO_TEXT);
v.setBackground(AppColors.INFO_BG); v.setBorder(BorderFactory.createEmptyBorder(3,8,3,8));
            JButton c = new JButton("Cancel");
            c.setFont(AppFonts.SMALL_BOLD); c.setForeground(AppColors.DANGER_TEXT);
            c.setBackground(AppColors.DANGER_BG); c.setBorder(BorderFactory.createEmptyBorder(3,8,3,8));
            p.add(v); p.add(c);
        }
        public Component getTableCellRendererComponent(JTable t, Object v,
                boolean sel, boolean foc, int row, int col) {
            p.setBackground(row % 2 == 0 ? Color.WHITE : AppColors.BG_ROW_ALT);
            return p;
        }
    }

    static class ActEditor extends DefaultCellEditor {
        JPanel p; int curRow; OrderFrame frame;
        ActEditor(OrderFrame f) {
            super(new JCheckBox()); this.frame = f;
            p = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 7));
            JButton v = new JButton("View");
            v.setFont(AppFonts.SMALL_BOLD); v.setForeground(AppColors.INFO_TEXT);
            v.setBackground(AppColors.INFO_BG); v.setBorder(BorderFactory.createEmptyBorder(3,8,3,8));
            v.setFocusPainted(false);
            v.addActionListener(e -> { fireEditingStopped(); frame.viewOrder(curRow); });
            JButton c = new JButton("Cancel");
            c.setFont(AppFonts.SMALL_BOLD); c.setForeground(AppColors.DANGER_TEXT);
            c.setBackground(AppColors.DANGER_BG); c.setBorder(BorderFactory.createEmptyBorder(3,8,3,8));
            c.setFocusPainted(false);
            c.addActionListener(e -> { fireEditingStopped(); frame.cancelOrder(curRow); });
            p.add(v); p.add(c);
        }
        public Component getTableCellEditorComponent(JTable t, Object v,
                boolean sel, int row, int col) { curRow = row; return p; }
        public Object getCellEditorValue() { return "ACTIONS"; }
    }
}
