package com.shopease.util;

import java.io.InputStream;
import java.sql.*;
import java.util.logging.*;

public class DatabaseConnection {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    // Use environment variables for cloud DB
    private static final String DB_HOST = System.getenv("DB_HOST");
    private static final String DB_PORT = System.getenv("DB_PORT");
    private static final String DB_NAME = System.getenv("DB_NAME");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    // JDBC URL
    private static final String JDBC_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?useSSL=false&allowPublicKeyRetrieval=true";

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

    /** Get connection to cloud DB */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
            LOGGER.info("Connected to cloud database: " + DB_NAME);
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
