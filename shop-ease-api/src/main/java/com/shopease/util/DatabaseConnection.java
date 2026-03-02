package com.shopease.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ MySQL JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() {

        try {
            String host = System.getenv("MYSQLHOST");
            String port = System.getenv("MYSQLPORT");
            String database = System.getenv("MYSQLDATABASE");
            String username = System.getenv("MYSQLUSER");
            String password = System.getenv("MYSQLPASSWORD");

            if (host == null || port == null || database == null ||
                    username == null || password == null) {
                throw new RuntimeException("MySQL environment variables missing");
            }

            String jdbcUrl = "jdbc:mysql://" + /*host*/ "mysql.railway.internal"+ ":" + /*port*/"3306" + "/" + /*database*/"railway"
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            System.out.println("FINAL JDBC URL: " + jdbcUrl);

            return DriverManager.getConnection(jdbcUrl, /*username*/"root", /*password*/"cUQFhyaDFxWaNNRTUnSHDjtLpqlsVAJz");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed", e);
        }
    }
}