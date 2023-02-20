package org.stockgame.portfolio;

/**
 * <p>An exception class, enforcing certain constraints
 * on a portfolio.</p>
 *
 * <p>For example - one cannot have a negative number
 * of any stock in portfolio.</p>
 */
public class PortfolioException extends Exception {

    public PortfolioException(String message) {
        super(message);
    }
}
