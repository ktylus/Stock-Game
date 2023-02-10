package org.stock_game;

import java.math.BigDecimal;

public interface StockAPIConnection {

    BigDecimal getStockPriceByCompanyCode(String code) throws StockAPIConnectionException;

    static StockAPIConnection createInstance() {
        return new FinnhubConnection();
    }

}
