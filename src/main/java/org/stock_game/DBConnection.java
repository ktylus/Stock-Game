package org.stock_game;

import java.sql.*;

public class DBConnection {

    private Connection connection = null;
    private static DBConnection instance = null;

    private DBConnection() {
        String jdbcConnectionString = "jdbc:postgresql://localhost:5432/test_stock_game";
        String user = "postgres";
        String password = "root";
        try {
            connection = DriverManager.getConnection(jdbcConnectionString, user, password);
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection");
            e.printStackTrace();
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public ResultSet executeSelectQuery(String sqlQuery) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sqlQuery);
    }
}
