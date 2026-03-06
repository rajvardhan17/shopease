package com.shopease.model;

import java.io.Serializable;

public class User implements Serializable {

    private String userId;
    private String fullName;
    private String email;
    private String password;  // matches JSON key
    private String phone;
    private boolean isAdmin = false; // default false

    public User() {}

    public User(String userId, String fullName, String email, String password, String phone, boolean isAdmin) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.isAdmin = isAdmin;
    }

    // Getters & Setters

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }
}