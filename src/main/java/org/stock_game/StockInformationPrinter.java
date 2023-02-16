package org.stock_game;

import java.math.BigDecimal;

public class StockInformationPrinter {

    public void printStockPrice(String code) {
        StockAPIConnection apiConnection = StockAPIConnection.createInstance();
        try {
            BigDecimal stockPrice = apiConnection.getStockPriceByCompanyCode(code);
            System.out.println("Price of " + code + " is: " + stockPrice + "$.");
        } catch (StockAPIException e) {
            System.err.println(e.getMessage());
        }
    }
}
