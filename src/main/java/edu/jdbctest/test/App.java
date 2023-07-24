package edu.jdbctest.test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;
        String sql = """
                CREATE DATABASE game;
                 """;
        try (Connection connection = ConnectionManager.open();
                Statement statement = connection.createStatement()) {
            System.out.println(connection.getTransactionIsolation());
            boolean executeResult = statement.execute(sql);
            System.out.println(executeResult);
        }
    }
}
