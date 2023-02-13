package org.stock_game;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOPassword {

    private static DAOPassword instance;
    private final DBConnection dbConnection;

    private DAOPassword() {
        dbConnection = DBConnection.getInstance();
    }

    public static DAOPassword getInstance() {
        if (instance == null) {
            instance = new DAOPassword();
        }
        return instance;
    }

    public String getPassword(String username) {
        String sqlQuery = "SELECT hashed_password " +
                "FROM public.\"Users\" " +
                "WHERE username LIKE '" + username + "'";
        try {
            ResultSet result = dbConnection.executeSelectQuery(sqlQuery);
            while (result.next()) {
                String password = result.getString("hashed_password");
                return password;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
