package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;
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
            System.out.println(transactionHistory.get(lastIndex).toString());
        }
        System.out.println("_________________________________");
    }

    public static void main(String[] args) {
        TransactionHistory th = new TransactionHistory();
        Transaction t = new Transaction("IBM", 5, new BigDecimal("100.00"), TransactionType.PURCHASE, LocalDate.now());
        th.transactionHistory.add(t);
        th.display(1);
    }
}
