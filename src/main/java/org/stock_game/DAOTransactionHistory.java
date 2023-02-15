package org.stock_game;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DAOTransactionHistory {

    private static DAOTransactionHistory instance;
    private final DBConnection dbConnection;

    private DAOTransactionHistory() {
        dbConnection = DBConnection.getInstance();
    }

    public static DAOTransactionHistory getInstance() {
        if (instance == null) {
            instance = new DAOTransactionHistory();
        }
        return instance;
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
                String company_code = result.getString("company_code");
                int units = result.getInt("units");
                BigDecimal unitPrice = new BigDecimal(result.getString("unit_price"));
                TransactionType transactionType = getTransactionTypeFromString(result.getString("type"));
                LocalDate date = getDateFromDBDateString(result.getString("date"));
                Transaction transaction = new Transaction(company_code, units, unitPrice, transactionType, date);
                transactionHistory.addTransaction(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionHistory;
    }

    private TransactionType getTransactionTypeFromString(String typeString) {
        return switch (typeString) {
            case "PURCHASE" -> TransactionType.PURCHASE;
            case "SALE" -> TransactionType.SALE;
            default -> TransactionType.INVALID;
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
