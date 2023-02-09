package org.example;

import java.sql.ResultSet;

public class DAOUser {

    private static DAOUser instance;
    private final DBConnection dbConnection;

    DAOUser() {
        dbConnection = DBConnection.getInstance();
    }

    public DAOUser getInstance() {
        if (instance == null) {
            instance = new DAOUser();
        }
        return instance;
    }

    public User getUserByLogin(String login) {
        String sqlQuery = "SELECT login, hashed_password " +
                "FROM public.\"Users\" " +
                "WHERE login == \'" + login + "\'";
        ResultSet result = dbConnection.executeSelectQuery(sqlQuery);

        // transaction history
        return new User();
    }
}
