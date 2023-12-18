package com.example;

import com.example.dao.TicketDao;
import com.example.dto.TicketFilter;
import com.example.entity.Ticket;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
//        saveTest();
//        deleteTest();
//        findAllTest();
//        findByIdTest();
//        updateTest();
//        findAllWithFilterTest();
        var ticketDao = TicketDao.getInstance();
        var ticket = ticketDao.findById(5L);
        ticket.ifPresent(System.out::println);
    }

    private static void findAllWithFilterTest() {
        var filter = new TicketFilter(3, 0, null, null, null, null, null);
        var ticketDao = TicketDao.getInstance();
        var tickets = ticketDao.findAll(filter);
        System.out.println(tickets);
    }

    private static void updateTest() {
        var ticketDao = TicketDao.getInstance();
        var maybeTicket = ticketDao.findById(2L);
        maybeTicket.ifPresent(ticket -> {
            ticket.setCost(BigDecimal.valueOf(188.99));
            ticketDao.update(ticket);
        });
    }

    private static void findByIdTest() {
        var ticketDao = TicketDao.getInstance();
        System.out.println(ticketDao.findById(2L));
    }


    private static void findAllTest() {
        var ticketDao = TicketDao.getInstance();
        ticketDao.findAll().forEach(System.out::println);
    }

    private static void deleteTest() {
        var ticketDao = TicketDao.getInstance();
        System.out.println(ticketDao.delete(1L));
    }

    private static void saveTest() {
        var ticket = new Ticket();
        ticket.setPassengerNo("1234765");
        ticket.setPassengerName("TEST2");
//        ticket.setFlightId(3L);
        ticket.setCost(BigDecimal.valueOf(105.00));
        ticket.setSeatNo("F7");

        var ticketDao = TicketDao.getInstance();
        System.out.println(ticketDao.save(ticket));
    }
}
