package edu.jdbctest.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionRunner {
    public static void main(String[] args) throws SQLException {
        Long flightId = 8L;
        String deleteFlightSql = "DELETE FROM flight WHERE id = " + flightId;
                ;
        String deleteTicketsSql = "DELETE FROM ticket WERE flight_id = " + flightId;
                Connection connection = null;
                Statement statement = null;
        try {

            connection = ConnectionManager.open();
            connection.setAutoCommit(false);
            
            statement = connection.createStatement();
            statement.addBatch(deleteTicketsSql);
            statement.addBatch(deleteFlightSql);
            

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
