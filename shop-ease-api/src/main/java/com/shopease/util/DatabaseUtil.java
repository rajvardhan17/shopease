package com.shopease.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseUtil {

    private static final Logger LOGGER = Logger.getLogger(DatabaseUtil.class.getName());

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL Driver not found", e);
        }
    }

    public static Connection getConnection() {
        try {
            String databaseUrl = System.getenv("DATABASE_URL");

            if (databaseUrl == null) {
                throw new RuntimeException("DATABASE_URL not found in Railway environment variables");
            }

            // Convert mysql:// to jdbc:mysql://
            String jdbcUrl = databaseUrl.replace("mysql://", "jdbc:mysql://")
                    + "?useSSL=false&allowPublicKeyRetrieval=true";

            Connection conn = DriverManager.getConnection(jdbcUrl);

            LOGGER.info("Connected to Railway MySQL successfully!");
            return conn;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection failed", e);
            return null;
        }
    }
}
