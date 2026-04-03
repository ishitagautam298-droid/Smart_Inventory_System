package com.inventory.ui.frames;

import com.inventory.backend.SupplierDAO;
import com.inventory.model.SupplierBean;
import com.inventory.ui.dialogs.AddSupplierDialog;
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

public class SupplierFrame extends JPanel {

    private JTable            table;
    private DefaultTableModel model;
    private JTextField        searchField;
    private JComboBox<String> statusFilter;

    private List<SupplierBean> allSuppliers       = new ArrayList<>();
    private List<SupplierBean> displayedSuppliers = new ArrayList<>();

    private final SupplierDAO supplierDAO = new SupplierDAO();

    private static final String[] COLS = {
        "#","Supplier Name","Contact Person","Email","Phone","Category","Lead","Status","Actions"
    };

    private static final int COL_ACTIONS = 8;

    public SupplierFrame() {
        setBackground(AppColors.BG_APP);
        setLayout(new BorderLayout());
        add(new HeaderPanel("Suppliers"), BorderLayout.NORTH);
        add(buildBody(),                  BorderLayout.CENTER);
        loadFromDB();
    }

    private void loadFromDB() {
        try {
            allSuppliers = supplierDAO.getAllSuppliers();
            if (allSuppliers.isEmpty()) loadSampleData();
        } catch (Exception e) { loadSampleData(); }
        applyFilters();
    }

