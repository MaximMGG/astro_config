package edu.jdbctest.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import edu.jdbctest.test.util.PropertiesUtil;


public class ConnectionManager {
    
    private static final String PASSWORD = "db.password";
    private static final String USERNAME = "db.username";
    private static final String URL_KEY = "db.url";

    static {
        loadDriver();
    }

    private ConnectionManager() {

    }

    private static void loadDriver() {

    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(
               PropertiesUtil.get(URL_KEY),
               PropertiesUtil.get(USERNAME),
               PropertiesUtil.get(PASSWORD));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}