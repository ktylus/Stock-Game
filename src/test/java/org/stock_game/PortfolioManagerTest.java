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
                portfolio.getAllStocks().size() == 2 &&
                portfolio.getBalance().doubleValue() == 1000 &&
                portfolio.getAllStocks().get(0).getCompanyCode().equals("IBM") &&
                portfolio.getAllStocks().get(0).getUnits() == 5 &&
                portfolio.getAllStocks().get(1).getCompanyCode().equals("TSLA") &&
                portfolio.getAllStocks().get(1).getUnits() == 10;
    }

    @Test
    void buyInvalidStock() {
        manager.buyStock("XXXXXXXXXXXXXX", 1);
        assertTrue(checkIfPortfolioAndHistoryUnchanged());
    }

    @Test
    void buyMoreStockThanCanAfford() throws StockAPIException {
        BigDecimal IBMStockPrice = apiConnection.getStockPriceByCompanyCode("IBM");
        int unitsToBuy = STARTING_BALANCE.divideToIntegralValue(IBMStockPrice).intValue() + 1;
        manager.buyStock("IBM", unitsToBuy);
        assertTrue(checkIfPortfolioAndHistoryUnchanged());
    }

    @Test
    void buyZeroUnitsOfStock() throws PortfolioException, StockAPIException {
        manager.buyStock("IBM", 0);
        assertTrue(checkIfPortfolioAndHistoryUnchanged());
    }

    @Test
    void buyValidAmountOfOwnedStock() throws StockAPIException, PortfolioException {
        BigDecimal IBMStockPrice = apiConnection.getStockPriceByCompanyCode("IBM");
        int unitsToBuy = STARTING_BALANCE.divideToIntegralValue(IBMStockPrice).intValue() / 2;
        manager.buyStock("IBM", unitsToBuy);
        IBMStockPrice = transactionHistory.getLatestTransaction().unitPrice();
        BigDecimal expectedBalance = STARTING_BALANCE.subtract(IBMStockPrice.multiply(new BigDecimal(unitsToBuy)));
        assertEquals(expectedBalance, portfolio.getBalance());
        assertEquals(2, portfolio.getAllStocks().size());
        assertEquals(1, transactionHistory.getAmountOfTransactions());
    }

    @Test
    void buyValidAmountOfNewStock() throws StockAPIException, PortfolioException {
        BigDecimal AAPLStockPrice = apiConnection.getStockPriceByCompanyCode("AAPL");
        int unitsToBuy = STARTING_BALANCE.divideToIntegralValue(AAPLStockPrice).intValue() / 2;
        manager.buyStock("AAPL", unitsToBuy);
        AAPLStockPrice = transactionHistory.getLatestTransaction().unitPrice();
        BigDecimal expectedBalance = STARTING_BALANCE.subtract(AAPLStockPrice.multiply(new BigDecimal(unitsToBuy)));
        assertEquals(expectedBalance, portfolio.getBalance());
        assertEquals(3, portfolio.getAllStocks().size());
        assertEquals(1, transactionHistory.getAmountOfTransactions());
    }

    @Test
    void sellMoreStockThanOwned() {
        manager.sellStock("IBM", 6);
        assertTrue(checkIfPortfolioAndHistoryUnchanged());
    }

    @Test
    void sellZeroUnitsOfStock() throws PortfolioException, StockAPIException {
        manager.sellStock("IBM", 0);
        assertTrue(checkIfPortfolioAndHistoryUnchanged());
    }

    @Test
    void sellValidAmountOfStock() throws StockAPIException, PortfolioException {
        manager.sellStock("IBM", 2);
        BigDecimal IBMStockPrice = transactionHistory.getLatestTransaction().unitPrice();
        BigDecimal expectedBalance = STARTING_BALANCE.add(IBMStockPrice.multiply(new BigDecimal(2)));
        assertEquals(expectedBalance, portfolio.getBalance());
        assertEquals(2, portfolio.getAllStocks().size());
        assertEquals(1, transactionHistory.getAmountOfTransactions());
    }

    @Test
    void sellExactlyOwnedAmountOfStock() throws StockAPIException, PortfolioException {
        manager.sellStock("TSLA", 10);
        BigDecimal TSLAStockPrice = transactionHistory.getLatestTransaction().unitPrice();
        BigDecimal expectedBalance = STARTING_BALANCE.add(TSLAStockPrice.multiply(new BigDecimal(10)));
        assertEquals(expectedBalance, portfolio.getBalance());
        assertEquals(1, portfolio.getAllStocks().size());
        assertEquals(1, transactionHistory.getAmountOfTransactions());
    }
}