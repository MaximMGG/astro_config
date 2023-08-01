package edu.jdbctest.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import edu.jdbctest.test.ConnectionManager;
import edu.jdbctest.test.entity.Flight;
import edu.jdbctest.test.exception.DaoException;

public class FlightEntityDao implements Dao<Long, Flight>{

    private static final FlightEntityDao INSTANCE = new FlightEntityDao();
    
    private FlightEntityDao() {}

    public static FlightEntityDao getInstance() {
        return INSTANCE;
    }

    private static final String FIND_BY_ID = """
           select
           id,
           flight_no,
           departure_date,
           departure_airport_code,
           arrival_date,
           arrival_airport_code,
           status,
           aircraft_id
           from flight
           where id = ?
            """;

    @Override
    public boolean delete(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Flight save(Flight ticket) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void update(Flight ticket) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
    public Optional<Flight> findById(Long id, Connection connection) {
        try (PreparedStatement prStatement = connection.prepareStatement(FIND_BY_ID)) {
            prStatement.setLong(1, id);
            ResultSet resultSet = prStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.ofNullable(new Flight(resultSet.getLong("id"),
                resultSet.getString("flight_no"),
                resultSet.getTimestamp("departure_date").toLocalDateTime(),
                resultSet.getString("departure_airport_code"),
                resultSet.getTimestamp("arrival_date").toLocalDateTime(),
                resultSet.getString("arrival_airport_code"),
                resultSet.getInt("aircraft_id"),
                resultSet.getString("status")
                ));
            }
            return Optional.ofNullable(new Flight(id, null, null, null, null, null, null, null));
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Flight> findById(Long id) {
        try (Connection connection = ConnectionManager.get()){
            return findById(id, connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Flight> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

}
