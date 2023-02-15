package org.stock_game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DAOTransactionHistoryTest {

    private DAOTransactionHistory daoTransactionHistory;
    private final String TEST_DATABASE = "test_stock_game";
    private final String TEST_USER = "test_user";
    private final String NONEXISTENT_USER = "nonexistent_user";

    @BeforeEach
    void setUp() {
        daoTransactionHistory = new DAOTransactionHistory(TEST_DATABASE);
    }

    @Test
    void getTransactionHistoryOfExistingUser() {
        TransactionHistory history = daoTransactionHistory.getTransactionHistory(TEST_USER);
        Transaction firstTransaction = history.getTransactionHistory().get(0);
        Transaction secondTransaction = history.getTransactionHistory().get(1);
        assertEquals(2, history.getAmountOfTransactions());

        assertEquals("IBM", firstTransaction.companyCode());
        assertEquals(5, firstTransaction.units());
        assertEquals("100.00", firstTransaction.unitPrice().toString());
        assertEquals("PURCHASE", firstTransaction.type().toString());
        assertEquals(2023, firstTransaction.date().getYear());
        assertEquals(2, firstTransaction.date().getMonthValue());
        assertEquals(15, firstTransaction.date().getDayOfMonth());

        assertEquals("TSLA", secondTransaction.companyCode());
        assertEquals(10, secondTransaction.units());
        assertEquals("150.00", secondTransaction.unitPrice().toString());
        assertEquals("SALE", secondTransaction.type().toString());
        assertEquals(2023, secondTransaction.date().getYear());
        assertEquals(2, secondTransaction.date().getMonthValue());
        assertEquals(16, secondTransaction.date().getDayOfMonth());
    }

    @Test
    void getTransactionHistoryOfNonexistentUser() {
        TransactionHistory history = daoTransactionHistory.getTransactionHistory(NONEXISTENT_USER);
        assertEquals(0, history.getAmountOfTransactions());
    }
}