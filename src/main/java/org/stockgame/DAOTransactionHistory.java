package org.stockgame;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DAOTransactionHistory {

    private final DBConnection dbConnection;

    DAOTransactionHistory() {
        dbConnection = DBConnection.getInstance();
    }

    DAOTransactionHistory(String database) {
        dbConnection = DBConnection.getInstance(database);
    }

    public TransactionHistory getTransactionHistory(String username) {
        TransactionHistory transactionHistory = null;
        String sqlQuery = "SELECT company_code, units, unit_price, type, date " +
                "FROM public.\"Transactions\" " +
                "WHERE username LIKE '" + username + "'";
        try {
            transactionHistory = new TransactionHistory();
            ResultSet result = dbConnection.executeSelectQuery(sqlQuery);
            while (result.next()) {
                Transaction transaction = createTransactionFromDBData(result);
                transactionHistory.addTransaction(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionHistory;
    }

    private Transaction createTransactionFromDBData(ResultSet result) throws SQLException {
        String companyCode = result.getString("company_code");
        int units = result.getInt("units");
        BigDecimal unitPrice = new BigDecimal(result.getString("unit_price"));
        TransactionType transactionType = getTransactionTypeFromString(result.getString("type"));
        LocalDate date = getDateFromDBDateString(result.getString("date"));
        return new Transaction(companyCode, units, unitPrice, transactionType, date);
    }

    private TransactionType getTransactionTypeFromString(String typeString) throws SQLException {
        return switch (typeString) {
            case "PURCHASE" -> TransactionType.PURCHASE;
            case "SALE" -> TransactionType.SALE;
            default -> throw new SQLException("Invalid transaction type in database.");
        };
    }

    private LocalDate getDateFromDBDateString(String dateString) {
        String[] yearMonthDaySplit = dateString.split("-");
        int year = Integer.parseInt(yearMonthDaySplit[0]);
        int month = Integer.parseInt(yearMonthDaySplit[1]);
        int day = Integer.parseInt(yearMonthDaySplit[2]);
        return LocalDate.of(year, month, day);
    }
}
