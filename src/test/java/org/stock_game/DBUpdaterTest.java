package org.stock_game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DBUpdaterTest {

    private DBUpdater dbUpdater;
    private final String TEST_DATABASE = "test_stock_game";
    private final String TEST_USER = "dbUpdater_test_user";

    @BeforeEach
    void setUp() {
        dbUpdater = new DBUpdater(TEST_USER, TEST_DATABASE);
    }

    @Test
    void updateTransactionHistory() throws SQLException {
        TransactionHistory history = new TransactionHistory();
        history.addTransaction(new Transaction("AAPL", 5, new BigDecimal("100.00"),
                TransactionType.PURCHASE, LocalDate.of(2023, 2, 15)));
        history.addTransaction(new Transaction("AAPL", 5, new BigDecimal("110.00"),
                TransactionType.SALE, LocalDate.of(2023, 2, 16)));
        dbUpdater.updateTransactionHistory(history);

        DAOTransactionHistory daoTransactionHistory = new DAOTransactionHistory(TEST_DATABASE);
        TransactionHistory historyInDB = daoTransactionHistory.getTransactionHistory(TEST_USER);
        cleanUpTransactionsTable();

        Transaction firstTransaction = historyInDB.getTransactionHistory().get(0);
        Transaction secondTransaction = historyInDB.getTransactionHistory().get(1);
        assertEquals(2, historyInDB.getAmountOfTransactions());

        assertEquals("AAPL", firstTransaction.companyCode());
        assertEquals(5, firstTransaction.units());
        assertEquals("100.00", firstTransaction.unitPrice().toString());
        assertEquals("PURCHASE", firstTransaction.type().toString());
        assertEquals(2023, firstTransaction.date().getYear());
        assertEquals(2, firstTransaction.date().getMonthValue());
        assertEquals(15, firstTransaction.date().getDayOfMonth());

        assertEquals("AAPL", secondTransaction.companyCode());
        assertEquals(5, secondTransaction.units());
        assertEquals("110.00", secondTransaction.unitPrice().toString());
        assertEquals("SALE", secondTransaction.type().toString());
        assertEquals(2023, secondTransaction.date().getYear());
        assertEquals(2, secondTransaction.date().getMonthValue());
        assertEquals(16, secondTransaction.date().getDayOfMonth());
    }

    @Test
    void updatePortfolio() throws SQLException {
        Portfolio portfolio = new Portfolio();
        portfolio.addStock("AAPL", 20);
        portfolio.addStock("TSLA", 50);
        portfolio.setBalance(new BigDecimal("5000.00"));
        dbUpdater.updatePortfolio(portfolio);

        DAOPortfolio daoPortfolio = new DAOPortfolio(TEST_DATABASE);
        Portfolio portfolioInDB = daoPortfolio.getPortfolio(TEST_USER);
        cleanUpPortfoliosAndBalancesTables();

        assertEquals(2, portfolioInDB.getAllStocks().size());
        assertEquals(20, portfolioInDB.getStockByCode("AAPL").units());
        assertEquals(50, portfolioInDB.getStockByCode("TSLA").units());
        assertEquals("5000.00", portfolioInDB.getBalance().toString());
    }

    @Test
    void registerValidAccount() throws SQLException {
        dbUpdater.registerAccount("password", "salt");
        DAOPassword daoPassword = new DAOPassword(TEST_DATABASE);
        String passwordInDB = daoPassword.getPassword(TEST_USER);
        String saltInDB = daoPassword.getPasswordSalt(TEST_USER);
        cleanUpUsersTable();

        assertEquals("password", passwordInDB);
        assertEquals("salt", saltInDB);
    }

    @Test
    void registerAlreadyExistingAccount() {
        String alreadyExistingUser = "test_user";
        assertThrows(
                SQLException.class,
                () -> (new DBUpdater(alreadyExistingUser, TEST_DATABASE))
                        .registerAccount("password", "salt")
        );
    }

    private void cleanUpTransactionsTable() throws SQLException {
        String sqlQuery = "DELETE FROM public.\"Transactions\" " +
                "WHERE username LIKE '" + TEST_USER + "'";
        DBConnection dbConnection = DBConnection.getInstance(TEST_DATABASE);
        dbConnection.executeDMLQuery(sqlQuery);
    }

    private void cleanUpPortfoliosAndBalancesTables() throws SQLException {
        String sqlQueryPortfolios = "DELETE FROM public.\"Portfolios\" " +
                "WHERE username LIKE '" + TEST_USER + "'";
        String sqlQueryBalances = "DELETE FROM public.\"Balances\" " +
                "WHERE username LIKE '" + TEST_USER + "'";
        DBConnection dbConnection = DBConnection.getInstance(TEST_DATABASE);
        dbConnection.executeDMLQuery(sqlQueryPortfolios);
        dbConnection.executeDMLQuery(sqlQueryBalances);
    }

    private void cleanUpUsersTable() throws SQLException {
        String sqlQuery = "DELETE FROM public.\"Users\" " +
                "WHERE username LIKE '" + TEST_USER + "'";
        DBConnection dbConnection = DBConnection.getInstance(TEST_DATABASE);
        dbConnection.executeDMLQuery(sqlQuery);
    }
}