package com.system.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/faculty_workload_db";

    public static Connection getConnection() throws SQLException {
        String url = getEnvironmentVariable("DB_URL", DEFAULT_URL);
        String user = getEnvironmentVariable("DB_USER", "root");
        String password = getEnvironmentVariable("DB_PASSWORD", "");
        return DriverManager.getConnection(url, user, password);
    }

    private static String getEnvironmentVariable(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null ? defaultValue : value;
    }
}