package com.inventory.controller;

import com.inventory.model.Supplier;

public class SupplierController {

    public void addSupplier(Supplier supplier) {
        System.out.println("Supplier Added: " + supplier.getName() + 
                           ", Contact: " + supplier.getContact());
    }
}
