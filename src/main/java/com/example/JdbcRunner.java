package com.example;

import com.example.util.ConnectionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException, InterruptedException, IOException {
//        saveImage();
        getImage(1);
    }

    private static void saveImage() throws SQLException, IOException {
        String sql = """
            UPDATE aircraft
            SET image = ?
            WHERE id = 1;
            """;
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setBytes(1, Files.readAllBytes(Path.of(
                    "src", "main", "resources", "boeing777.jpeg")));
            preparedStatement.executeUpdate();
        }
    }

    private static void getImage(Integer id) throws SQLException, IOException {
        String sql = """
            SELECT image 
            FROM aircraft
            WHERE id = ?
            """;
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                var image = resultSet.getBytes("image");
                Files.write(Path.of(
                                "src", "main", "resources", "new_boeing777.jpeg"),
                        image,
                        StandardOpenOption.CREATE);
            }
        }
    }
}
