package edu.jdbctest.test;

import java.math.BigDecimal;

import edu.jdbctest.test.dao.TicketEntityDao;
import edu.jdbctest.test.entity.TicketEntity;

public class DaoRunner {

    public static void main(String[] args) {
        TicketEntityDao ticketDao = TicketEntityDao.getInstance();
        System.out.println(ticketDao.delete(57L));
    }

    private static void getSave() {
        TicketEntityDao ticketDao = TicketEntityDao.getInstance();

        TicketEntity ticketEntity = new TicketEntity();

        ticketEntity.setPassengerNo("12345");
        ticketEntity.setPassengerName("Bob");
        ticketEntity.setFlightId(3L);
        ticketEntity.setSeatNo("B3");
        ticketEntity.setCost(BigDecimal.TEN);
        TicketEntity save = ticketDao.save(ticketEntity);
        System.out.println(save.toString());
    }
}
