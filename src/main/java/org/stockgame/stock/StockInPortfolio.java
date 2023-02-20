package org.stockgame.stock;

/**
 * A record representing a stock that is
 * a part of the user's portfolio.
 *
 * @param companyCode
 * @param units
 */
public record StockInPortfolio(String companyCode, int units) {

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StockInPortfolio)) {
            return false;
        }
        StockInPortfolio otherStock = (StockInPortfolio) other;
        return companyCode.equals(otherStock.companyCode) &&
                units == otherStock.units;
    }
}
