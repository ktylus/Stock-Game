package org.stock_game;

public record StockInPortfolio(String companyCode, int units) {

    public String getCompanyCode() {
        return companyCode;
    }

    public int getUnits() {
        return units;
    }
}
