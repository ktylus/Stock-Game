package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Portfolio {

    private final List<StockInPortfolio> stocks;
    private BigDecimal balance;

    Portfolio() {
        stocks = new ArrayList<>();
        balance = new BigDecimal("1000.00");
    }

    public StockInPortfolio getStockByCode(String code) {
        for (StockInPortfolio stock : stocks) {
            if (stock.getCompanyCode().equals(code)) {
                return stock;
            }
        }
        return new StockInPortfolio(code, 0);
    }

    public List<StockInPortfolio> getAllStocks() {
        return stocks;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal newBalance) {
        balance = newBalance;
    }

    public void addStock(String code, int units) {
        if (containsStockWithCode(code)) {
            addUnitsToExistingStock(code, units);
        }
        else {
            addNewStock(code, units);
        }
    }

    public void removeStock(String code, int units) {
        // error if no such stock
        // error if removing more than there is
        // case 1: removing exactly the amount that there is -> delete stock from list
        // case 2: removing less -> do just that
    }

    private boolean containsStockWithCode(String code) {
        for (StockInPortfolio stock : stocks) {
            if (stock.getCompanyCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    private void addNewStock(String code, int units) {
        StockInPortfolio newStock = new StockInPortfolio(code, units);
        stocks.add(newStock);
    }

    private void addUnitsToExistingStock(String code, int units) {
        StockInPortfolio existingStock = getStockByCode(code);
        int unitsBeforeChange = existingStock.getUnits();
        existingStock.setUnits(unitsBeforeChange + units);
    }
}
