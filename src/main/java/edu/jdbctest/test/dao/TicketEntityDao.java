package edu.jdbctest.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.jdbctest.test.ConnectionManager;
import edu.jdbctest.test.dto.TicketFilter;
import edu.jdbctest.test.entity.Flight;
import edu.jdbctest.test.entity.TicketEntity;
import edu.jdbctest.test.exception.DaoException;

public class TicketEntityDao {

    private TicketEntityDao() {}

    public static TicketEntityDao getInstance() {
        return INSTANCE;
    }

    private static final TicketEntityDao INSTANCE = new TicketEntityDao();
    private static final String DELETE_SQL = """
                                DELETE FROM ticket
                                where id = ?
                                """;
    private static final String FIND_ALL_SQL = """
        select
        ticket.id,
        passenger_no,
        passenger_name,
        flight_id,
        seat_no,
        cost,
        fl.status,
        fl.aircraft_id,
        fl.arrival_airport_code,
        fl.arrival_date,
        fl.departure_airport_code,
        fl.departure_date,
        fl.flight_no
        from ticket
        JOIN flight fl ON ticket.flight_id = fl.id
        """;
    private static final String SAVE_SQL = """
            INSERT INTO ticket (passenger_no, passenger_name, flight_id, seat_no, cost)
            values (?, ?, ?, ?, ?);
        """;
    private static final String UPDATE_SQL = """
                update ticket
                set passenger_no = ?,
                passenger_name = ?,
                flight_id = ?,
                seat_no = ?,
                cost = ?
                where id = ?
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
        where ticket.id = ?
        """;
    
    public List<TicketEntity> findAll(TicketFilter filter) {
        List<Object> param = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();
        if (filter.seatNo() != null) {
            whereSql.add("seat_no LIKE ?");
            param.add("%" + filter.seatNo() + "%");
        }
        if (filter.passengerName() != null) {
            whereSql.add("passenger_name = ?");
            param.add(filter.passengerName());
        }
        param.add(filter.limit());
        param.add(filter.offset());

        String where = whereSql.stream().collect(Collectors.joining(" AND " , " WHERE ", " LIMIT ? OFFSET ? "));

        String sql = FIND_ALL_SQL + where;

        try (Connection connection = ConnectionManager.get();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            System.out.println(preparedStatement);
            for (int i = 0; i < param.size(); i++) {
                    preparedStatement.setObject(i + 1, param.get(i));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            List<TicketEntity> tickets = new ArrayList<>();
            while (resultSet.next()) {
                tickets.add(buildTicket(resultSet));
            }
            return tickets;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }


    
    public List<TicketEntity> findAll() {
        try (Connection connection = ConnectionManager.get();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<TicketEntity> list = new ArrayList<>();
            while (resultSet.next()) {
               list.add(buildTicket(resultSet));
            }
            return list;
        } catch (SQLException e) {
           throw new DaoException(e);
        }
    }

    private TicketEntity buildTicket(ResultSet executeQuery) throws SQLException {
        Flight flight = new Flight(
            executeQuery.getLong("flight_id"),
            executeQuery.getString("flight_no"),
            executeQuery.getTimestamp("departure_date").toLocalDateTime(),
            executeQuery.getString("departure_airport_code"),
            executeQuery.getTimestamp("arrival_date").toLocalDateTime(),
            executeQuery.getString("arrival_airport_code"),
            executeQuery.getInt("aircraft_id"),
            executeQuery.getString("status")
        );
                  return new TicketEntity(executeQuery.getLong("id"),
                            executeQuery.getString("passenger_no"),
                            executeQuery.getString("passenger_name"),
                            flight,
                            executeQuery.getString("seat_no"),
                            executeQuery.getBigDecimal("cost")
                            );
    }

    public Optional<TicketEntity> findById(Long id) {

        try (Connection connection = ConnectionManager.get();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
                preparedStatement.setLong(1, id);
                ResultSet executeQuery = preparedStatement.executeQuery();
                TicketEntity ticket = null;
                if (executeQuery.next()) {
                    ticket = buildTicket(executeQuery);
                }
                return Optional.ofNullable(ticket);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void update(TicketEntity ticket) {
        try (Connection connection = ConnectionManager.get();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlight().id());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());
            preparedStatement.setLong(6, ticket.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
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
            preparedStatement.setLong(3, ticket.getFlight().id());
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
