package org.stockgame.dbaccess;

import java.sql.*;

/**
 * A singleton class, used to establish the connection with
 * a given database, using JDBC.
 */
public final class DBConnection {

    private Connection connection;
    private final String database;
    private static DBConnection instance = null;
    private static final String DEFAULT_DATABASE = "stock_game";

    private DBConnection(String database) {
        this.database = database;
        String jdbcConnectionString = "jdbc:postgresql://localhost:5432/" + database;
        String user = "postgres";
        String password = "root";
        connection = null;
        try {
            connection = DriverManager.getConnection(jdbcConnectionString, user, password);
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection");
            System.err.println(e.getMessage());
        }
    }

    public static DBConnection getInstance() {
        return getInstance(DEFAULT_DATABASE);
    }

    public static DBConnection getInstance(String database) {
        if (instance == null || !instance.database.equals(database)) {
            instance = new DBConnection(database);
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
