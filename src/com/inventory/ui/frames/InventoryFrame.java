package com.inventory.ui.frames;

import com.inventory.backend.ProductDAO;
import com.inventory.model.ProductBean;
import com.inventory.ui.dialogs.AddProductDialog;
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
 * JFC: Inventory management page with JTable, JTextField, JComboBox.
 * JDBC: Loads products from ProductDAO.
 * EBT: KeyListener (search), ActionListener (buttons), MouseListener (row click).
 * OOPs: Inheritance (extends JPanel), Encapsulation of table state.
 * Runtime fix: displayedProducts tracks exactly what is shown in table.
 */
public class InventoryFrame extends JPanel {

    private JTable            table;
    private DefaultTableModel model;
    private JTextField        searchField;
    private JComboBox<String> categoryFilter;

    // allProducts = full master list from DB
    // displayedProducts = what is currently shown in table (FIXED)
    private List<ProductBean> allProducts       = new ArrayList<>();
    private List<ProductBean> displayedProducts = new ArrayList<>();

    private final ProductDAO productDAO = new ProductDAO();

    private static final String[] COLS = {
        "#", "Product Name", "Category", "In Stock", "Reserved",
        "Available", "Reorder", "Stock Bar", "Status", "Actions"
    };

    public InventoryFrame() {
        setBackground(AppColors.BG_APP);
        setLayout(new BorderLayout());
        add(new HeaderPanel("Inventory"), BorderLayout.NORTH);
        add(buildBody(),                  BorderLayout.CENTER);
        loadFromDB();
    }

    private void loadFromDB() {
        try {
            allProducts = productDAO.getAllProducts();
            if (allProducts.isEmpty()) loadSampleData();
        } catch (Exception e) {
            loadSampleData();
        }
        applyFilters(); // always go through filter so displayedProducts is set
    }

    private void loadSampleData() {
        allProducts.clear();
        allProducts.add(new ProductBean(1,  "Laptop 15\" Pro",      "Electronics", 142, 1299.99, 20));
        allProducts.add(new ProductBean(2,  "USB-C Hub 7-Port",      "Accessories", 215,   49.99, 30));
        allProducts.add(new ProductBean(3,  "Office Chair Ergo",     "Furniture",    12,  399.00, 15));
        allProducts.add(new ProductBean(4,  "Mechanical Keyboard",   "Peripherals",  87,  129.00, 25));
        allProducts.add(new ProductBean(5,  "Monitor 27\" IPS",      "Electronics",  63,  459.99, 10));
        allProducts.add(new ProductBean(6,  "Wireless Mouse",        "Peripherals", 134,   59.99, 40));
        allProducts.add(new ProductBean(7,  "Printer Paper A4",      "Stationery",   20,   12.50, 50));
        allProducts.add(new ProductBean(8,  "USB-C Cable 2m",        "Accessories",   2,   15.99, 30));
        allProducts.add(new ProductBean(9,  "Standing Desk",         "Furniture",     8,  649.00,  5));
        allProducts.add(new ProductBean(10, "Webcam 1080p",          "Electronics",  45,   99.99, 15));
        allProducts.add(new ProductBean(11, "Headset Noise Cancel.", "Electronics",   3,  249.99, 10));
        allProducts.add(new ProductBean(12, "Laptop Bag 15\"",       "Accessories",  56,   79.99, 20));
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(0, 16));
        body.setBackground(AppColors.BG_APP);
        body.setBorder(UIFactory.pagePadding());
        body.add(buildSummaryRow(), BorderLayout.NORTH);
        body.add(buildTableCard(),  BorderLayout.CENTER);
        return body;
    }
