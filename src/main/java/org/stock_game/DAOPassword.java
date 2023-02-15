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
        String password = null;
        String sqlQuery = "SELECT hashed_password " +
                "FROM public.\"Users\" " +
                "WHERE username LIKE '" + username + "'";
        try {
            ResultSet result = dbConnection.executeSelectQuery(sqlQuery);
            result.next();
            password = result.getString("hashed_password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return password;
    }
}
