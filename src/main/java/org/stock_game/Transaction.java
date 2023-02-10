package org.stock_game;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Transaction(String stock, int units, BigDecimal unitPrice, TransactionType type, LocalDate date) {

    public void display() {
        System.out.println(stock + "  |  " + units + "  |  " + unitPrice + "  |  " + type + "  |  " + date);
    }
}
