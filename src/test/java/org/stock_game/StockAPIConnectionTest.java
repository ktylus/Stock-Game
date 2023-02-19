package org.stock_game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockAPIConnectionTest {

    private final StockAPIConnection apiConnection = new FinnhubConnection();

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
                () -> apiConnection.getStockPriceByCompanyCode(TestUtilities.INVALID_COMPANY_CODE)
        );
    }
}