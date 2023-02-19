package org.stock_game;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class TestUtilities {

    public static final String INVALID_COMPANY_CODE = "XXXXXXXXXXXXXX";
    public static final String TEST_DATABASE = "test_stock_game";
    public static final String TEST_USER = "test_user";
    public static final String NONEXISTENT_USER = "nonexistent_user";

    public static boolean isTransactionCorrect(
            Transaction transaction,
            String expectedCompanyCode,
            int expectedUnits,
            BigDecimal expectedUnitPrice,
            TransactionType expectedTransactionType,
            LocalDate expectedDate) {
        return transaction.companyCode().equals(expectedCompanyCode) &&
                transaction.units() == expectedUnits &&
                transaction.unitPrice().equals(expectedUnitPrice) &&
                transaction.type().equals(expectedTransactionType) &&
                transaction.date().equals(expectedDate);
    }

    public static boolean isStockInPortfolioCorrect(
            StockInPortfolio stock,
            String expectedCompanyCode,
            int expectedUnits) {
        return stock.companyCode().equals(expectedCompanyCode) &&
                stock.units() == expectedUnits;
    }

    private TestUtilities() {
    }
}
