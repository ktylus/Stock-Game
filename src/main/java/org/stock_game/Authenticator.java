package org.stock_game;

import java.sql.SQLException;
import java.util.Scanner;

public class Authenticator {

    private final String username;

    Authenticator(String username) {
        this.username = username;
    }

    public void logIn() {
        System.out.println("Enter password:");
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();
        while (!isPasswordCorrect(password)) {
            System.out.println("Incorrect password.");
            password = scanner.nextLine();
        }
        System.out.println("Logged in successfully.");
        User user = getUserFromDB();
        user.play();
    }

    private boolean isPasswordCorrect(String password) {
        String passwordInDB = (new DAOPassword()).getPassword(username);
        String passwordSalt = (new DAOPassword()).getPasswordSalt(username);
        String expectedPassword = SecurityUtilities.getHashedPassword(password, passwordSalt);
        return passwordInDB.equals(expectedPassword);
    }

    public void register() {
        System.out.println("Enter password:");
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();
        String passwordSalt = SecurityUtilities.getSalt();
        String hashedPassword = SecurityUtilities.getHashedPassword(password, passwordSalt);
        try {
            (new DBUpdater(username)).registerAccount(hashedPassword, passwordSalt);
            System.out.println("Account registered successfully.");
        } catch (SQLException e) {
            System.out.println("Encountered an error while trying to register the account:");
            System.out.println(e.getMessage());
        }
    }

    private User getUserFromDB() {
        Portfolio portfolio = (new DAOPortfolio()).getPortfolio(username);
        TransactionHistory transactionHistory = (new DAOTransactionHistory()).getTransactionHistory(username);
        return new User(username, portfolio, transactionHistory);
    }
}
