package org.stock_game;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        decideToLogInOrRegister();
    }

    public static void decideToLogInOrRegister() {
        System.out.println("Enter \"log in\" if you wish to log in");
        System.out.println("Enter \"register\" if you wish to register");
        Scanner scanner = new Scanner(System.in);
        String decision = scanner.nextLine();
        if (isDecisionValid(decision)) {
            System.out.println("Enter username");
            String username = scanner.nextLine();
            Authenticator authenticator = new Authenticator(username);
            if (decision.equals("log in")) {
                authenticator.logIn();
            }
            else {
                authenticator.register();
            }
        }
        else {
            System.out.println("Invalid command");
        }
    }

    private static boolean isDecisionValid(String decision) {
        return decision.equals("log in") || decision.equals("register");
    }
}