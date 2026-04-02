package com.inventory.controller;

import com.inventory.model.ProductBean;
import com.inventory.service.InventoryService;
import com.inventory.ui.dialogs.ConfirmDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// REMOVED: InventoryFrame import   — unused
// REMOVED: AddProductDialog import — unused
// REMOVED: private final InventoryFrame view = null — unused field

/**
 * EBT: ActionListener — handles all product-related button events.
 * OOPs: Single Responsibility — only handles product UI events.
 */
public class ProductController implements ActionListener {

    // OOPs: Composition
    private final InventoryService service = new InventoryService();

    public ProductController() {}

    // EBT: actionPerformed dispatches events by command string
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case "ADD":     handleAdd();     break;
            case "REFRESH": handleRefresh(); break;
            default: break;
        }
    }

    private void handleAdd()     { /* delegate to AddProductDialog */ }
    private void handleRefresh() { /* reload table from DB */ }

    public List<ProductBean> loadAllProducts() {
        return service.getAllProducts();
    }

    public List<ProductBean> searchProducts(String keyword) {
        return service.searchProducts(keyword);
    }

    public boolean addProduct(String name, String category,
                              double price, int qty, int reorderLevel) {
        ProductBean p = new ProductBean(0, name, category, qty, price, reorderLevel);
        boolean ok = service.addProduct(p);
        if (ok)
            JOptionPane.showMessageDialog(null,
                "Product added successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null,
                "Failed to add product.", "Error",
                JOptionPane.ERROR_MESSAGE);
        return ok;
    }

    public boolean deleteProduct(int productId, String productName) {
        ConfirmDialog dlg = new ConfirmDialog(null,
            "Delete Product",
            "Are you sure you want to delete \"" + productName + "\"?");
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            boolean ok = service.deleteProduct(productId);
            if (ok)
                JOptionPane.showMessageDialog(null,
                    "Product deleted.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            return ok;
        }
        return false;
    }

    public boolean adjustStock(int productId, int newQty) {
        return service.adjustStock(productId, newQty);
    }

    public int getLowStockCount() { return service.getLowStockCount(); }
    public int getTotalCount()    { return service.getTotalProductCount(); }
}

