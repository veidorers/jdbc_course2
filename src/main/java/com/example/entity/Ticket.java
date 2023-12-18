package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {
    private Long id;
    private String passengerNo;
    private String passengerName;
    private Flight flight;
    private String seatNo;
    private BigDecimal cost;
}
