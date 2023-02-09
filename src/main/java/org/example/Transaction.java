package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Transaction(String stock, int units, BigDecimal unitPrice, TransactionType type, LocalDate date) {

    public String display() {
        return stock + "  |  " + units + "  |  " + unitPrice + "  |  " + type + "  |  " + date;
    }
}
