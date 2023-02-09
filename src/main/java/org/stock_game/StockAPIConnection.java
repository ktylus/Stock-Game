package org.stock_game;

import java.math.BigDecimal;

public interface StockAPIConnection {

    public BigDecimal getStockPriceByCompanyCode(String code) throws StockAPIConnectionException;

    public static StockAPIConnection createInstance() {
        return new FinnhubConnection();
    }

}
