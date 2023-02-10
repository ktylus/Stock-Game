package org.stock_game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioManagerTest {

    private PortfolioManager manager;
    private TransactionHistory transactionHistory;
    private Portfolio portfolio;
    private final StockAPIConnection apiConnection = StockAPIConnection.createInstance();
    private BigDecimal STARTING_BALANCE;

    @BeforeEach
    void setUp() {
        portfolio = new Portfolio();
        STARTING_BALANCE = portfolio.getBalance();
        portfolio.addStock("IBM", 5);
        portfolio.addStock("TSLA", 10);
        transactionHistory = new TransactionHistory();
        manager = new PortfolioManager(portfolio, transactionHistory);
    }

    boolean checkIfPortfolioAndHistoryUnchanged() {
        return transactionHistory.getAmountOfTransactions() == 0 &&
                portfolio.getAllStocks().size() == 0 &&
                portfolio.getBalance().doubleValue() == 1000 &&
                portfolio.getAllStocks().get(0).getCompanyCode().equals("IBM") &&
                portfolio.getAllStocks().get(0).getUnits() == 5 &&
                portfolio.getAllStocks().get(1).getCompanyCode().equals("TSLA") &&
                portfolio.getAllStocks().get(1).getUnits() == 10;
    }

    @Test
    void buyInvalidStock() {
        assertThrows(
                StockAPIException.class,
                () -> manager.buyStock("XXXXXXXXXXXXXX", 1)
        );
        assertTrue(checkIfPortfolioAndHistoryUnchanged());
    }

    @Test
    void buyMoreStockThanCanAfford() throws StockAPIException {
        BigDecimal IBMStockPrice = apiConnection.getStockPriceByCompanyCode("IBM");
        int unitsToBuy = STARTING_BALANCE.divideToIntegralValue(IBMStockPrice).intValue() + 1;
        assertThrows(
                PortfolioException.class,
                () -> manager.buyStock("IBM", unitsToBuy)
        );
        assertTrue(checkIfPortfolioAndHistoryUnchanged());
    }

    @Test
    void buyZeroUnitsOfStock() {
        manager.buyStock("IBM", 0);
        assertTrue(checkIfPortfolioAndHistoryUnchanged());
    }

    @Test
    void buyValidAmountOfOwnedStock() throws StockAPIException {
        BigDecimal IBMStockPrice = apiConnection.getStockPriceByCompanyCode("IBM");
        int unitsToBuy = STARTING_BALANCE.divideToIntegralValue(IBMStockPrice).intValue() / 2;
        manager.buyStock("IBM", unitsToBuy);
        BigDecimal expectedBalance = STARTING_BALANCE.subtract(IBMStockPrice.multiply(new BigDecimal(unitsToBuy)));
        assertEquals(expectedBalance, portfolio.getBalance());
        assertEquals(2, portfolio.getAllStocks().size());
        assertEquals(1, transactionHistory.getAmountOfTransactions());
    }

    @Test
    void buyValidAmountOfNewStock() throws StockAPIException {
        BigDecimal AAPLStockPrice = apiConnection.getStockPriceByCompanyCode("AAPL");
        int unitsToBuy = STARTING_BALANCE.divideToIntegralValue(AAPLStockPrice).intValue() / 2;
        manager.buyStock("AAPL", unitsToBuy);
        BigDecimal expectedBalance = STARTING_BALANCE.subtract(AAPLStockPrice.multiply(new BigDecimal(unitsToBuy)));
        assertEquals(expectedBalance, portfolio.getBalance());
        assertEquals(3, portfolio.getAllStocks().size());
        assertEquals(1, transactionHistory.getAmountOfTransactions());
    }

    @Test
    void sellMoreStockThanOwned() {
        assertThrows(
                PortfolioException.class,
                () -> manager.sellStock("IBM", 6)
        );
        assertTrue(checkIfPortfolioAndHistoryUnchanged());
    }

    @Test
    void sellZeroUnitsOfStock() {
        manager.sellStock("IBM", 0);
        assertTrue(checkIfPortfolioAndHistoryUnchanged());
    }

    @Test
    void sellValidAmountOfStock() throws StockAPIException {
        BigDecimal IBMStockPrice = apiConnection.getStockPriceByCompanyCode("IBM");
        manager.sellStock("IBM", 2);
        BigDecimal expectedBalance = STARTING_BALANCE.add(IBMStockPrice.multiply(new BigDecimal(2)));
        assertEquals(expectedBalance, portfolio.getBalance());
        assertEquals(2, portfolio.getAllStocks().size());
        assertEquals(1, transactionHistory.getAmountOfTransactions());
    }

    @Test
    void sellExactlyOwnedAmountOfStock() throws StockAPIException {
        BigDecimal TSLAStockPrice = apiConnection.getStockPriceByCompanyCode("TSLA");
        manager.sellStock("TSLA", 10);
        BigDecimal expectedBalance = STARTING_BALANCE.add(TSLAStockPrice.multiply(new BigDecimal(10)));
        assertEquals(expectedBalance, portfolio.getBalance());
        assertEquals(1, portfolio.getAllStocks().size());
        assertEquals(1, transactionHistory.getAmountOfTransactions());
    }
}