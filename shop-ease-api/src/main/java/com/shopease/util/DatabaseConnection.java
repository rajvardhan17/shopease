package com.shopease.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class DatabaseConnection {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    static {
        setupLogging();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.info("MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found.", e);
        }
    }

    private static void setupLogging() {
        try (InputStream configFile = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("logging.properties")) {

            if (configFile != null) {
                LogManager.getLogManager().readConfiguration(configFile);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading logging configuration.", e);
        }
    }

    public static Connection getConnection() {

        try {
            String host = System.getenv("MYSQLHOST");
            String port = System.getenv("MYSQLPORT");
            String database = System.getenv("MYSQLDATABASE");
            String user = System.getenv("MYSQLUSER");
            String password = System.getenv("MYSQLPASSWORD");

            if (host == null || port == null || database == null || user == null || password == null) {
                throw new RuntimeException("Railway MySQL environment variables not found!");
            }

            LOGGER.info("Connecting to Railway MySQL...");
            LOGGER.info("Host: " + host);
            LOGGER.info("Port: " + port);
            LOGGER.info("Database: " + database);

            // Use SSL mode REQUIRED for Railway
            String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database
                    + "?sslMode=REQUIRED";

            Connection connection = DriverManager.getConnection(jdbcUrl, user, password);

            LOGGER.info("Connected to Railway MySQL successfully!");
            return connection;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection failed", e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public static void close(Connection conn, java.sql.Statement stmt, java.sql.ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error while closing DB resources.", e);
        }
    }
}
