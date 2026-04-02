package com.inventory.backend;

import com.inventory.model.UserBean;
import java.sql.*;

/**
 * JDBC: Data Access Object for users table.
 */
public class UserDAO {

    private Connection conn() throws SQLException {
        Connection c = DBConnection.getConnection();
        if (c == null) throw new SQLException("No database connection available.");
        return c;
    }

    public UserBean authenticate(String username, String password) {
        try {
            PreparedStatement ps = conn().prepareStatement(
                "SELECT * FROM users WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new UserBean(
                    rs.getInt   ("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] authenticate: " + e.getMessage());
        }
        return null;
    }

    public boolean addUser(UserBean u) {
        try {
            PreparedStatement ps = conn().prepareStatement(
                "INSERT INTO users(username,password,full_name,role) VALUES(?,?,?,?)");
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getFullName());
            ps.setString(4, u.getRole());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserDAO] addUser: " + e.getMessage());
            return false;
        }
    }

    public boolean usernameExists(String username) {
        try {
            PreparedStatement ps = conn().prepareStatement(
                "SELECT 1 FROM users WHERE username=?");
            ps.setString(1, username);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("[UserDAO] usernameExists: " + e.getMessage());
            return false;
        }
    }
}
