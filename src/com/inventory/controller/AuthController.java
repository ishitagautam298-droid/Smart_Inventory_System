package com.inventory.controller;

public class AuthController {

    public boolean login(String username, String password) {
        try {
            username = username.trim();
            password = password.trim();

            if (username.isEmpty() || password.isEmpty()) {
                throw new Exception("Fields cannot be empty");
            }

            // Dummy login
            if (username.equals("admin") && password.equals("admin")) {
                return true;
            }

            return false;

        } catch (Exception e) {
            System.out.println("Login Error: " + e.getMessage());
            return false;
        }
    }
}
