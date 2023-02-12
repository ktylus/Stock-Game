package org.stock_game;

import java.util.Scanner;

public class Authenticator {

    private final String username;
    private User user;

    Authenticator(String username) {
        this.username = username;
    }

    public void LogIn() {
        System.out.println("Enter password:");
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();
        while (!isPasswordCorrect(password)) {
            System.out.println("Incorrect password");
            password = scanner.nextLine();
        }
        System.out.println("Logged in successfully");
        user = getUserFromDB();
    }

    private boolean isPasswordCorrect(String password) {
        DAOPassword daoPassword = DAOPassword.getInstance();
        String passwordInDB = daoPassword.getPassword(username);
        return Integer.parseInt(passwordInDB) == password.hashCode();
    }

    private User getUserFromDB() {

    }

    public User getUser() {
        return user;
    }
}
