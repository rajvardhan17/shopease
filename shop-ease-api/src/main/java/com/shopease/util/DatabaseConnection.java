package com.shopease.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ MySQL JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 🔥 Replace with your Aiven details
        String url = "jdbc:mysql://<AIVEN_HOST>:<PORT>/<DATABASE>?sslmode=require";
        String user = "<USERNAME>";
        String password = "<PASSWORD>";

        System.out.println("✅ Connecting to DB: " + url);
        return DriverManager.getConnection(url, user, password);

    } catch (Exception e) {
        throw new RuntimeException("❌ Database connection failed", e);
    }
}
}
