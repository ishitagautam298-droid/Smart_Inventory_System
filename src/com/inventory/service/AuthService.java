package com.inventory.service;

import com.inventory.backend.InventoryDataStore;
import com.inventory.model.User;

public class AuthService {
    private final InventoryDataStore dataStore = InventoryDataStore.getInstance();

    public User login(String username, String password) {
        String normalizedUsername = requireText(username, "Username");
        String normalizedPassword = requireText(password, "Password");

        User user = dataStore.findUser(normalizedUsername);
        if (user == null || !user.getPassword().equals(normalizedPassword)) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return user;
    }

    private String requireText(String value, String fieldName) {
        String normalized = value == null ? "" : value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        return normalized;
    }
}
