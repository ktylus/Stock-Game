package org.stock_game;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory {

    private List<Transaction> transactionHistory;

    TransactionHistory() {
        transactionHistory = new ArrayList<>();
    }

    public void display(int numberOfTransactions) {
        int lastIndex = transactionHistory.size() - 1;
        System.out.println("_________________________________");
        for (int i = lastIndex; i > lastIndex - numberOfTransactions; i--) {
            System.out.println(transactionHistory.get(lastIndex).display());
        }
        System.out.println("_________________________________");
    }
}
