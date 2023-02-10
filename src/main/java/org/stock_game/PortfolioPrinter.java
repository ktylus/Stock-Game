package org.stock_game;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PortfolioPrinter {

    private Portfolio portfolio;
    Map<String, BigDecimal> stockPrices;

    PortfolioPrinter(Portfolio portfolio) {
        this.portfolio = portfolio;
        stockPrices = new HashMap<>();
    }

    public void printContent() {
        printAllStocks();
        printBalance();
        printTotalAssetsValue();
    }

    private void displayStock(StockInPortfolio stock) {
        BigDecimal stockPrice = getStockPrice(stock);
        System.out.println(stock.getCompanyCode() + "  |  " + stock.getUnits() + "  |  " + stockPrice + '$');
    }

    private void printAllStocks() {
        stockPrices = getAllStockPrices();
        System.out.println("Portfolio content: ");
        for (StockInPortfolio stock : portfolio.getAllStocks()) {
            displayStock(stock);
        }
    }

    public void printBalance() {
        System.out.println("Balance: " + portfolio.getBalance() + '$');
    }

    private void printTotalAssetsValue() {
        System.out.println("Total value: " + calculateTotalAssetsValue() + '$');
    }

    private BigDecimal calculateTotalAssetsValue() {
        BigDecimal totalValue = portfolio.getBalance();
        for (StockInPortfolio stock : portfolio.getAllStocks()) {
            BigDecimal stockValue = stockPrices.get(stock.getCompanyCode()).multiply(new BigDecimal(stock.getUnits()));
            totalValue = totalValue.add(stockValue);
        }
        return totalValue;
    }

    private Map<String, BigDecimal> getAllStockPrices() {
        Map<String, BigDecimal> stockPrices = new HashMap<>();
        for (StockInPortfolio stock : portfolio.getAllStocks()) {
            stockPrices.put(stock.getCompanyCode(), getStockPrice(stock));
        }
        return stockPrices;
    }

    private BigDecimal getStockPrice(StockInPortfolio stock) {
        StockAPIConnection apiConnection = StockAPIConnection.createInstance();
        BigDecimal stockPrice = new BigDecimal(0);
        try {
            stockPrice = apiConnection.getStockPriceByCompanyCode(stock.getCompanyCode());
        } catch (StockAPIConnectionException e) {
            e.printStackTrace();
        }
        return stockPrice;
    }

    public static void main(String[] args) {
        PortfolioPrinter pp = new PortfolioPrinter(new Portfolio());
        pp.portfolio.addStock("IBM", 5);
        pp.portfolio.addStock("TSLA", 10);
        pp.printContent();
    }
}
