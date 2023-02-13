package org.stock_game;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PortfolioManager {

    private final Portfolio portfolio;
    private final TransactionHistory transactionHistory;

    PortfolioManager(Portfolio portfolio, TransactionHistory transactionHistory) {
        this.portfolio = portfolio;
        this.transactionHistory = transactionHistory;
    }

    public void buyStock(String code, int units) {
        if (units == 0) {
            return;
        }

        try {
            BigDecimal stockPrice = getStockPrice(code);
            if (canAfford(stockPrice, units)) {
                portfolio.addStock(code, units);
                transactionHistory.addTransaction(
                        new Transaction(code, units, stockPrice, TransactionType.PURCHASE, LocalDate.now())
                );
                portfolio.setBalance(portfolio.getBalance().subtract(calculateTotalValue(stockPrice, units)));
            }
            else {
                System.out.println("You can't afford " + units + " units of " + code);
            }
        } catch (StockAPIException e) {
            System.out.println(e.getMessage());
        }
    }

    private BigDecimal getStockPrice(String code) throws StockAPIException {
        StockAPIConnection apiConnection = StockAPIConnection.createInstance();
        BigDecimal stockPrice = apiConnection.getStockPriceByCompanyCode(code);
        return stockPrice;
    }

    private boolean canAfford(BigDecimal unitPrice, int units) {
        BigDecimal totalCost = calculateTotalValue(unitPrice, units);
        return totalCost.compareTo(portfolio.getBalance()) <= 0;
    }

    private BigDecimal calculateTotalValue(BigDecimal unitPrice, int units) {
        return unitPrice.multiply(new BigDecimal(units));
    }

    public void sellStock(String code, int units) {
        if (units == 0) {
            return;
        }

        boolean haveEnoughUnits = units <= portfolio.getStockByCode(code).getUnits();
        if (haveEnoughUnits) {
            try {
                portfolio.removeStock(code, units);
                BigDecimal unitPrice = getStockPrice(code);
                transactionHistory.addTransaction(
                        new Transaction(code, units, unitPrice, TransactionType.SALE, LocalDate.now())
                );
                portfolio.setBalance(portfolio.getBalance().add(calculateTotalValue(unitPrice, units)));
            } catch (StockAPIException | PortfolioException e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            System.out.println("You don't have " + units + " units of " + code);
        }
    }
}
