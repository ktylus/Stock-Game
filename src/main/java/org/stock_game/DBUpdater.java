package org.stock_game;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class DBUpdater {

    private final DBConnection dbConnection;
    private String username;

    DBUpdater(String username) {
        dbConnection = DBConnection.getInstance();
        this.username = username;
    }

    public void updateTransactionHistory(TransactionHistory history) {
        try {
            deleteOldEntries("Transactions");
            for (Transaction transaction : history.getTransactionHistory()) {
                addSingleTransactionToHistory(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addSingleTransactionToHistory(Transaction transaction) throws SQLException {
        String companyCode = transaction.companyCode();
        int units = transaction.units();
        String unitPrice = transaction.unitPrice().toString();
        String type = transaction.type().toString();
        String date = transaction.date().toString();
        String sqlQuery = "INSERT INTO public.\"Transactions\" (username, company_code, units, unit_price, type, date) " +
                "VALUES ('" + username + "', '" + companyCode + "', " + units + ", '" + unitPrice + "', '" + type + "', '" + date + "')";
        dbConnection.executeDMLQuery(sqlQuery);
    }

    public void updatePortfolio(Portfolio portfolio) {
        try {
            deleteOldEntries("Portfolios");
            deleteOldEntries("Balances");
            for (StockInPortfolio stock : portfolio.getAllStocks()) {
                addSingleStockToPortfolio(stock);
            }
            updatePortfolioBalance(portfolio);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteOldEntries(String tableName) throws SQLException {
        String sqlQuery = "DELETE FROM public.\"" + tableName + "\"";
        dbConnection.executeDMLQuery(sqlQuery);
    }

    private void addSingleStockToPortfolio(StockInPortfolio stock) throws SQLException {
        String companyCode = stock.companyCode();
        int units = stock.units();
        String sqlQuery = "INSERT INTO public.\"Portfolios\" " +
                "VALUES ('" + username + "', '" + companyCode + "', " + units + ")";
        dbConnection.executeDMLQuery(sqlQuery);
    }

    private void updatePortfolioBalance(Portfolio portfolio) throws SQLException {
        String sqlQuery = "INSERT INTO public.\"Balances\" " +
                "VALUES ('" + username + "', '" + portfolio.getBalance() + "')";
        dbConnection.executeDMLQuery(sqlQuery);
    }

    public void registerAccount(String hashedPassword, String passwordSalt) throws SQLException {
        String sqlQuery = "INSERT INTO public.\"Users\" " +
                "VALUES ('" + username + "', '" + hashedPassword + "', '" + passwordSalt + "')";
        dbConnection.executeDMLQuery(sqlQuery);
    }
}