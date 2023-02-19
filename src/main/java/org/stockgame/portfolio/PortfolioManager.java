package org.stockgame.portfolio;

import org.stockgame.stockapi.StockAPIConnection;
import org.stockgame.stockapi.StockAPIException;
import org.stockgame.transaction.Transaction;
import org.stockgame.transaction.TransactionHistory;
import org.stockgame.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PortfolioManager {

    private final Portfolio portfolio;
    private final TransactionHistory transactionHistory;

    public PortfolioManager(Portfolio portfolio, TransactionHistory transactionHistory) {
        this.portfolio = portfolio;
        this.transactionHistory = transactionHistory;
    }

    public void buyStock(String code, int units) {
        if (units <= 0) {
            return;
        }

        try {
            BigDecimal stockPrice = getStockPrice(code);
            if (canAfford(stockPrice, units)) {
                buyStockWhenCanAfford(code, units, stockPrice);
                System.out.println("Bought " + units + " of " + code + '.');
            }
            else {
                System.out.println("You can't afford " + units + " units of " + code + '.');
            }
        } catch (StockAPIException e) {
            System.err.println(e.getMessage());
        }
    }

    private BigDecimal getStockPrice(String code) throws StockAPIException {
        StockAPIConnection apiConnection = StockAPIConnection.createInstance();
        return apiConnection.getStockPriceByCompanyCode(code);
    }

    private boolean canAfford(BigDecimal unitPrice, int units) {
        BigDecimal totalCost = calculateTotalValue(unitPrice, units);
        return totalCost.compareTo(portfolio.getBalance()) <= 0;
    }

    private void buyStockWhenCanAfford(String code, int units, BigDecimal stockPrice) {
        portfolio.addStock(code, units);
        transactionHistory.addTransaction(
                new Transaction(code, units, stockPrice, TransactionType.PURCHASE, LocalDate.now())
        );
        BigDecimal balanceAfterTransaction = portfolio.getBalance().subtract(calculateTotalValue(stockPrice, units));
        portfolio.setBalance(balanceAfterTransaction);
    }

    private BigDecimal calculateTotalValue(BigDecimal unitPrice, int units) {
        return unitPrice.multiply(new BigDecimal(units));
    }

    public void sellStock(String code, int units) {
        if (units <= 0) {
            return;
        }

        boolean haveEnoughUnits = units <= portfolio.getStockByCode(code).units();
        if (haveEnoughUnits) {
            sellValidAmountOfStock(code, units);
            System.out.println("Sold " + units + " of " + code + '.');
        }
        else {
            System.out.println("You don't have " + units + " units of " + code + '.');
        }
    }

    private void sellValidAmountOfStock(String code, int units) {
        try {
            portfolio.removeStock(code, units);
            BigDecimal unitPrice = getStockPrice(code);
            transactionHistory.addTransaction(
                    new Transaction(code, units, unitPrice, TransactionType.SALE, LocalDate.now())
            );
            BigDecimal balanceAfterTransaction = portfolio.getBalance().add(calculateTotalValue(unitPrice, units));
            portfolio.setBalance(balanceAfterTransaction);
        } catch (StockAPIException | PortfolioException e) {
            System.err.println(e.getMessage());
        }
    }
}