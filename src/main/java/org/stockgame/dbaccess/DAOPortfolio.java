package org.stockgame.dbaccess;

import org.stockgame.portfolio.Portfolio;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO retrieving portfolio and its balance
 * from database, given username.
 */
public class DAOPortfolio {

    private final DBConnection dbConnection;

    public DAOPortfolio() {
        dbConnection = DBConnection.getInstance();
    }

    public DAOPortfolio(String database) {
        dbConnection = DBConnection.getInstance(database);
    }

    public Portfolio getPortfolio(String username) {
        Portfolio portfolio = null;
        String sqlQuery = "SELECT company_code, units " +
                "FROM public.\"Portfolios\" " +
                "WHERE username LIKE '" + username + "'";
        try (ResultSet result = dbConnection.executeSelectQuery(sqlQuery)) {
            portfolio = new Portfolio();
            portfolio.setBalance(getBalance(username));
            while (result.next()) {
                String companyCode = result.getString("company_code");
                int units = result.getInt("units");
                portfolio.addStock(companyCode, units);
            }
        } catch (SQLException e) {
            System.err.println("Couldn't retrieve portfolio data from database.");
            System.err.println(e.getMessage());
        }
        return portfolio;
    }

    private BigDecimal getBalance(String username) {
        BigDecimal balance = null;
        String sqlQuery = "SELECT balance " +
                "FROM public.\"Balances\" " +
                "WHERE username LIKE '" + username + "'";
        try (ResultSet result = dbConnection.executeSelectQuery(sqlQuery)) {
            boolean foundBalance = result.next();
            if (foundBalance) {
                balance = new BigDecimal(result.getString("balance"));
            }
        } catch (SQLException e) {
            System.err.println("Couldn't retrieve balance data from database.");
            System.err.println(e.getMessage());
        }
        return balance;
    }
}
