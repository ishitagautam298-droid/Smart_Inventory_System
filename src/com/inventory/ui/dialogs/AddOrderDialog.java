package com.inventory.ui.dialogs;

import com.inventory.model.OrderBean;
import com.inventory.ui.theme.AppColors;
import com.inventory.ui.theme.AppFonts;
import com.inventory.ui.theme.UIFactory;

import javax.swing.*;
import java.awt.*;

/**
 * JFC: JDialog for placing a new order.
 * EBT: ActionListener on Place Order button.
 * Beans: Builds and returns an OrderBean.
 */
public class AddOrderDialog extends JDialog {

    private boolean confirmed = false;
    private JComboBox<String> productBox, supplierBox;
    private JTextField qtyField;
    // notesField REMOVED as field — declared locally below so no unused warning

    public AddOrderDialog(Window parent) {
        super(parent, "Place New Order", ModalityType.APPLICATION_MODAL);
        setSize(460, 400);
        setResizable(false);
        setLocationRelativeTo(parent);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppColors.BG_CARD);

        // Header bar
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(37, 99, 235));
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel title = new JLabel("\uD83D\uDED2  Place New Order");
        title.setFont(AppFonts.CARD_TITLE);
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);

        // Form body
        JPanel form = new JPanel();
        form.setBackground(AppColors.BG_CARD);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 10, 24));

        productBox  = addCombo(form, "Product *",
            new String[]{"Laptop 15\" Pro", "USB-C Hub",
                         "Office Chair", "Wireless Mouse", "Monitor 27\""});

        supplierBox = addCombo(form, "Supplier *",
            new String[]{"TechWorld Inc.", "AccessPro Ltd.",
                         "OfficeFurn Co.", "PeriphWorld.", "DisplayTech."});

        qtyField = addField(form, "Quantity *", "e.g. 50");

        // Notes field declared locally — used only in this form, never read back
        addField(form, "Notes (optional)", "Any special instructions...");

        root.add(form, BorderLayout.CENTER);

        // Footer buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setBackground(AppColors.BG_APP);
        footer.setBorder(BorderFactory.createMatteBorder(
            1, 0, 0, 0, AppColors.BORDER));

        JButton cancelBtn = UIFactory.outlineButton("Cancel",
            AppColors.TEXT_SECONDARY);
        JButton placeBtn  = UIFactory.primaryButton("Place Order");

        // EBT: ActionListener
        cancelBtn.addActionListener(e -> dispose());
        placeBtn.addActionListener(e -> {
            try {
                int qty = Integer.parseInt(qtyField.getText().trim());
                if (qty <= 0) throw new NumberFormatException();
                confirmed = true;
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Enter a valid quantity greater than 0.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        footer.add(cancelBtn);
        footer.add(placeBtn);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JTextField addField(JPanel f, String lbl, String ph) {
        JLabel l = UIFactory.formLabel(lbl);
        l.setAlignmentX(LEFT_ALIGNMENT);
        JTextField tf = UIFactory.textField(ph);
        tf.setAlignmentX(LEFT_ALIGNMENT);
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.add(l);
        f.add(Box.createVerticalStrut(4));
        f.add(tf);
        f.add(Box.createVerticalStrut(12));
        return tf;
    }
private JComboBox<String> addCombo(JPanel f, String lbl, String[] items) {
        JLabel l = UIFactory.formLabel(lbl);
        l.setAlignmentX(LEFT_ALIGNMENT);
        JComboBox<String> cb = UIFactory.comboBox(items);
        cb.setAlignmentX(LEFT_ALIGNMENT);
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.add(l);
        f.add(Box.createVerticalStrut(4));
        f.add(cb);
        f.add(Box.createVerticalStrut(12));
        return cb;
    }

    public boolean isConfirmed() { return confirmed; }

    // Returns a populated OrderBean — Beans usage
    public OrderBean getOrder() {
        OrderBean o = new OrderBean();
        o.setProductName((String) productBox.getSelectedItem());
        o.setSupplierName((String) supplierBox.getSelectedItem());
        o.setQuantity(Integer.parseInt(qtyField.getText().trim()));
        o.setStatus("PENDING");
        return o;
    }
}
