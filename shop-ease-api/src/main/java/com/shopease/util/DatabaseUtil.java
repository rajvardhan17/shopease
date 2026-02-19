package com.shopease.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseUtil {
    private static final Logger LOGGER = Logger.getLogger(DatabaseUtil.class.getName());

    private static final String URL = "jdbc:mysql://localhost:3306/shopease";
    private static final String USER = "root"; // change if needed
    private static final String PASSWORD = "admin@2204"; // change if needed

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 8+ driver
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL Driver not found", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to create database connection", e);
            return null;
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing connection", e);
            }
        }
    }
}
