package org.stockgame;

import java.math.BigDecimal;

public interface StockAPIConnection {

    BigDecimal getStockPriceByCompanyCode(String companyCode) throws StockAPIException;

    static StockAPIConnection createInstance() {
        return new FinnhubConnection();
    }

}
