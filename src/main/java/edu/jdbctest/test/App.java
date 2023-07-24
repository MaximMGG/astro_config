package edu.jdbctest.test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;
        String sql = """
           select *
           from ticket
                          """;
        try (Connection connection = ConnectionManager.open();
                Statement statement = connection.createStatement()) {
            System.out.println(connection.getSchema());
            System.out.println(connection.getTransactionIsolation());
            ResultSet executeResult = statement.executeQuery(sql);
            System.out.println(executeResult);
            while(executeResult.next()) {
                System.out.println(executeResult.getLong("id"));
                System.out.println(executeResult.getString("passenger_no"));
                System.out.println(executeResult.getString("passenger_name"));
                System.out.println(executeResult.getBigDecimal("cost"));
                System.out.println("--------");
            }
        }
    }
}
