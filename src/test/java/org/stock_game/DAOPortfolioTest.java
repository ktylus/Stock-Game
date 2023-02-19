package org.stock_game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.stock_game.TestUtilities.TEST_DATABASE;
import static org.stock_game.TestUtilities.TEST_USER;
import static org.stock_game.TestUtilities.NONEXISTENT_USER;

class DAOPortfolioTest {

    private DAOPortfolio daoPortfolio;

    @BeforeEach
    void setUp() {
        daoPortfolio = new DAOPortfolio(TEST_DATABASE);
    }

    @Test
    void getPortfolioOfExistingUser() {
        Portfolio portfolio = daoPortfolio.getPortfolio(TEST_USER);
        assertEquals("2000.00", portfolio.getBalance().toString());
        assertEquals(5, portfolio.getStockByCode("IBM").units());
    }

    @Test
    void getPortfolioOfNonexistentUser() {
        Portfolio portfolio = daoPortfolio.getPortfolio(NONEXISTENT_USER);
        assertEquals(0, portfolio.getAllStocks().size());
        assertNull(portfolio.getBalance());
    }
}