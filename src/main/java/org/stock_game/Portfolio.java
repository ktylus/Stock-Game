package org.stock_game;

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

    public void removeStock(String code, int unitsRemoved) throws PortfolioException {
        if (unitsRemoved == 0) {
            return;
        }

        StockInPortfolio stock = getStockByCode(code);
        int ownedUnits = stock.getUnits();
        if (unitsRemoved > ownedUnits) {
            throw new PortfolioException("Attempted to remove more units of stock than owned.");
        }

        stocks.remove(stock);
        if (ownedUnits - unitsRemoved > 0) {
            stocks.add(new StockInPortfolio(code, ownedUnits - unitsRemoved));
        }
    }

    private boolean containsStockWithCode(String code) {
        return getStockByCode(code).getUnits() != 0;
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

    public void display() {
        PortfolioPrinter printer = new PortfolioPrinter(this);
        printer.printPortfolio();
    }

    public void displayBalance() {
        PortfolioPrinter printer = new PortfolioPrinter(this);
        printer.printBalance();
    }
}
