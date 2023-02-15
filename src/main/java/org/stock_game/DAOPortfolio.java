package org.stock_game;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOPortfolio {

    private final DBConnection dbConnection;

    DAOPortfolio() {
        dbConnection = DBConnection.getInstance();
    }

    DAOPortfolio(String database) {
        dbConnection = DBConnection.getInstance(database);
    }

    public Portfolio getPortfolio(String username) {
        Portfolio portfolio = null;
        String sqlQuery = "SELECT company_code, units " +
                "FROM public.\"Portfolios\" " +
                "WHERE username LIKE '" + username + "'";
        try {
            ResultSet result = dbConnection.executeSelectQuery(sqlQuery);
            portfolio = new Portfolio();
            portfolio.setBalance(getBalance(username));
            while (result.next()) {
                String companyCode = result.getString("company_code");
                int units = result.getInt("units");
                portfolio.addStock(companyCode, units);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return portfolio;
    }

    private BigDecimal getBalance(String username) {
        BigDecimal balance = null;
        String sqlQuery = "SELECT balance " +
                "FROM public.\"Balances\" " +
                "WHERE username LIKE '" + username + "'";
        try {
            ResultSet result = dbConnection.executeSelectQuery(sqlQuery);
            result.next();
            balance = new BigDecimal(result.getString("balance"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }
}
