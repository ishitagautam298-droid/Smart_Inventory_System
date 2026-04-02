package com.inventory.controller;

import com.inventory.model.SupplierBean;
import com.inventory.service.SupplierService;
import com.inventory.ui.dialogs.ConfirmDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * EBT: ActionListener — handles all supplier button events.
 */
public class SupplierController implements ActionListener {

    // OOPs: Composition
    private final SupplierService service = new SupplierService();

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case "ADD_SUPPLIER": break;
            case "REFRESH":      break;
            default: break;
        }
    }

    public List<SupplierBean> loadAllSuppliers() {
        return service.getAllSuppliers();
    }

    public boolean addSupplier(String name, String contact, String email,
                               String phone, String category, int leadTime) {
        SupplierBean s = new SupplierBean(0, name, contact, email, phone, category, leadTime, "ACTIVE");
        boolean ok = service.addSupplier(s);
        if (ok) JOptionPane.showMessageDialog(null, "Supplier added!", "Success", JOptionPane.INFORMATION_MESSAGE);
        else    JOptionPane.showMessageDialog(null, "Failed to add supplier.", "Error", JOptionPane.ERROR_MESSAGE);
        return ok;
    }

    public boolean deleteSupplier(int supplierId, String supplierName) {
        ConfirmDialog dlg = new ConfirmDialog(null, "Delete Supplier",
            "Delete supplier \"" + supplierName + "\"?");
        dlg.setVisible(true);
        if (dlg.isConfirmed()) return service.deleteSupplier(supplierId);
        return false;
    }
}

