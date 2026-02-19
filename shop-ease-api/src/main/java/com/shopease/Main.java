package com.shopease;

import com.shopease.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Database connection successful!");
            } else {
                System.out.println("❌ Failed to connect to database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
