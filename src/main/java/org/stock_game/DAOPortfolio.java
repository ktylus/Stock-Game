package org.stock_game;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOPortfolio {

    private static DAOPortfolio instance;
    private final DBConnection dbConnection;

    private DAOPortfolio() {
        dbConnection = DBConnection.getInstance();
    }

    public static DAOPortfolio getInstance() {
        if (instance == null) {
            instance = new DAOPortfolio();
        }
        return instance;
    }

    public Portfolio getPortfolio(String username) {
        String sqlQuery = "SELECT company_code, units " +
                "FROM public.\"Portfolios\" " +
                "WHERE username LIKE '" + username + "'";
        try {
            ResultSet result = dbConnection.executeSelectQuery(sqlQuery);
            Portfolio portfolio = new Portfolio();
            portfolio.setBalance(getBalance(username));
            while (result.next()) {
                String companyCode = result.getString("company_code");
                int units = result.getInt("units");
                portfolio.addStock(companyCode, units);
            }
            return portfolio;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BigDecimal getBalance(String username) {
        String sqlQuery = "SELECT balance " +
                "FROM public.\"Balances\" " +
                "WHERE username LIKE '" + username + "'";
        try {
            ResultSet result = dbConnection.executeSelectQuery(sqlQuery);
            while (result.next()) {
                return new BigDecimal(result.getString("balance"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
