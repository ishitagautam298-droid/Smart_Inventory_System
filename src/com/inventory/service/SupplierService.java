package com.inventory.service;

import com.inventory.backend.SupplierDAO;
import com.inventory.model.SupplierBean;
import java.util.List;

/**
 * OOPs: Abstraction — wraps SupplierDAO with business rules.
 */
public class SupplierService {

    // OOPs: Composition
    private final SupplierDAO supplierDAO = new SupplierDAO();

    public boolean addSupplier(SupplierBean s) {
        if (s.getSupplierName() == null || s.getSupplierName().trim().isEmpty()) return false;
        if (s.getEmail() == null || !s.getEmail().contains("@")) return false;
        s.setStatus("ACTIVE");
        return supplierDAO.addSupplier(s);
    }

    public List<SupplierBean> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

    public List<SupplierBean> getActiveSuppliers() {
        return supplierDAO.getActiveSuppliers();
    }

    public boolean updateSupplier(SupplierBean s) {
        return supplierDAO.updateSupplier(s);
    }

    public boolean deleteSupplier(int supplierId) {
        return supplierDAO.deleteSupplier(supplierId);
    }

    public SupplierBean getSupplierById(int id) {
        return supplierDAO.getSupplierById(id);
    }
}
