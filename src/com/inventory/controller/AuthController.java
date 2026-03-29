package com.inventory.controller;

public class AuthController {

    public boolean login(String username, String password) {

        // Dummy login logic
        if (username.equals("admin") && password.equals("admin")) {
            return true;
        }

        return false;
    }
}
