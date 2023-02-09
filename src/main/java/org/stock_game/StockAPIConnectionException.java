package org.stock_game;

public class StockAPIConnectionException extends Exception {

    StockAPIConnectionException(String errorMessage) {
        super(errorMessage);
    }
}
