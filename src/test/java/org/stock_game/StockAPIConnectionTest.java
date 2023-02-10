package org.stock_game;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class StockAPIConnectionTest {

    StockAPIConnection apiConnection = new FinnhubConnection();

    @Test
    void getStockPriceByCorrectCompanyCode() {
        assertDoesNotThrow(
                () -> apiConnection.getStockPriceByCompanyCode("IBM")
        );
    }

    @Test
    void getStockPriceByIncorrectCompanyCode() {
        assertThrows(
                StockAPIException.class,
                () -> apiConnection.getStockPriceByCompanyCode("XXXXXXXXXXX")
        );
    }
}