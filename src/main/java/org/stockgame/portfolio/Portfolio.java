package org.stockgame.portfolio;

import org.stockgame.stock.StockInPortfolio;
import org.stockgame.dbaccess.DBUpdater;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Portfolio {

    private final List<StockInPortfolio> stocks;
    private BigDecimal balance;
    private static final String STARTING_BALANCE = "1000.00";

    public Portfolio() {
        stocks = new ArrayList<>();
        balance = new BigDecimal(STARTING_BALANCE);
    }

    public StockInPortfolio getStockByCode(String companyCode) {
        for (StockInPortfolio stock : stocks) {
            if (stock.companyCode().equals(companyCode)) {
                return stock;
            }
        }
        return new StockInPortfolio(companyCode, 0);
    }

    public List<StockInPortfolio> getAllStocks() {
        return Collections.unmodifiableList(stocks);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal newBalance) {
        balance = newBalance;
    }

    public void addStock(String companyCode, int units) {
        if (containsStockWithCode(companyCode)) {
            addUnitsToExistingStock(companyCode, units);
        }
        else {
            addNewStock(companyCode, units);
        }
    }

    private boolean containsStockWithCode(String companyCode) {
        return getStockByCode(companyCode).units() != 0;
    }

    private void addUnitsToExistingStock(String companyCode, int units) {
        int unitsBeforeChange = getStockByCode(companyCode).units();
        try {
            removeStock(companyCode, unitsBeforeChange);
        } catch (PortfolioException e) {
            System.err.println(e.getMessage());
        }
        addNewStock(companyCode, unitsBeforeChange + units);
    }

    private void addNewStock(String companyCode, int units) {
        StockInPortfolio newStock = new StockInPortfolio(companyCode, units);
        stocks.add(newStock);
    }

    public void removeStock(String companyCode, int unitsRemoved) throws PortfolioException {
        if (unitsRemoved == 0) {
            return;
        }

        StockInPortfolio stock = getStockByCode(companyCode);
        int ownedUnits = stock.units();
        if (unitsRemoved > ownedUnits) {
            throw new PortfolioException("Attempted to remove more units of stock than owned.");
        }

        stocks.remove(stock);
        int stocksAfterRemoval = ownedUnits - unitsRemoved;
        if (stocksAfterRemoval > 0) {
            stocks.add(new StockInPortfolio(companyCode, stocksAfterRemoval));
        }
    }

    public void print() {
        PortfolioPrinter printer = new PortfolioPrinter(this);
        printer.printPortfolio();
    }

    public void printBalance() {
        PortfolioPrinter printer = new PortfolioPrinter(this);
        printer.printBalance();
    }

    public void updateInDB(String username) {
        (new DBUpdater(username)).updatePortfolio(this);
    }
}
