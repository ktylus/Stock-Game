package org.stockgame.portfolio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.stockgame.utilities.TestUtilities;
import org.stockgame.stockapi.StockAPIConnection;
import org.stockgame.stockapi.StockAPIException;
import org.stockgame.transaction.TransactionHistory;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioManagerTest {

    private PortfolioManager manager;
    private TransactionHistory transactionHistory;
    private Portfolio portfolio;
    private final StockAPIConnection apiConnection = StockAPIConnection.createInstance();
    private BigDecimal startingBalance;

    @BeforeEach
    void setUp() {
        portfolio = new Portfolio();
        startingBalance = portfolio.getBalance();
        portfolio.addStock("IBM", 5);
        portfolio.addStock("TSLA", 10);
        transactionHistory = new TransactionHistory();
        manager = new PortfolioManager(portfolio, transactionHistory);
    }

    boolean checkIfPortfolioAndHistoryUnchanged() {
        return transactionHistory.getNumberOfTransactions() == 0 &&
                portfolio.getAllStocks().size() == 2 &&
                portfolio.getBalance().equals(startingBalance) &&
                portfolio.getAllStocks().get(0).companyCode().equals("IBM") &&
                portfolio.getAllStocks().get(0).units() == 5 &&
                portfolio.getAllStocks().get(1).companyCode().equals("TSLA") &&
                portfolio.getAllStocks().get(1).units() == 10;
    }

    @Test
    void buyInvalidStock() {
        manager.buyStock(TestUtilities.INVALID_COMPANY_CODE, 1);
        assertTrue(checkIfPortfolioAndHistoryUnchanged());
    }

    @Test
    void buyMoreStockThanCanAfford() throws StockAPIException {
        BigDecimal IBMStockPrice = apiConnection.getStockPriceByCompanyCode("IBM");
        int unitsToBuy = startingBalance.divideToIntegralValue(IBMStockPrice).intValue() + 1;
        manager.buyStock("IBM", unitsToBuy);
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
        int unitsToBuy = startingBalance.divideToIntegralValue(IBMStockPrice).intValue() / 2;
        manager.buyStock("IBM", unitsToBuy);
        IBMStockPrice = transactionHistory.getLatestTransaction().unitPrice();
        BigDecimal expectedBalance = startingBalance.subtract(IBMStockPrice.multiply(new BigDecimal(unitsToBuy)));
        assertEquals(expectedBalance, portfolio.getBalance());
        assertEquals(2, portfolio.getAllStocks().size());
        assertEquals(1, transactionHistory.getNumberOfTransactions());
    }

    @Test
    void buyValidAmountOfNewStock() throws StockAPIException {
        BigDecimal AAPLStockPrice = apiConnection.getStockPriceByCompanyCode("AAPL");
        int unitsToBuy = startingBalance.divideToIntegralValue(AAPLStockPrice).intValue() / 2;
        manager.buyStock("AAPL", unitsToBuy);
        AAPLStockPrice = transactionHistory.getLatestTransaction().unitPrice();
        BigDecimal expectedBalance = startingBalance.subtract(AAPLStockPrice.multiply(new BigDecimal(unitsToBuy)));
        assertEquals(expectedBalance, portfolio.getBalance());
        assertEquals(3, portfolio.getAllStocks().size());
        assertEquals(1, transactionHistory.getNumberOfTransactions());
    }

    @Test
    void sellMoreStockThanOwned() {
        manager.sellStock("IBM", 6);
        assertTrue(checkIfPortfolioAndHistoryUnchanged());
    }

    @Test
    void sellZeroUnitsOfStock() {
        manager.sellStock("IBM", 0);
        assertTrue(checkIfPortfolioAndHistoryUnchanged());
    }

    @Test
    void sellValidAmountOfStock() {
        manager.sellStock("IBM", 2);
        BigDecimal IBMStockPrice = transactionHistory.getLatestTransaction().unitPrice();
        BigDecimal expectedBalance = startingBalance.add(IBMStockPrice.multiply(new BigDecimal(2)));
        assertEquals(expectedBalance, portfolio.getBalance());
        assertEquals(2, portfolio.getAllStocks().size());
        assertEquals(1, transactionHistory.getNumberOfTransactions());
    }

    @Test
    void sellExactlyOwnedAmountOfStock() {
        manager.sellStock("TSLA", 10);
        BigDecimal TSLAStockPrice = transactionHistory.getLatestTransaction().unitPrice();
        BigDecimal expectedBalance = startingBalance.add(TSLAStockPrice.multiply(new BigDecimal(10)));
        assertEquals(expectedBalance, portfolio.getBalance());
        assertEquals(1, portfolio.getAllStocks().size());
        assertEquals(1, transactionHistory.getNumberOfTransactions());
    }
}