private JPanel buildSummaryRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 14, 0));
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 80));
        row.add(miniChip("Total SKUs",   "12",  AppColors.PRIMARY, AppColors.PRIMARY_LIGHT));
        row.add(miniChip("Total Units",  "791", AppColors.INFO,    AppColors.INFO_BG));
        row.add(miniChip("Low Stock",    "2",   AppColors.DANGER,  AppColors.DANGER_BG));
        row.add(miniChip("Near Reorder", "2",   AppColors.WARNING, AppColors.WARNING_BG));
        return row;
    }

    private JPanel miniChip(String label, String val, Color fg, Color bg) {
        JPanel p = new JPanel(new BorderLayout(10, 0)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppColors.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.setColor(AppColors.BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        JLabel dot = new JLabel("\u25CF");
        dot.setFont(new Font("Segoe UI", Font.BOLD, 26));
        dot.setForeground(fg);
        JPanel txt = new JPanel();
        txt.setOpaque(false);
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        JLabel num = new JLabel(val);
        num.setFont(new Font("Segoe UI", Font.BOLD, 22));
        num.setForeground(AppColors.TEXT_PRIMARY);
        JLabel lbl = new JLabel(label);
        lbl.setFont(AppFonts.SMALL);
        lbl.setForeground(AppColors.TEXT_MUTED);
        txt.add(num); txt.add(lbl);
        p.add(dot, BorderLayout.WEST);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildTableCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(AppColors.BG_CARD);
        card.setBorder(UIFactory.cardBorder());

        // Toolbar
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(AppColors.BG_CARD);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, AppColors.BORDER),
            BorderFactory.createEmptyBorder(12, 18, 12, 18)));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);

        JLabel title = new JLabel("Product Inventory");
        title.setFont(AppFonts.CARD_TITLE);
        title.setForeground(AppColors.TEXT_PRIMARY);

        searchField = new JTextField(18);
        searchField.setFont(AppFonts.INPUT);
        searchField.setPreferredSize(new Dimension(210, 34));
        searchField.setBorder(UIFactory.inputBorder());
        searchField.setToolTipText("Search products...");
        // EBT: KeyListener — live search
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { applyFilters(); }
        });

        String[] cats = {"All Categories","Electronics","Accessories","Furniture","Peripherals","Stationery"};
        categoryFilter = UIFactory.comboBox(cats);
        categoryFilter.setPreferredSize(new Dimension(155, 34));
        // EBT: ActionListener — category filter
        categoryFilter.addActionListener(e -> applyFilters());

        left.add(title);
        left.add(Box.createHorizontalStrut(16));
        left.add(searchField);
        left.add(categoryFilter);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
right.setOpaque(false);
        JButton addBtn = UIFactory.primaryButton("+ Add Product");
        addBtn.addActionListener(e -> openAddDialog());
        JButton refreshBtn = UIFactory.secondaryButton("\u21BB Refresh");
        refreshBtn.addActionListener(e -> loadFromDB());
        right.add(refreshBtn);
        right.add(addBtn);

        toolbar.add(left,  BorderLayout.WEST);
        toolbar.add(right, BorderLayout.EAST);
        card.add(toolbar,  BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(COLS, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        UIFactory.styleTable(table);

        int[] widths = {40, 170, 105, 65, 65, 75, 65, 140, 90, 90};
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Alternating row renderer for text columns
        DefaultTableCellRenderer alt = UIFactory.altRowRenderer();
        for (int i = 0; i < 7; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(alt);

        // Stock bar renderer (col 7) — uses ProductBean stored in model
        table.getColumnModel().getColumn(7).setCellRenderer((t, v, sel, foc, row, col) -> {
            if (!(v instanceof ProductBean)) return new JLabel();
            ProductBean p = (ProductBean) v;
            int qty = p.getQuantity(), reorder = p.getReorderLevel();
            int pct = (int) Math.min(100, (qty / (double) Math.max(reorder * 5, qty + 1)) * 100);
            Color barC = qty <= 0 ? AppColors.DANGER
                       : qty <= reorder ? AppColors.WARNING : AppColors.SUCCESS;
            JPanel panel = new JPanel(new BorderLayout(5, 0));
            panel.setBorder(BorderFactory.createEmptyBorder(11, 12, 11, 12));
            panel.setBackground(row % 2 == 0 ? Color.WHITE : AppColors.BG_ROW_ALT);
            JLabel qLbl = new JLabel(qty + " ");
            qLbl.setFont(AppFonts.SMALL_BOLD);
            qLbl.setForeground(barC);
            qLbl.setPreferredSize(new Dimension(36, 16));
            JProgressBar bar = new JProgressBar(0, 100);
            bar.setValue(pct);
            bar.setForeground(barC);
            bar.setBackground(AppColors.BORDER);
            bar.setBorderPainted(false);
            bar.setStringPainted(false);
            bar.setPreferredSize(new Dimension(0, 7));
            panel.add(qLbl, BorderLayout.WEST);
            panel.add(bar,  BorderLayout.CENTER);
            return panel;
        });

        // Status badge renderer (col 8)
        table.getColumnModel().getColumn(8).setCellRenderer(UIFactory.badgeRenderer());

        // Action buttons (col 9)
        table.getColumnModel().getColumn(9).setCellRenderer(new ActionsRenderer());
        table.getColumnModel().getColumn(9).setCellEditor(new ActionsEditor(this));

        // EBT: MouseListener — double-click for details
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) showProductDetails(row);
                }
            }
        });

        card.add(UIFactory.scrollPane(table), BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 8));
        footer.setBackground(AppColors.BG_ROW_ALT);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppColors.BORDER));
        footer.add(UIFactory.mutedLabel(
            "Showing " + allProducts.size() + " products  •  Double-click a row for details"));
        card.add(footer, BorderLayout.SOUTH);

        return card;
    }
