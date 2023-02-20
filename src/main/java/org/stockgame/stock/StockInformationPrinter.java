package org.stockgame.stock;

import org.stockgame.stockapi.StockAPIConnection;
import org.stockgame.stockapi.StockAPIException;

import java.math.BigDecimal;

/**
 * A class responsible for displaying information
 * about a given stock.
 */
public class StockInformationPrinter {

    public void printStockPrice(String companyCode) {
        StockAPIConnection apiConnection = StockAPIConnection.createInstance();
        try {
            BigDecimal stockPrice = apiConnection.getStockPriceByCompanyCode(companyCode);
            System.out.println("Price of " + companyCode + " is: " + stockPrice + "$.");
        } catch (StockAPIException e) {
            System.err.println("Couldn't display stock price.");
            System.err.println(e.getMessage());
        }
    }
}
