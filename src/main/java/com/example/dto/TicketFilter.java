package com.example.dto;

import java.math.BigDecimal;

public record TicketFilter(int limit,
                           int offset,
                           String passengerName,
                           String passengerNo,
                           Long flightId,
                           String seatNo,
                           BigDecimal cost) {
}
