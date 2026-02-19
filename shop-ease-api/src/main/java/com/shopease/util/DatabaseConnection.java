package com.shopease.util;

import java.io.InputStream;
import java.sql.*;
import java.util.logging.*;

public class DatabaseConnection {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "shopease";
    private static final String DB_USER = "root";       // change as per your MySQL username
    private static final String DB_PASSWORD = "admin@2204";   // change as per your MySQL password

    static {
        setupLogging();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.info("MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found. Please add it to your classpath.", e);
        }
    }

    private static void setupLogging() {
        try (InputStream configFile = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("logging.properties")) {

            if (configFile != null) {
                LogManager.getLogManager().readConfiguration(configFile);
                LOGGER.info("Custom logging configuration loaded.");
            } else {
                LOGGER.warning("logging.properties not found. Using default logging configuration.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading logging configuration.", e);
        }
    }

    /** Get connection (auto-create DB if needed) */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Step 1: Connect without DB
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            stmt.close();
            connection.close();

            // Step 2: Connect to the actual database
            connection = DriverManager.getConnection(DB_URL + DB_NAME + "?useSSL=false&allowPublicKeyRetrieval=true", DB_USER, DB_PASSWORD);
            LOGGER.info("Connected to database: " + DB_NAME);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection error: " + e.getMessage(), e);
        }
        return connection;
    }

    /** Close DB resources safely */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error while closing DB resources.", e);
        }
    }
}
