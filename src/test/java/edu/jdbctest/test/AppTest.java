package edu.jdbctest.test;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private JdbcRunner app;

    @Before
    public void initialize() {
        app = new JdbcRunner();
    }

    /**
     * @param parametr
     * parametr can't be 0
     * if 1 -> getFlightBetween
     * if 2 -> getTicketByFlight
     */
    public void getFlightsBeteenSuccess() {
        List<Long> result = new ArrayList<>();
        List<Long> testResult = null;
        result.add(9L);
        try {
            testResult = (List<Long>) app.testParam(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(result.get(0), testResult.get(0));
        assertEquals(result.size(), testResult.size());
    }
    @Test
    public void getTicketsByFlightIdSuccess() {
        List<Long> result = List.of(1L,1L,1L,1L,1L,1L );
        List<Long> testResult = null;
        try {
            testResult = (List<Long>) app.testParam(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(result.size(), testResult.size());
    }

}
