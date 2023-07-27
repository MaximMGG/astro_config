package edu.jdbctest.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void getFlightsBeteenSuccess() {
        List<Long> result = new ArrayList<>();
        List<Long> testResult = null;
        result.add(9L);
        try {
            testResult = App.getFlightsBeteen(LocalDate.of(2020, 10, 1).atStartOfDay(), LocalDateTime.now());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(result.get(0), testResult.get(0));
        assertEquals(result.size(), testResult.size());
    }

}