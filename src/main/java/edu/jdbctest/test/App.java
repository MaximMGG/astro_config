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
                          insert into info(data)
                          values
                          ('autogenerated')
                          """;
        try (Connection connection = ConnectionManager.open();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            System.out.println(connection.getSchema());
            System.out.println(connection.getTransactionIsolation());

            int executeResult = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            System.out.println(executeResult);

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int serialKey = generatedKeys.getInt("id");
                System.out.println(serialKey);
            }
            
        }
    }
}
