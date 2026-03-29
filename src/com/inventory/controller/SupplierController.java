package com.inventory.controller;

import com.inventory.backend.InventoryBackendFacade;
import com.inventory.model.Supplier;

import java.util.List;

public class SupplierController {
    private final InventoryBackendFacade backend = InventoryBackendFacade.getInstance();

    public Supplier addSupplier(Supplier supplier) {
        return backend.addSupplier(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return backend.getAllSuppliers();
    }
}
