package org.stock_game;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory {

    private final List<Transaction> transactionHistory;

    TransactionHistory() {
        transactionHistory = new ArrayList<>();
    }

    public void print(int numberOfTransactions) {
        int lastIndex = transactionHistory.size() - 1;
        System.out.println("_________________________________");
        for (int i = lastIndex; i > lastIndex - numberOfTransactions; i--) {
            transactionHistory.get(lastIndex).print();
        }
        System.out.println("_________________________________");
    }

    public void addTransaction(Transaction addedTransaction) {
        transactionHistory.add(addedTransaction);
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public int getAmountOfTransactions() {
        return transactionHistory.size();
    }

    public Transaction getLatestTransaction() {
        int lastTransactionIndex = transactionHistory.size() - 1;
        return transactionHistory.get(lastTransactionIndex);
    }

    public void updateInDB(String username) {
        (new DBUpdater(username)).updateTransactionHistory(this);
    }
}
