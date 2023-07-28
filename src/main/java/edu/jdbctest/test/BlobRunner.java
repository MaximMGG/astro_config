package edu.jdbctest.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobRunner {


    public static void main(String[] args) throws SQLException, IOException {
        // blob - bytea(postgresql) 
        // clob - TEXT(postgresql)
        // saveImage();
        getImage();
    }

    private static void getImage() throws SQLException, IOException {
        String sql = """
            select image
            from aircraft 
            where id = ?
            """;
            try (Connection connection = ConnectionManager.get();
            PreparedStatement prStatement = connection.prepareStatement(sql)) {
                prStatement.setInt(1, 1);
                ResultSet executeQuery = prStatement.executeQuery();
                if (executeQuery.next()) {
                    byte[] bytes = executeQuery.getBytes("image");
                    Files.write(Path.of("c:/users/123/desktop/air.jpg"), bytes);
                }
             }
    }


    private static void saveImage() throws SQLException, IOException {
        String sql = """
                update aircraft
                set image = ?
                where id = 1
            """;
        try (Connection connection = ConnectionManager.get();
                PreparedStatement prStatement = connection.prepareStatement(sql)) {
            prStatement.setBytes(1, Files.readAllBytes(Path.of("resources", "boing777.jpg")));
            prStatement.executeUpdate();
        }
    }
}

