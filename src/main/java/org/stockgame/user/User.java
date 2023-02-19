package org.stockgame.user;

import org.stockgame.portfolio.Portfolio;
import org.stockgame.portfolio.PortfolioManager;
import org.stockgame.stock.StockInformationPrinter;
import org.stockgame.transaction.TransactionHistory;

import java.util.Scanner;

public class User {

    private final String username;
    private final Portfolio portfolio;
    private final TransactionHistory transactionHistory;

    User(String username, Portfolio portfolio, TransactionHistory transactionHistory) {
        this.username = username;
        this.portfolio = portfolio;
        this.transactionHistory = transactionHistory;
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type \"help\" to display available commands.");
        System.out.println("Enter command:");
        String command;
        do {
            command = scanner.nextLine();
            executeOperation(command);
            updateInDB();
        } while (!command.equals("end"));
    }

    private void executeOperation(String command) {
        String operation = getOperation(command);
        String[] parameters = getParameters(command);

        switch (operation) {
            case "balance" -> printBalance();
            case "portfolio" -> printPortfolio();
            case "history" -> printTransactionHistory(parameters);
            case "price" -> printStockPrice(parameters);
            case "buy" -> buyStock(parameters);
            case "sell" -> sellStock(parameters);
            case "help" -> printAvailableCommands();
            case "end" -> {
            }
            default -> System.out.println("Invalid operation.");
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

    private void printBalance() {
        printUsername();
        portfolio.printBalance();
    }

    private void printPortfolio() {
        printUsername();
        portfolio.print();
    }

    private void printTransactionHistory(String[] parameters) {
        printUsername();
        String numberOfTransactionsString = parameters[0];
        if (!isInteger(numberOfTransactionsString)) {
            System.out.println("Invalid parameter format - " + numberOfTransactionsString + " is not a number.");
            return;
        }
        int numberOfTransactions = Integer.parseInt(numberOfTransactionsString);
        transactionHistory.print(numberOfTransactions);
    }

    private void printUsername() {
        System.out.println("User: " + username);
    }

    private void printStockPrice(String[] parameters) {
        String companyCode = parameters[0];
        (new StockInformationPrinter()).printStockPrice(companyCode);
    }

    private void buyStock(String[] parameters) {
        String companyCode = parameters[0];
        String unitsString = parameters[1];
        if (!isInteger(unitsString)) {
            System.out.println("Invalid parameter format - " + unitsString + " is not a number.");
            return;
        }
        int units = Integer.parseInt(unitsString);
        (new PortfolioManager(portfolio, transactionHistory)).buyStock(companyCode, units);
    }

    private void sellStock(String[] parameters) {
        String companyCode = parameters[0];
        String unitsString = parameters[1];
        if (!isInteger(unitsString)) {
            System.out.println("Invalid parameter format - " + unitsString + " is not a number.");
            return;
        }
        int units = Integer.parseInt(unitsString);
        (new PortfolioManager(portfolio, transactionHistory)).sellStock(companyCode, units);
    }

    private void updateInDB() {
        portfolio.updateInDB(username);
        transactionHistory.updateInDB(username);
    }

    private boolean isInteger(String parameter) {
        boolean isInteger = false;
        try {
            Integer.parseInt(parameter);
            isInteger = true;
        } catch (NumberFormatException ignored) {
        }
        return isInteger;
    }

    private void printAvailableCommands() {
        System.out.println("Available commands: ");
        System.out.println("\"balance\" - Displays your balance.");
        System.out.println("\"portfolio\" - Displays content of your portfolio.");
        System.out.println("\"history <number_of_transactions>\" - Displays your transaction history.");
        System.out.println("\"price <company_code>\" - Displays the price of a given stock.");
        System.out.println("\"buy <company_code> <units_of_stock>\" - Buys certain amount of stocks of a given company.");
        System.out.println("\"sell <company_code> <units_of_stock>\" - Sells certain amount of stocks of a given company.");
    }
}
