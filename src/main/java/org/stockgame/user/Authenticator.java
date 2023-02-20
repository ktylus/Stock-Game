package org.stockgame.user;

import org.stockgame.dbaccess.DAOPassword;
import org.stockgame.dbaccess.DAOPortfolio;
import org.stockgame.dbaccess.DAOTransactionHistory;
import org.stockgame.dbaccess.DBUpdater;
import org.stockgame.portfolio.Portfolio;
import org.stockgame.transaction.TransactionHistory;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * <p>A class responsible logging the user in or
 * registering an account.</p>
 *
 * <p>After logging in successfully, all the user's
 * information is taken from the database and used to
 * instantiate the object of the User class.</p>
 */
public class Authenticator {

    private final String username;

    public Authenticator(String username) {
        this.username = username;
    }

    public void logIn() {
        System.out.println("Enter password:");
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();
        while (!isPasswordCorrect(password)) {
            System.err.println("Incorrect password.");
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
            System.err.println("Encountered an error while trying to register the account:");
            System.err.println(e.getMessage());
        }
    }

    private User getUserFromDB() {
        Portfolio portfolio = (new DAOPortfolio()).getPortfolio(username);
        TransactionHistory transactionHistory = (new DAOTransactionHistory()).getTransactionHistory(username);
        return new User(username, portfolio, transactionHistory);
    }
}
