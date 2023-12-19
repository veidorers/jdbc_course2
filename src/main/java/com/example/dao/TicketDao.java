package com.example.dao;

import com.example.dto.TicketFilter;
import com.example.entity.Flight;
import com.example.entity.Ticket;
import com.example.exception.DaoException;
import com.example.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class TicketDao implements Dao<Long, Ticket> {
    private static final TicketDao INSTANCE = new TicketDao();
    private static final FlightDao flightDao = FlightDao.getInstance();
    private static final String DELETE_SQL = """
            DELETE FROM ticket
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO ticket (passenger_no, passenger_name, flight_id, seat_no, cost) 
            VALUES 
            (?, ?, ?, ?, ?)
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                passenger_no,
                passenger_name,
                flight_id,
                seat_no,
                cost
            FROM ticket
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE ticket
            SET passenger_no = ?,
                passenger_name= ?,
                flight_id = ?,
                seat_no = ?,
                cost = ?
            WHERE id = ?
            """;


    private TicketDao() {}

    public List<Ticket> findAll(TicketFilter filter) {
        List<Object> parameters = new ArrayList<>(); //отсюда мы будем вставлять данные в preparedStatement
        List<String> whereSql = new ArrayList<>(); //при помощи него мы сконструируем сам SQL запрос
        if (filter.passengerName() != null) {   //поля в filter могут быть null, проверяем их на это
            parameters.add(filter.passengerName()); //passengerName мы вставим в preparedStatement
            whereSql.add("passenger_name = ?");  //а это само условие внутри блока WHERE
        }
        if (filter.passengerNo() != null) {
            parameters.add(filter.passengerNo());
            whereSql.add("passenger_no = ?");
        }
        if (filter.flightId() != null) {
            parameters.add(filter.flightId());
            whereSql.add("flight_id = ?");
        }
        if (filter.seatNo() != null) {
            parameters.add(filter.seatNo());
            whereSql.add("seat_no = ?");
        }
        if (filter.cost() != null) {
            parameters.add(filter.cost());
            whereSql.add("cost = ?");
        }

        parameters.add(filter.limit());     //limit и offset обязательные по логике приложения и не могут быть null
        parameters.add(filter.offset());

        String where = null;
        //конструируем блок where в sql запросе
        //если whereSql пуст - значит условий не было. Тогда мы вставим только limit и offset
        if(!whereSql.isEmpty()) {
            where = whereSql.stream().collect(joining(" AND ", " WHERE ", " LIMIT ? OFFSET ? "));
        } else {
            where = " LIMIT ? OFFSET ? ";
        }

        //итоговый sql
        var sql = FIND_ALL_SQL + where;


        try (var connection = ConnectionManager.get();
            var prepareStatement = connection.prepareStatement(sql)) {

            //вставляем все поля фильтров
            for (int i = 0; i < parameters.size(); i++) {
                prepareStatement.setObject(i + 1, parameters.get(i));
            }

            System.out.println(prepareStatement);   //просто чтобы видеть получающийся запрос

            //получаем resultSet и засовываем его результаты в лист и возвращаем этот лист
            var resultSet = prepareStatement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (resultSet.next()) {
                tickets.add(buildTicket(resultSet));
            }
            return tickets;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<Ticket> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Ticket> result = new ArrayList<>();
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(buildTicket(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void update(Ticket ticket) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlight().getId());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());
            preparedStatement.setLong(6, ticket.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<Ticket> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Ticket ticket = null;
            if (resultSet.next()) {
                ticket = buildTicket(resultSet);
            }
            return Optional.ofNullable(ticket);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Ticket save(Ticket ticket) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlight().getId());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                ticket.setId(generatedKeys.getLong("id"));
            }
            return ticket;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Ticket buildTicket(ResultSet resultSet) throws SQLException {


        return new Ticket(
                resultSet.getLong("id"),
                resultSet.getString("passenger_no"),
                resultSet.getString("passenger_name"),
                flightDao.findById(resultSet.getLong("flight_id")).orElse(null),
                resultSet.getString("seat_no"),
                resultSet.getBigDecimal("cost")
        );
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }
}
