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

    private DatabaseUtil() {}

    public static Connection getConnection() {

        try {
            String host = System.getenv("MYSQLHOST");
            String port = System.getenv("MYSQLPORT");
            String database = System.getenv("MYSQLDATABASE");
            String user = System.getenv("MYSQLUSER");
            String password = System.getenv("MYSQLPASSWORD");

            if (host == null || port == null || database == null ||
                    user == null || password == null) {

                throw new RuntimeException(
                        "One or more MySQL environment variables are missing."
                );
            }

            String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            LOGGER.info("Connecting to: " + jdbcUrl);

            Connection connection = DriverManager.getConnection(jdbcUrl, user, password);

            LOGGER.info("Database connection established successfully.");
            return connection;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection failed.", e);
            throw new RuntimeException("Database connection failed.", e);
        }
    }
}