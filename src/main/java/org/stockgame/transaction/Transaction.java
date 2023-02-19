package org.stockgame.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Transaction(String companyCode, int units, BigDecimal unitPrice, TransactionType type, LocalDate date) {

    public void print() {
        System.out.println(companyCode + "  |  " + units + "  |  " + unitPrice + "$  |  " + type + "  |  " + date);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Transaction)) {
            return false;
        }
        Transaction otherTransaction = (Transaction) other;
        return companyCode.equals(otherTransaction.companyCode) &&
                units == otherTransaction.units &&
                unitPrice.equals(otherTransaction.unitPrice) &&
                type.equals(otherTransaction.type) &&
                date.equals(otherTransaction.date);
    }
}
