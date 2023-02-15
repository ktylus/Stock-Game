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
            System.out.println("Incorrect password");
            password = scanner.nextLine();
        }
        System.out.println("Logged in successfully");
        User user = getUserFromDB();
        user.play();
    }

    private boolean isPasswordCorrect(String password) {
        DAOPassword daoPassword = DAOPassword.getInstance();
        String passwordInDB = daoPassword.getPassword(username);
        return Integer.parseInt(passwordInDB) == password.hashCode();
    }

    public void register() {
        System.out.println("Enter password:");
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();

        String passwordSalt = SecurityUtilities.getSalt();
        String securePassword = SecurityUtilities.getSecurePassword(password, passwordSalt);
        try {
            (new DBUpdater(username)).registerAccount(securePassword, passwordSalt);
        } catch (SQLException e) {
            System.out.println("Encountered an error while trying to register the account");
            e.printStackTrace();
        }
        System.out.println("Account registered successfully");
    }

    private User getUserFromDB() {
        Portfolio portfolio = DAOPortfolio.getInstance().getPortfolio(username);
        TransactionHistory transactionHistory = DAOTransactionHistory.getInstance().getTransactionHistory(username);
        return new User(username, portfolio, transactionHistory);
    }
}
