package com.inventory.ui.dialogs;

import com.inventory.model.ProductBean;
import com.inventory.ui.theme.AppColors;
import com.inventory.ui.theme.AppFonts;
import com.inventory.ui.theme.UIFactory;

import javax.swing.*;
import java.awt.*;

/**
 * JFC: JDialog for adding/editing a product.
 * EBT: ActionListener on Save button with validation.
 * Beans: Builds and returns a ProductBean.
 */
public class AddProductDialog extends JDialog {

    private boolean confirmed = false;
    private JTextField    nameField, priceField, qtyField, reorderField;
    private JComboBox<String> categoryBox;

    public AddProductDialog(Window parent) {
        super(parent, "Add New Product", ModalityType.APPLICATION_MODAL);
        setSize(460, 470);
        setResizable(false);
        setLocationRelativeTo(parent);
        buildUI();
    }

    // Constructor for edit mode
    public AddProductDialog(Window parent, ProductBean existing) {
        this(parent);
        // Pre-fill fields with existing data
        nameField.setText(existing.getProductName());
        nameField.setForeground(AppColors.TEXT_PRIMARY);
        priceField.setText(String.valueOf(existing.getPrice()));
        priceField.setForeground(AppColors.TEXT_PRIMARY);
        qtyField.setText(String.valueOf(existing.getQuantity()));
        qtyField.setForeground(AppColors.TEXT_PRIMARY);
        reorderField.setText(String.valueOf(existing.getReorderLevel()));
        reorderField.setForeground(AppColors.TEXT_PRIMARY);
        categoryBox.setSelectedItem(existing.getCategory());
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppColors.BG_CARD);

        // Header bar
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppColors.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel title = new JLabel("\uD83D\uDCE6  Add New Product");
        title.setFont(AppFonts.CARD_TITLE);
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel();
        form.setBackground(AppColors.BG_CARD);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 10, 24));

        nameField    = addField(form, "Product Name *",       "e.g. Laptop 15\" Pro");
        categoryBox  = addCombo(form, "Category *",
            new String[]{"Electronics","Accessories","Furniture","Peripherals","Stationery"});
        priceField   = addField(form, "Unit Price ($) *",     "e.g. 999.99");
        qtyField     = addField(form, "Initial Quantity *",   "e.g. 50");
        reorderField = addField(form, "Reorder Level *",      "e.g. 10");

        root.add(form, BorderLayout.CENTER);

        // Footer buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setBackground(AppColors.BG_APP);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppColors.BORDER));

        JButton cancelBtn = UIFactory.outlineButton("Cancel", AppColors.TEXT_SECONDARY);
        JButton saveBtn   = UIFactory.primaryButton("Save Product");

        // EBT: ActionListener
        cancelBtn.addActionListener(e -> dispose());
        saveBtn.addActionListener(e -> {
            if (validateForm()) {
                confirmed = true;
                dispose();
            }
        });

        footer.add(cancelBtn);
        footer.add(saveBtn);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JTextField addField(JPanel form, String labelText, String placeholder) {
        JLabel lbl = UIFactory.formLabel(labelText);
lbl.setAlignmentX(LEFT_ALIGNMENT);
        JTextField tf = UIFactory.textField(placeholder);
        tf.setAlignmentX(LEFT_ALIGNMENT);
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        form.add(lbl);
        form.add(Box.createVerticalStrut(4));
        form.add(tf);
        form.add(Box.createVerticalStrut(12));
        return tf;
    }

    private JComboBox<String> addCombo(JPanel form, String labelText, String[] items) {
        JLabel lbl = UIFactory.formLabel(labelText);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        JComboBox<String> cb = UIFactory.comboBox(items);
        cb.setAlignmentX(LEFT_ALIGNMENT);
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        form.add(lbl);
        form.add(Box.createVerticalStrut(4));
        form.add(cb);
        form.add(Box.createVerticalStrut(12));
        return cb;
    }

    private boolean validateForm() {
        String name  = nameField.getText().trim();
        String price = priceField.getText().trim();
        String qty   = qtyField.getText().trim();
        String reord = reorderField.getText().trim();

        if (name.isEmpty() || name.startsWith("e.g")) {
            error("Product name is required."); return false; }
        try { if (Double.parseDouble(price) < 0) throw new NumberFormatException(); }
        catch (NumberFormatException e) { error("Enter a valid positive price."); return false; }
        try { if (Integer.parseInt(qty) < 0) throw new NumberFormatException(); }
        catch (NumberFormatException e) { error("Enter a valid quantity."); return false; }
        try { if (Integer.parseInt(reord) < 0) throw new NumberFormatException(); }
        catch (NumberFormatException e) { error("Enter a valid reorder level."); return false; }
        return true;
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    // Returns a populated ProductBean (Beans usage)
    public ProductBean getProduct() {
        ProductBean p = new ProductBean();
        p.setProductName((String) nameField.getText().trim());
        p.setCategory((String) categoryBox.getSelectedItem());
        p.setPrice(Double.parseDouble(priceField.getText().trim()));
        p.setQuantity(Integer.parseInt(qtyField.getText().trim()));
        p.setReorderLevel(Integer.parseInt(reorderField.getText().trim()));
        return p;
    }

    public boolean isConfirmed() { return confirmed; }
}
