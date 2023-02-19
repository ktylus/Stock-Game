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

    public void buyStock(String companyCode, int units) {
        if (units <= 0) {
            System.err.println("Can't buy " + units + " of stock.");
            return;
        }

        try {
            BigDecimal stockPrice = getStockPrice(companyCode);
            if (canAfford(stockPrice, units)) {
                buyStockWhenCanAfford(companyCode, units, stockPrice);
                System.out.println("Bought " + units + " of " + companyCode + '.');
            }
            else {
                System.err.println("You can't afford " + units + " units of " + companyCode + '.');
            }
        } catch (StockAPIException e) {
            System.err.println(e.getMessage());
        }
    }

    private BigDecimal getStockPrice(String companyCode) throws StockAPIException {
        StockAPIConnection apiConnection = StockAPIConnection.createInstance();
        return apiConnection.getStockPriceByCompanyCode(companyCode);
    }

    private boolean canAfford(BigDecimal unitPrice, int units) {
        BigDecimal totalCost = calculateTotalValue(unitPrice, units);
        return totalCost.compareTo(portfolio.getBalance()) <= 0;
    }

    private void buyStockWhenCanAfford(String companyCode, int units, BigDecimal stockPrice) {
        portfolio.addStock(companyCode, units);
        transactionHistory.addTransaction(
                new Transaction(companyCode, units, stockPrice, TransactionType.PURCHASE, LocalDate.now())
        );
        BigDecimal balanceAfterTransaction = portfolio.getBalance().subtract(calculateTotalValue(stockPrice, units));
        portfolio.setBalance(balanceAfterTransaction);
    }

    private BigDecimal calculateTotalValue(BigDecimal unitPrice, int units) {
        return unitPrice.multiply(new BigDecimal(units));
    }

    public void sellStock(String companyCode, int units) {
        if (units <= 0) {
            System.err.println("Can't sell " + units + " of stock.");
            return;
        }

        boolean haveEnoughUnits = units <= portfolio.getStockByCode(companyCode).units();
        if (haveEnoughUnits) {
            sellValidAmountOfStock(companyCode, units);
            System.out.println("Sold " + units + " of " + companyCode + '.');
        }
        else {
            System.err.println("You don't have " + units + " units of " + companyCode + '.');
        }
    }

    private void sellValidAmountOfStock(String companyCode, int units) {
        try {
            portfolio.removeStock(companyCode, units);
            BigDecimal unitPrice = getStockPrice(companyCode);
            transactionHistory.addTransaction(
                    new Transaction(companyCode, units, unitPrice, TransactionType.SALE, LocalDate.now())
            );
            BigDecimal balanceAfterTransaction = portfolio.getBalance().add(calculateTotalValue(unitPrice, units));
            portfolio.setBalance(balanceAfterTransaction);
        } catch (StockAPIException | PortfolioException e) {
            System.err.println(e.getMessage());
        }
    }
}
