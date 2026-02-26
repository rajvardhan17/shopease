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
            LOGGER.info("MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL Driver not found", e);
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }

    public static Connection getConnection() {
        String host = System.getenv("MYSQLHOST");
        String port = System.getenv("MYSQLPORT");
        String database = System.getenv("MYSQLDATABASE");
        String user = System.getenv("MYSQLUSER");
        String password = System.getenv("MYSQLPASSWORD");

        if (isEmpty(host) || isEmpty(port) || isEmpty(database)
                || isEmpty(user) || isEmpty(password)) {

            throw new RuntimeException(
                    "Missing Railway MySQL environment variables. " +
                            "Check MYSQLHOST, MYSQLPORT, MYSQLDATABASE, MYSQLUSER, MYSQLPASSWORD"
            );
        }

        String jdbcUrl = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                host, port, database
        );

        try {
            Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
            LOGGER.info("Connected to Railway MySQL successfully!");
            return conn;

        } catch (SQLException e) {
                e.printStackTrace();   // VERY IMPORTANT
                LOGGER.log(Level.SEVERE, "Database connection failed. URL: " + jdbcUrl, e);
                throw new RuntimeException("Database connection failed", e);
        }
    }

    private static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}