    private void loadSampleData() {
        allSuppliers.clear();
        allSuppliers.add(new SupplierBean(1,"TechWorld Inc.",  "John Smith",   "john@techworld.com",  "555-1001","Electronics", 5,"ACTIVE"));
        allSuppliers.add(new SupplierBean(2,"AccessPro Ltd.",  "Emma Johnson", "emma@accesspro.com",  "555-1002","Accessories", 3,"ACTIVE"));
        allSuppliers.add(new SupplierBean(3,"OfficeFurn Co.",  "Bob Williams", "bob@officefurn.com",  "555-1003","Furniture",   7,"ACTIVE"));
        allSuppliers.add(new SupplierBean(4,"PaperMills Ltd.", "Alice Brown",  "alice@papermills.com","555-1004","Stationery",  2,"ACTIVE"));
        allSuppliers.add(new SupplierBean(5,"PeriphWorld.",    "Carlos Diaz",  "carlos@periph.com",   "555-1005","Peripherals", 4,"ACTIVE"));
        allSuppliers.add(new SupplierBean(6,"DisplayTech.",    "Sara Lee",     "sara@displaytech.com","555-1006","Electronics", 6,"ACTIVE"));
        allSuppliers.add(new SupplierBean(7,"KeyTech Ltd.",    "Mike Chen",    "mike@keytech.com",    "555-1007","Peripherals", 4,"INACTIVE"));
        allSuppliers.add(new SupplierBean(8,"DeskPro Inc.",    "Julia Davis",  "julia@deskpro.com",   "555-1008","Furniture",   8,"ACTIVE"));
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(0, 0));
        body.setBackground(AppColors.BG_APP);
        body.setBorder(UIFactory.pagePadding());
        body.add(buildTableCard(), BorderLayout.CENTER);
        return body;
    }

    private JPanel buildTableCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(AppColors.BG_CARD);
        card.setBorder(UIFactory.cardBorder());

        // Toolbar
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(AppColors.BG_CARD);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, AppColors.BORDER),
            BorderFactory.createEmptyBorder(12, 18, 12, 18)));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        JLabel title = new JLabel("All Suppliers");
        title.setFont(AppFonts.CARD_TITLE);
        title.setForeground(AppColors.TEXT_PRIMARY);

        searchField = new JTextField(18);
        searchField.setFont(AppFonts.INPUT);
        searchField.setPreferredSize(new Dimension(210, 34));
        searchField.setBorder(UIFactory.inputBorder());
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { applyFilters(); }
        });

        String[] statuses = {"All", "ACTIVE", "INACTIVE"};
        statusFilter = UIFactory.comboBox(statuses);
        statusFilter.setPreferredSize(new Dimension(120, 34));
        statusFilter.addActionListener(e -> applyFilters());

        left.add(title);
        left.add(Box.createHorizontalStrut(16));
        left.add(searchField);
        left.add(statusFilter);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        JButton addBtn = UIFactory.primaryButton("+ Add Supplier");
        addBtn.addActionListener(e -> openAddDialog());
        JButton refreshBtn = UIFactory.secondaryButton("\u21BB Refresh");
        refreshBtn.addActionListener(e -> loadFromDB());
        right.add(refreshBtn);
        right.add(addBtn);

        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        card.add(bar, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(COLS, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        UIFactory.styleTable(table);

        int[] widths = {40,145,115,165,95,100,65,80,120};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        DefaultTableCellRenderer alt = UIFactory.altRowRenderer();
        for (int i = 0; i < 7; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(alt);

        table.getColumnModel().getColumn(7).setCellRenderer(UIFactory.badgeRenderer());
        table.getColumnModel().getColumn(8).setCellRenderer(new SupplierActionsRenderer());

        // ── SINGLE MOUSE LISTENER ─────────────────────────────────────────────
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row < 0) return;

                if (col == COL_ACTIONS) {
                    Rectangle cellRect  = table.getCellRect(row, col, false);
                    int xInCell   = e.getX() - cellRect.x;
                    int cellWidth = cellRect.width;

                    // Left half = Edit, Right half = Delete
                    if (xInCell < cellWidth / 2) {
                        editSupplier(row);
                    } else {
                        deleteSupplier(row);
                    }
                }
            }
        });

        card.add(UIFactory.scrollPane(table), BorderLayout.CENTER);
        return card;
    }

    private void refreshTable(List<SupplierBean> list) {
        displayedSuppliers = new ArrayList<>(list);
        model.setRowCount(0);
        for (SupplierBean s : list) {
            model.addRow(new Object[]{
                "#" + s.getSupplierId(),
                s.getSupplierName(),
                s.getContactPerson(),
                s.getEmail(),
                s.getPhone(),
                s.getProductCategory(),
                s.getLeadTimeDays() + " days",
                s.getStatus(),
                "Edit | Del"
            });
        }
    }

    private void applyFilters() {
        String q   = searchField  != null ? searchField.getText().toLowerCase()     : "";
        String sel = statusFilter != null ? (String) statusFilter.getSelectedItem() : "All";
        List<SupplierBean> filtered = new ArrayList<>();
        for (SupplierBean s : allSuppliers) {
            boolean matchQ = s.getSupplierName().toLowerCase().contains(q)
                          || s.getProductCategory().toLowerCase().contains(q)
                          || s.getContactPerson().toLowerCase().contains(q);
            boolean matchS = "All".equals(sel) || s.getStatus().equals(sel);
            if (matchQ && matchS) filtered.add(s);
        }
        refreshTable(filtered);
    }

    private void openAddDialog() {
        AddSupplierDialog dlg = new AddSupplierDialog(SwingUtilities.getWindowAncestor(this));
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            SupplierBean s = dlg.getSupplier();
            boolean saved = supplierDAO.addSupplier(s);
            if (saved) {
                loadFromDB();
            } else {
                s.setSupplierId(allSuppliers.size() + 1);
                allSuppliers.add(s);
                applyFilters();
            }
        }
    }

    private void editSupplier(int row) {
        if (row < 0 || row >= displayedSuppliers.size()) return;
        SupplierBean s = displayedSuppliers.get(row);
        JOptionPane.showMessageDialog(this,
            "Supplier:  " + s.getSupplierName()
            + "\nContact:   " + s.getContactPerson()
            + "\nEmail:     " + s.getEmail()
            + "\nPhone:     " + s.getPhone()
            + "\nCategory:  " + s.getProductCategory()
            + "\nLead Time: " + s.getLeadTimeDays() + " days"
            + "\nStatus:    " + s.getStatus(),
            "Supplier Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteSupplier(int row) {
        if (row < 0 || row >= displayedSuppliers.size()) return;
        SupplierBean s = displayedSuppliers.get(row);
        ConfirmDialog dlg = new ConfirmDialog(
            SwingUtilities.getWindowAncestor(this),
            "Delete Supplier",
            "Delete \"" + s.getSupplierName() + "\"?");
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            supplierDAO.deleteSupplier(s.getSupplierId());
            allSuppliers.removeIf(x -> x.getSupplierId() == s.getSupplierId());
            applyFilters();
        }
    }

    static class SupplierActionsRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v,
                boolean sel, boolean foc, int row, int col) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 7));
            panel.setBackground(row % 2 == 0 ? Color.WHITE : AppColors.BG_ROW_ALT);

            JButton editBtn = new JButton("Edit");
            editBtn.setFont(AppFonts.SMALL_BOLD);
            editBtn.setForeground(AppColors.INFO_TEXT);
            editBtn.setBackground(AppColors.INFO_BG);
            editBtn.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
            editBtn.setFocusPainted(false);

            JButton delBtn = new JButton("Del");
            delBtn.setFont(AppFonts.SMALL_BOLD);
            delBtn.setForeground(AppColors.DANGER_TEXT);
            delBtn.setBackground(AppColors.DANGER_BG);
            delBtn.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
            delBtn.setFocusPainted(false);

            panel.add(editBtn);
            panel.add(delBtn);
            return panel;
        }
    }
}