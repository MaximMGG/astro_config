package edu.jdbctest.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.jdbctest.test.ConnectionManager;
import edu.jdbctest.test.entity.TicketEntity;
import edu.jdbctest.test.exception.DaoException;

public class TicketEntityDao {

    private static final TicketEntityDao INSTANCE = new TicketEntityDao();
    private static final String DELETE_SQL = """
                                DELETE FROM ticket
                                where id = ?
                                """;

    private static final String SAVE_SQL = """
            INSERT INTO ticket (passenger_no, passenger_name, flight_id, seat_no, cost)
            values (?, ?, ?, ?, ?);
        """;

    private TicketEntityDao() {}

    public static TicketEntityDao getInstance() {
        return INSTANCE; 
    }

    public boolean delete(Long id) {
        try (Connection connection = ConnectionManager.get();
            PreparedStatement prStatement = connection.prepareStatement(DELETE_SQL)) {
            prStatement.setLong(1, id);
            return prStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    public TicketEntity save(TicketEntity ticket) {

        try (Connection connection = ConnectionManager.get();
                PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlightId());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                ticket.setId(generatedKeys.getLong("id"));
            }
            return ticket;
        } catch (SQLException e) {
           throw new DaoException(e);
        }

    }
}
