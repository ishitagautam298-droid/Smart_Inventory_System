package com.inventory.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * OOPs: Singleton Pattern — only one DB connection instance.
 * JDBC: Manages Connection to MySQL database.
 */
public class DBConnection {

    private static Connection connection = null;

    private static final String URL      = "jdbc:mysql://localhost:3306/smart_inventory";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private DBConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("[DB] Connection established.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("[DB] Driver not found: " + e.getMessage());
            connection = null;
        } catch (SQLException e) {
            System.err.println("[DB] Connection failed: " + e.getMessage());
            connection = null;
        }
        return connection;
    }

    // ← NEW: Safe check before any DAO uses the connection
    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Error closing: " + e.getMessage());
        }
    }

    public static boolean testConnection() {
        getConnection();
        return isConnected();
    }
}
