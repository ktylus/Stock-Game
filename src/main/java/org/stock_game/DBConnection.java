package org.stock_game;

import java.sql.*;

public class DBConnection {

    private Connection connection = null;
    private static DBConnection instance = null;

    DBConnection() {
        String jdbcConnectionString = "\"jdbc:postgresql://localhost:5432/test_stock_game\"";
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

    public ResultSet executeSelectQuery(String sqlQuery) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test_stock_game", "postgres", "root");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.\"Users\"");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("login"));
                System.out.println(resultSet.getString("hashed_password"));
                System.out.println();
            }
            System.out.println("test_password".hashCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}