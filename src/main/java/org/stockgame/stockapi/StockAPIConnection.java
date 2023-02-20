package org.stockgame.stockapi;

import java.math.BigDecimal;

/**
 * <p>An interface containing features, which
 * need to be implemented by any class
 * handling connection with a specific API.</p>
 *
 * <p>Also has a static createInstance() method
 * to ensure a simple way of getting access to an API,
 * without giving details about certain specific APIs</p>
 */
public interface StockAPIConnection {

    BigDecimal getStockPriceByCompanyCode(String companyCode) throws StockAPIException;

    static StockAPIConnection createInstance() {
        return new FinnhubConnection();
    }

}