// ── FIXED: refreshTable now also updates displayedProducts ───────────────
    private void refreshTable(List<ProductBean> list) {
        // CRITICAL FIX: keep displayedProducts in sync with what table shows
        displayedProducts = new ArrayList<>(list);
        model.setRowCount(0);
        for (ProductBean p : list) {
            int reserved  = (int)(p.getQuantity() * 0.08);
            int available = p.getQuantity() - reserved;
            model.addRow(new Object[]{
                "#" + p.getProductId(),
                p.getProductName(),
                p.getCategory(),
                p.getQuantity(),
                reserved,
                available,
                p.getReorderLevel(),
                p,                      // passed to bar renderer
                p.getStockStatus(),
                "ACTIONS"
            });
        }
    }

    // ── FIXED: applyFilters calls refreshTable which sets displayedProducts ──
    private void applyFilters() {
        String q   = searchField != null ? searchField.getText().toLowerCase() : "";
        String cat = categoryFilter != null
            ? (String) categoryFilter.getSelectedItem()
            : "All Categories";

        List<ProductBean> filtered = new ArrayList<>();
        for (ProductBean p : allProducts) {
            boolean matchQ = p.getProductName().toLowerCase().contains(q)
                          || p.getCategory().toLowerCase().contains(q);
            boolean matchC = "All Categories".equals(cat) || p.getCategory().equals(cat);
            if (matchQ && matchC) filtered.add(p);
        }
        refreshTable(filtered); // this sets displayedProducts
    }

    private void openAddDialog() {
        AddProductDialog dlg = new AddProductDialog(SwingUtilities.getWindowAncestor(this));
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            ProductBean p = dlg.getProduct();
            boolean saved = productDAO.addProduct(p);
            if (saved) {
                JOptionPane.showMessageDialog(this,
                    "Product added to database!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadFromDB();
            } else {
                p.setProductId(allProducts.size() + 1);
                allProducts.add(p);
                applyFilters();
            }
        }
    }

    // ── FIXED: uses displayedProducts.get(row) — always correct ─────────────
    void editProduct(int row) {
        if (row < 0 || row >= displayedProducts.size()) return;
        ProductBean existing = displayedProducts.get(row); // FIXED
        AddProductDialog dlg = new AddProductDialog(
            SwingUtilities.getWindowAncestor(this), existing);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            ProductBean updated = dlg.getProduct();
            updated.setProductId(existing.getProductId());
            productDAO.updateProduct(updated);
            // Update in master list
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getProductId() == existing.getProductId()) {
                    allProducts.set(i, updated);
                    break;
                }
            }
            applyFilters();
        }
    }

    // ── FIXED: uses displayedProducts.get(row) ───────────────────────────────
    void deleteProduct(int row) {
        if (row < 0 || row >= displayedProducts.size()) return;
        ProductBean p = displayedProducts.get(row); // FIXED
        ConfirmDialog dlg = new ConfirmDialog(
            SwingUtilities.getWindowAncestor(this),
            "Delete Product",
            "Delete \"" + p.getProductName() + "\"? This cannot be undone.");
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            productDAO.deleteProduct(p.getProductId());
            allProducts.removeIf(x -> x.getProductId() == p.getProductId());
            applyFilters();
        }
    }
