package com.example;

import com.example.util.ConnectionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        var resultTickets = findTicketsByFlightId(1L);
        System.out.println(resultTickets);
        ConnectionManager.closePool();
    }

    private static List<Long> findTicketsByFlightId(Long id) throws SQLException {
        String sql = """
                SELECT id 
                FROM ticket
                WHERE flight_id = ?
                """;
        List<Long> result = new ArrayList<>();
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);

            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                 result.add(resultSet.getLong("id"));
            }

            return result;
        }
    }
}
