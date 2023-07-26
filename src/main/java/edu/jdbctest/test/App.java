package edu.jdbctest.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws SQLException {
        // Long flightId = 2L;
        // List<Long> ticketsByFlightId = getTicketsByFlightId(flightId);
        // System.out.println(ticketsByFlightId);
        // System.out.println(getFlightsBeteen(LocalDate.of(2020, 10, 1).atStartOfDay(),
        // LocalDateTime.now()));
        checkMetaDate();
    }

    private static void checkMetaDate() throws SQLException {
        try (Connection connection = ConnectionManager.open()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet catalogs = metaData.getCatalogs();
            while (catalogs.next()) {
                String catalog = catalogs.getString(1);
                ResultSet schemas = metaData.getSchemas();
                while (schemas.next()) {
                    String schema = schemas.getString("TABLE_SCHEM");
                    ResultSet tables = metaData.getTables(catalog,schema, "%", new String[]{"TABLE"});
                    if (schema.equals("public")) {
                        while (tables.next()) {
                            System.out.println(tables.getString("TABLE_NAME"));
                        }
                    }
                }
            }

        }
    }

    private static List<Long> getFlightsBeteen(LocalDateTime start, LocalDateTime end) throws SQLException {
        List<Long> result = new ArrayList<>();
        String sql = """
                Select id
                from flight
                where departure_date between ? and ?
                    """;
        try (Connection connection = ConnectionManager.open();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(10);
            preparedStatement.setMaxRows(100);
            System.out.println(preparedStatement);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(start));
            System.out.println(preparedStatement);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(end));
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getLong("id"));

            }
        }
        return result;
    }

    private static List<Long> getTicketsByFlightId(Long flightId) throws SQLException {
        String sql = """
                Select id
                from ticket
                where flight_id = ?
                 """;
        List<Long> result = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, flightId);
            ResultSet reslutSet = preparedStatement.executeQuery();
            while (reslutSet.next()) {
                result.add(reslutSet.getObject("id", Long.class));
            }
        }
        return result;
    }
}
