package com.inventory.controller;

import com.inventory.backend.InventoryBackendFacade;

public class ReportController {
    private final InventoryBackendFacade backend = InventoryBackendFacade.getInstance();

    public String getInventoryReport() {
        return backend.generateInventoryReport();
    }
}