private void showProductDetails(int row) {
        if (row < 0 || row >= displayedProducts.size()) return;
        ProductBean p = displayedProducts.get(row); // FIXED
        JOptionPane.showMessageDialog(this,
            "Product:     " + p.getProductName()
            + "\nCategory:    " + p.getCategory()
            + "\nIn Stock:    " + p.getQuantity()
            + "\nPrice:       $" + String.format("%.2f", p.getPrice())
            + "\nReorder Lvl: " + p.getReorderLevel()
            + "\nStatus:      " + p.getStockStatus(),
            "Product Details", JOptionPane.INFORMATION_MESSAGE);
    }

    // ── Inner Renderers & Editor ──────────────────────────────────────────────

    static class ActionsRenderer implements TableCellRenderer {
        JPanel p;
        ActionsRenderer() {
            p = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 7));
            JButton e = new JButton("Edit");
            e.setFont(AppFonts.SMALL_BOLD); e.setForeground(AppColors.INFO_TEXT);
            e.setBackground(AppColors.INFO_BG); e.setBorder(BorderFactory.createEmptyBorder(3,8,3,8));
            JButton d = new JButton("Del");
            d.setFont(AppFonts.SMALL_BOLD); d.setForeground(AppColors.DANGER_TEXT);
            d.setBackground(AppColors.DANGER_BG); d.setBorder(BorderFactory.createEmptyBorder(3,8,3,8));
            p.add(e); p.add(d);
        }
        public Component getTableCellRendererComponent(JTable t, Object v,
                boolean sel, boolean foc, int row, int col) {
            p.setBackground(row % 2 == 0 ? Color.WHITE : AppColors.BG_ROW_ALT);
            return p;
        }
    }

    static class ActionsEditor extends DefaultCellEditor {
        JPanel p; int curRow; InventoryFrame frame;
        ActionsEditor(InventoryFrame f) {
            super(new JCheckBox()); this.frame = f;
            p = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 7));
            JButton e = new JButton("Edit");
            e.setFont(AppFonts.SMALL_BOLD); e.setForeground(AppColors.INFO_TEXT);
            e.setBackground(AppColors.INFO_BG); e.setBorder(BorderFactory.createEmptyBorder(3,8,3,8));
            e.setFocusPainted(false);
            e.addActionListener(ev -> { fireEditingStopped(); frame.editProduct(curRow); });
            JButton d = new JButton("Del");
            d.setFont(AppFonts.SMALL_BOLD); d.setForeground(AppColors.DANGER_TEXT);
            d.setBackground(AppColors.DANGER_BG); d.setBorder(BorderFactory.createEmptyBorder(3,8,3,8));
            d.setFocusPainted(false);
            d.addActionListener(ev -> { fireEditingStopped(); frame.deleteProduct(curRow); });
            p.add(e); p.add(d);
        }
        public Component getTableCellEditorComponent(JTable t, Object v,
                boolean sel, int row, int col) {
            curRow = row; return p;
        }
        public Object getCellEditorValue() { return "ACTIONS"; }
    }
}
