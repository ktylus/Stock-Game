package org.stock_game;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

public interface StockAPIConnection {

    public BigDecimal getStockPriceByCompanyCode(String code) throws URISyntaxException, IOException, InterruptedException;

}
