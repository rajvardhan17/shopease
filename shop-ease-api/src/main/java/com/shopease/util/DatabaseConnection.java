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

        // Example:
            // mysql://user:pass@host:port/database
            databaseUrl = databaseUrl.replace("mysql://", "");

            String[] parts = databaseUrl.split("@");

            String credentials = parts[0];
            String hostPart = parts[1];
    
            String user = credentials.split(":")[0];
            String password = credentials.split(":")[1];

            String host = hostPart.split("/")[0];
            String database = hostPart.split("/")[1];

            String jdbcUrl = "jdbc:mysql://" + host + "/" + database
                    + "?useSSL=true&requireSSL=true&serverTimezone=UTC";

            LOGGER.info("Final JDBC URL: " + jdbcUrl);

            return DriverManager.getConnection(jdbcUrl, user, password);

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
