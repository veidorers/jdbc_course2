package com.example.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
    private static final String URL_KEY = "mysql.db.url";
    private static final String USERNAME_KEY = "mysql.db.username";
    private static final String PASSWORD_KEY = "mysql.db.password";

//    static {
//        loadDriver();
//    }
//
//    private static void loadDriver() {
//        Class.forName(Driver)
//    }

    private ConnectionManager() {}

    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
