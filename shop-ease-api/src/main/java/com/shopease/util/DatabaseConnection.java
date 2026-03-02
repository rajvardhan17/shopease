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

    private DatabaseConnection() {
        // Prevent instantiation
    }

    public static Connection getConnection() {

        String railwayUrl = System.getenv("MYSQL_URL");

        if (railwayUrl == null || railwayUrl.trim().isEmpty()) {
            throw new RuntimeException("MYSQL_URL environment variable not found.");
        }

        try {
            // Convert Railway URL to proper JDBC format
            String jdbcUrl = railwayUrl.replace("mysql://", "jdbc:mysql://");

            // Add required JDBC parameters
            if (!jdbcUrl.contains("?")) {
                jdbcUrl += "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            } else {
                jdbcUrl += "&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            }

            System.out.println("FINAL JDBC URL: " + jdbcUrl);

            return DriverManager.getConnection(jdbcUrl);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed", e);
        }
    }
}