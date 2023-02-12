package org.stock_game;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        Authenticator authenticator = new Authenticator(username);
        authenticator.LogIn();
        User user = authenticator.getUser();
        user.play();
    }
}