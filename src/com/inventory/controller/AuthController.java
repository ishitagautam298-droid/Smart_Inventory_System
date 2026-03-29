package com.inventory.controller;

import com.inventory.backend.InventoryBackendFacade;
import com.inventory.model.User;

public class AuthController {
    private final InventoryBackendFacade backend = InventoryBackendFacade.getInstance();

    public boolean login(String username, String password) {
        User user = backend.login(username, password);
        return user != null;
    }
}
