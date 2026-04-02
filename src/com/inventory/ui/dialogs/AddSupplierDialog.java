package com.inventory.ui.dialogs;

import com.inventory.model.SupplierBean;
import com.inventory.ui.theme.AppColors;
import com.inventory.ui.theme.AppFonts;
import com.inventory.ui.theme.UIFactory;

import javax.swing.*;
import java.awt.*;

/**
 * JFC: JDialog for adding a supplier.
 * EBT: ActionListener with full validation.
 * Beans: Builds and returns a SupplierBean.
 */
public class AddSupplierDialog extends JDialog {

    private boolean confirmed = false;
    private JTextField nameField, contactField, emailField, phoneField, leadTimeField;
    private JComboBox<String> categoryBox;

    public AddSupplierDialog(Window parent) {
        super(parent, "Add New Supplier", ModalityType.APPLICATION_MODAL);
        setSize(460, 520);
        setResizable(false);
        setLocationRelativeTo(parent);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppColors.BG_CARD);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(16, 185, 129));
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel title = new JLabel("\uD83C\uDFED  Add New Supplier");
        title.setFont(AppFonts.CARD_TITLE);
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setBackground(AppColors.BG_CARD);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 10, 24));

        nameField     = addField(form, "Supplier Name *",     "e.g. TechWorld Inc.");
        contactField  = addField(form, "Contact Person *",    "e.g. John Smith");
        emailField    = addField(form, "Email Address *",     "e.g. john@company.com");
        phoneField    = addField(form, "Phone Number *",      "e.g. 555-1234");
        categoryBox   = addCombo(form, "Product Category *",
            new String[]{"Electronics","Accessories","Furniture","Peripherals","Stationery"});
        leadTimeField = addField(form, "Lead Time (days) *",  "e.g. 5");

        root.add(form, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setBackground(AppColors.BG_APP);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppColors.BORDER));

        JButton cancelBtn = UIFactory.outlineButton("Cancel", AppColors.TEXT_SECONDARY);
        JButton saveBtn   = UIFactory.successButton("Save Supplier");

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

    private JTextField addField(JPanel f, String lbl, String ph) {
        JLabel l = UIFactory.formLabel(lbl); l.setAlignmentX(LEFT_ALIGNMENT);
        JTextField tf = UIFactory.textField(ph);
        tf.setAlignmentX(LEFT_ALIGNMENT);
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.add(l); f.add(Box.createVerticalStrut(4)); f.add(tf); f.add(Box.createVerticalStrut(12));
        return tf;
    }

    private JComboBox<String> addCombo(JPanel f, String lbl, String[] items) {
        JLabel l = UIFactory.formLabel(lbl); l.setAlignmentX(LEFT_ALIGNMENT);
        JComboBox<String> cb = UIFactory.comboBox(items);
        cb.setAlignmentX(LEFT_ALIGNMENT);
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.add(l); f.add(Box.createVerticalStrut(4)); f.add(cb); f.add(Box.createVerticalStrut(12));
        return cb;
    }
private boolean validateForm() {
        if (isEmpty(nameField))    { error("Supplier name is required.");    return false; }
        if (isEmpty(contactField)) { error("Contact person is required.");   return false; }
        String email = emailField.getText().trim();
        if (email.isEmpty() || !email.contains("@")) { error("Enter a valid email."); return false; }
        if (isEmpty(phoneField))   { error("Phone number is required.");     return false; }
        try { Integer.parseInt(leadTimeField.getText().trim()); }
        catch (NumberFormatException e) { error("Enter a valid lead time (days)."); return false; }
        return true;
    }

    private boolean isEmpty(JTextField tf) {
        String t = tf.getText().trim();
        return t.isEmpty() || t.startsWith("e.g");
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    public boolean isConfirmed() { return confirmed; }
    public SupplierBean getSupplier() {
        SupplierBean s = new SupplierBean();
        s.setSupplierName(nameField.getText().trim());
        s.setContactPerson(contactField.getText().trim());
        s.setEmail(emailField.getText().trim());
        s.setPhone(phoneField.getText().trim());
        s.setProductCategory((String) categoryBox.getSelectedItem());
        s.setLeadTimeDays(Integer.parseInt(leadTimeField.getText().trim()));
        s.setStatus("ACTIVE");
        return s;
    }
}
