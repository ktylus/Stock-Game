package org.stockgame.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Transaction(String companyCode, int units, BigDecimal unitPrice, TransactionType type, LocalDate date) {

    public void print() {
        System.out.println(companyCode + "  |  " + units + "  |  " + unitPrice + "$  |  " + type + "  |  " + date);
    }
}
