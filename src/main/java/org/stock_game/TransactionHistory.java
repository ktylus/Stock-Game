package org.stock_game;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory {

    private final List<Transaction> transactionHistory;

    TransactionHistory() {
        transactionHistory = new ArrayList<>();
    }

    public void display(int numberOfTransactions) {
        int lastIndex = transactionHistory.size() - 1;
        System.out.println("_________________________________");
        for (int i = lastIndex; i > lastIndex - numberOfTransactions; i--) {
            transactionHistory.get(lastIndex).display();
        }
        System.out.println("_________________________________");
    }

    public int getAmountOfTransactions() {
        return transactionHistory.size();
    }

    public void addTransaction(Transaction addedTransaction) {
        transactionHistory.add(addedTransaction);
    }

    public Transaction getLatestTransaction() {
        int lastTransactionIndex = transactionHistory.size() - 1;
        return transactionHistory.get(lastTransactionIndex);
    }
}
