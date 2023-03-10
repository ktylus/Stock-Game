package org.stockgame.transaction;

import org.stockgame.dbaccess.DBUpdater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class responsible for keeping track of the
 * transactions made.
 */
public class TransactionHistory {

    private final List<Transaction> transactionHistory;

    public TransactionHistory() {
        transactionHistory = new ArrayList<>();
    }

    public void print(int numberOfTransactions) {
        int lastIndex = transactionHistory.size() - 1;
        System.out.println("_________________________________");
        for (int i = lastIndex; i > lastIndex - numberOfTransactions && i >= 0; i--) {
            transactionHistory.get(i).print();
        }
        System.out.println("_________________________________");
    }

    public void addTransaction(Transaction addedTransaction) {
        transactionHistory.add(addedTransaction);
    }

    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }

    public int getNumberOfTransactions() {
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
