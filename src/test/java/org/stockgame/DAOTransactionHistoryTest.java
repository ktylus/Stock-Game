package org.stockgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import static org.stockgame.TestUtilities.TEST_DATABASE;
import static org.stockgame.TestUtilities.TEST_USER;
import static org.stockgame.TestUtilities.NONEXISTENT_USER;

class DAOTransactionHistoryTest {

    private DAOTransactionHistory daoTransactionHistory;

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
        assertTrue(TestUtilities.isTransactionCorrect(firstTransaction, "IBM", 5,
                new BigDecimal("100.00"), TransactionType.PURCHASE,
                LocalDate.of(2023, 2, 15)));
        assertTrue(TestUtilities.isTransactionCorrect(secondTransaction, "TSLA", 10,
                new BigDecimal("150.00"), TransactionType.SALE,
                LocalDate.of(2023, 2, 16)));
    }

    @Test
    void getTransactionHistoryOfNonexistentUser() {
        TransactionHistory history = daoTransactionHistory.getTransactionHistory(NONEXISTENT_USER);
        assertEquals(0, history.getAmountOfTransactions());
    }
}