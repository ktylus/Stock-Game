package org.stock_game;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOPassword {

    private final DBConnection dbConnection;

    DAOPassword() {
        dbConnection = DBConnection.getInstance();
    }

    DAOPassword(String database) {
        dbConnection = DBConnection.getInstance(database);
    }

    public String getPassword(String username) {
        String password = null;
        String sqlQuery = "SELECT hashed_password " +
                "FROM public.\"Users\" " +
                "WHERE username LIKE '" + username + "'";
        try {
            ResultSet result = dbConnection.executeSelectQuery(sqlQuery);
            boolean foundPassword = result.next();
            if (foundPassword) {
                password = result.getString("hashed_password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return password;
    }

    public String getPasswordSalt(String username) {
        String salt = null;
        String sqlQuery = "SELECT salt " +
                "FROM public.\"Users\" " +
                "WHERE username LIKE '" + username + "'";
        try {
            ResultSet result = dbConnection.executeSelectQuery(sqlQuery);
            boolean foundPasswordSalt = result.next();
            if (foundPasswordSalt) {
                salt = result.getString("salt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salt;
    }
}
