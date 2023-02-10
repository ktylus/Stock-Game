package org.stock_game;

public class PortfolioManager {

    private Portfolio portfolio;
    private TransactionHistory transactionHistory;

    PortfolioManager(Portfolio portfolio, TransactionHistory transactionHistory) {
        this.portfolio = portfolio;
        this.transactionHistory = transactionHistory;
    }

    public void buyStock(String code, int units) {

    }

    public void sellStock(String code, int units) {

    }
}
