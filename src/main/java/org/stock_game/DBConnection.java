package org.stock_game;

import java.sql.*;

public class DBConnection {

    private Connection connection;
    private static DBConnection instance = null;
    private String database;
    private static final String DEFAULT_DATABASE = "stock_game";

    private DBConnection() {
        connection = null;
        this.database = DEFAULT_DATABASE;
        String jdbcConnectionString = "jdbc:postgresql://localhost:5432/" + database;
        String user = "postgres";
        String password = "root";
        try {
            connection = DriverManager.getConnection(jdbcConnectionString, user, password);
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection");
            e.printStackTrace();
        }
    }

    public static DBConnection getInstance(String database) {
        if (instance == null || !instance.database.equals(database)) {
            instance = new DBConnection();
            instance.database = database;
        }
        return instance;
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

    public void executeDMLQuery(String sqlQuery) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(sqlQuery);
    }
}
