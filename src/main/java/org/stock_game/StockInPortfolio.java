package org.stock_game;

public class StockInPortfolio {

    private final String companyCode;
    private final int units;

    StockInPortfolio(String companyCode, int units) {
        this.companyCode = companyCode;
        this.units = units;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public int getUnits() {
        return units;
    }
}
