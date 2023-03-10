package org.stockgame.portfolio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.stockgame.utilities.TestUtilities;
import org.stockgame.stock.StockInPortfolio;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioTest {

    private Portfolio portfolio;

    @BeforeEach
    void setUp() {
        portfolio = new Portfolio();
        portfolio.addStock("IBM", 5);
        portfolio.addStock("TSLA", 10);
    }

    @Test
    void getStockInPortfolioByCode() {
        StockInPortfolio stock = portfolio.getStockByCode("TSLA");
        StockInPortfolio expectedStock = new StockInPortfolio("TSLA", 10);
        assertEquals(expectedStock, stock);
    }

    @Test
    void getStockNotInPortfolioByCode() {
        StockInPortfolio stock = portfolio.getStockByCode("ZZZ");
        StockInPortfolio expectedStock = new StockInPortfolio("ZZZ", 0);
        assertEquals(expectedStock, stock);
    }

    @Test
    void getAllStocks() {
        List<StockInPortfolio> allStocks = portfolio.getAllStocks();
        StockInPortfolio firstStock = allStocks.get(0);
        StockInPortfolio secondStock = allStocks.get(1);
        StockInPortfolio expectedFirstStock = new StockInPortfolio("IBM", 5);
        StockInPortfolio expectedSecondStock = new StockInPortfolio("TSLA", 10);
        assertEquals(expectedFirstStock, firstStock);
        assertEquals(expectedSecondStock, secondStock);
    }

    @Test
    void getBalance() {
        double expectedStartBalance = new BigDecimal("1000.00").doubleValue();
        assertEquals(expectedStartBalance, portfolio.getBalance().doubleValue());
    }

    @Test
    void setBalance() {
        portfolio.setBalance(new BigDecimal("1234.56"));
        double expectedBalance = new BigDecimal("1234.56").doubleValue();
        assertEquals(expectedBalance, portfolio.getBalance().doubleValue());
    }

    @Test
    void addUnitsToExistingStock() {
        portfolio.addStock("IBM", 2);
        assertEquals(7, portfolio.getStockByCode("IBM").units());
        assertEquals(2, portfolio.getAllStocks().size());
    }

    @Test
    void addNewStock() {
        portfolio.addStock("AAPL", 5);
        assertEquals(5, portfolio.getStockByCode("AAPL").units());
        assertEquals(3, portfolio.getAllStocks().size());
    }

    @Test
    void removeAllUnitsOfStock() throws PortfolioException {
        portfolio.removeStock("IBM", 5);
        assertEquals(0, portfolio.getStockByCode("IBM").units());
        assertEquals(1, portfolio.getAllStocks().size());
    }

    @Test
    void removeNotAllUnitsOfStock() throws PortfolioException {
        portfolio.removeStock("IBM", 3);
        assertEquals(2, portfolio.getStockByCode("IBM").units());
        assertEquals(2, portfolio.getAllStocks().size());
    }

    @Test
    void removeMoreUnitsOfStockThanOwned() {
        assertThrows(
                PortfolioException.class,
                () -> portfolio.removeStock("IBM", 8)
        );
    }

    @Test
    void removeZeroOfOwnedStock() throws PortfolioException {
        portfolio.removeStock("IBM", 0);
        assertEquals(5, portfolio.getStockByCode("IBM").units());
        assertEquals(2, portfolio.getAllStocks().size());
    }

    @Test
    void removeZeroOfNotOwnedStock() throws PortfolioException {
        portfolio.removeStock("ZZZ", 0);
    }

    @Test
    void removeNotOwnedStock() {
        assertThrows(
                PortfolioException.class,
                () -> portfolio.removeStock("ZZZ", 1)
        );
    }
}