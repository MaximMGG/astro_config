package edu.jdbctest.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionRunner {
    public static void main(String[] args) throws SQLException {
        Long flightId = 8L;
        String deleteFlightSql = """
            DELETE FROM flight WHERE id = ?
                """;
        String deleteTicketsSql = """
               DELETE FROM ticket WHERE flight_id = ?
                """;
                Connection connection = null;
                Statement statement = null;
        try {
            connection = ConnectionManager.open();
            statement = connection.createStatement();
            statement.addBatch(deleteTicketsSql);
        
            connection.setAutoCommit(false);


            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) connection.close();
        }
    }
}
