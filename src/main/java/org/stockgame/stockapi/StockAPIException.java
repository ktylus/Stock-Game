package org.stockgame.stockapi;

/**
 * <p>An exception class, that is meant to describe
 * all the different errors that may arise while
 * using the stock API.</p>
 *
 * <p>This is a way to summarize all of these
 * exceptions in one, while having necessary
 * details in an exception message.</p>
 */
public class StockAPIException extends Exception {

    public StockAPIException(String message) {
        super(message);
    }
}
