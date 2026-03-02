package com.shopease.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DatabaseUtil {

    private static final Logger LOGGER = Logger.getLogger(DatabaseUtil.class.getName());

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.info("MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL Driver not found.", e);
            throw new RuntimeException("MySQL Driver not found.", e);
        }
    }

    private DatabaseUtil() {
        // Prevent instantiation
    }

    public static Connection getConnection() {

        String railwayUrl = System.getenv("MYSQL_URL");

        if (railwayUrl == null || railwayUrl.trim().isEmpty()) {
            throw new RuntimeException(
                    "MYSQL_URL environment variable not found. " +
                            "Ensure Railway MySQL is properly configured."
            );
        }

        try {
            // Convert Railway URL to JDBC format
            String jdbcUrl = railwayUrl
                    .replace("mysql://", "jdbc:mysql://")
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            Connection connection = DriverManager.getConnection(jdbcUrl);

            LOGGER.info("Database connection established successfully.");
            return connection;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection failed.", e);
            throw new RuntimeException("Database connection failed.", e);
        }
    }
}