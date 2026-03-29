package com.inventory.service;

import java.util.List;

import com.inventory.backend.InventoryDataStore;
import com.inventory.model.Supplier;

public class SupplierService {
    private final InventoryDataStore dataStore = InventoryDataStore.getInstance();

    public Supplier addSupplier(Supplier supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier is required");
        }

        supplier.setName(requireText(supplier.getName(), "Supplier name"));
        supplier.setContact(requireText(supplier.getContact(), "Contact"));
        if (supplier.getAddress() != null) {
            supplier.setAddress(supplier.getAddress().trim());
        }

        return dataStore.saveSupplier(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return dataStore.getSuppliers();
    }

    private String requireText(String value, String fieldName) {
        String normalized = value == null ? "" : value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        return normalized;
    }
}
