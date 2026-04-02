package com.inventory.model;

import java.io.Serializable;

// OOPs: Encapsulation | JavaBean
public class UserBean implements Serializable {
    private static final long serialVersionUID = 4L;

    private int    userId;
    private String username;
    private String password;
    private String fullName;
    private String role; // ADMIN | MANAGER | STAFF

    public UserBean() {}

    public UserBean(int userId, String username, String password,
                    String fullName, String role) {
        this.userId   = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role     = role;
    }

    public boolean isAdmin()   { return "ADMIN".equals(role); }
    public boolean isManager() { return "MANAGER".equals(role); }

    // Getters & Setters
    public int    getUserId()           { return userId; }
    public void   setUserId(int v)      { this.userId = v; }
    public String getUsername()         { return username; }
    public void   setUsername(String v) { this.username = v; }
    public String getPassword()         { return password; }
    public void   setPassword(String v) { this.password = v; }
    public String getFullName()         { return fullName; }
    public void   setFullName(String v) { this.fullName = v; }
    public String getRole()             { return role; }
    public void   setRole(String v)     { this.role = v; }

    @Override
    public String toString() { return fullName + " (" + role + ")"; }
}
