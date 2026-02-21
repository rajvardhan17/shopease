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
        String databaseUrl = System.getenv("MYSQL_URL");

        if (databaseUrl == null) {
            databaseUrl = System.getenv("DATABASE_URL");
        }

        if (databaseUrl == null) {
            throw new RuntimeException("No Railway database URL found.");
        }

        // Parse using URI (safe way)
        java.net.URI uri = new java.net.URI(databaseUrl);

        String userInfo = uri.getUserInfo(); // user:password
        String[] credentials = userInfo.split(":");

        String username = credentials[0];
        String password = credentials[1];

        String host = uri.getHost();
        int port = uri.getPort();
        String database = uri.getPath().replaceFirst("/", "");

        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database
                + "?useSSL=true&requireSSL=true&serverTimezone=UTC";

        LOGGER.info("Final JDBC URL: " + jdbcUrl);

        return DriverManager.getConnection(jdbcUrl, username, password);

    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Database connection failed", e);
        return null;
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
