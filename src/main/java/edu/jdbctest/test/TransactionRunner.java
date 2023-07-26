package edu.jdbctest.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
                PreparedStatement deleteFlightStatement = null;
                PreparedStatement deleteTicketsStatement = null;
        try {
            connection = ConnectionManager.open();
            
            deleteFlightStatement = connection.prepareStatement(deleteFlightSql);
            deleteTicketsStatement = connection.prepareStatement(deleteTicketsSql);

            connection.setAutoCommit(false);

            deleteFlightStatement.setLong(1, flightId);
            deleteTicketsStatement.setLong(1, flightId);

            int executeUpdate2 = deleteTicketsStatement.executeUpdate();

            if (true) {
                throw new RuntimeException("Oooops");
            }
            int executeUpdate = deleteFlightStatement.executeUpdate();

            System.out.println(executeUpdate);
            System.out.println(executeUpdate2);
            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) connection.close();
            if (deleteFlightStatement != null) deleteFlightStatement.close();
            if (deleteTicketsStatement != null) deleteTicketsStatement.close();
        }
    }
}
