package org.stock_game;

import java.util.Scanner;

public class User {

    private final String username;
    private final Portfolio portfolio;
    private final TransactionHistory transactionHistory;

    User() {
        username = "";
        portfolio = new Portfolio();
        transactionHistory = new TransactionHistory();
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        while (!command.equals("end")) {
            selectOperation(command);
            command = scanner.nextLine();
        }
    }

    private void selectOperation(String command) {
        String operation = getOperation(command);
        String[] parameters = getParameters(command);

        switch (operation) {
            case "balance" -> displayBalance();
            case "portfolio" -> displayPortfolio();
            case "lastTransaction" -> displayLastTransaction();
            case "transactionHistory" -> displayTransactionHistory(parameters);
            case "price" -> displayStockPrice(parameters);
            case "buy" -> buyStock(parameters);
            case "sell" -> sellStock(parameters);
            default -> System.out.println("Invalid operation");
        }
    }

    private String getOperation(String command) {
        return command.split(" ")[0];
    }

    private String[] getParameters(String command) {
        String[] parametersWithOperation = command.split(" ");
        String[] parameters = new String[parametersWithOperation.length - 1];
        System.arraycopy(parametersWithOperation, 1,
                parameters, 0,
                parametersWithOperation.length - 1);
        return parameters;
    }

    private void displayBalance() {
        portfolio.displayBalance();
    }

    private void displayPortfolio() {
        portfolio.display();
    }

    private void displayLastTransaction() {
        transactionHistory.getLatestTransaction().display();
    }

    private void displayTransactionHistory(String[] parameters) {
        int numberOfTransactions = Integer.parseInt(parameters[0]);
        transactionHistory.display(numberOfTransactions);
    }

    private void displayStockPrice(String[] parameters) {
        String companyCode = parameters[0];
        (new StockInformationPrinter()).printStockPrice(companyCode);
    }

    private void buyStock(String[] parameters) {
        String companyCode = parameters[0];
        int units = Integer.parseInt(parameters[1]);
        (new PortfolioManager(portfolio, transactionHistory)).buyStock(companyCode, units);
    }

    private void sellStock(String[] parameters) {
        String companyCode = parameters[0];
        int units = Integer.parseInt(parameters[1]);
        (new PortfolioManager(portfolio, transactionHistory)).sellStock(companyCode, units);
    }
}
