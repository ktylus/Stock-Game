package org.stock_game;

public class StockAPIException extends Exception {

    StockAPIException(String errorMessage) {
        super(errorMessage);
    }
}
