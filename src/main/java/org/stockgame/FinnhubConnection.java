package org.stockgame;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class FinnhubConnection implements StockAPIConnection {

    private static final String API_KEY = "cficf51r01qq9nt20fjgcficf51r01qq9nt20fk0";
    private static final int HTTP_OK_STATUS_CODE = 200;

    @Override
    public BigDecimal getStockPriceByCompanyCode(String companyCode) throws StockAPIException {
        HttpResponse<String> response = sendRequestAndReturnResponse(constructRequest(companyCode));
        if (response.statusCode() != HTTP_OK_STATUS_CODE) {
            throw new StockAPIException("HTTP error. Status code: " + response.statusCode());
        }
        BigDecimal stockPrice = findLatestClosePrice(response.body());
        if (stockPrice.doubleValue() == 0) {
            throw new StockAPIException("Invalid company code.");
        }
        return stockPrice;
    }

    private String constructRequest(String companyCode) {
        return "https://finnhub.io/api/v1/quote?symbol=" + companyCode + "&token=" + API_KEY;
    }

    private HttpResponse<String> sendRequestAndReturnResponse(String request) throws StockAPIException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(request))
                    .build();
            return client.send(httpRequest, BodyHandlers.ofString());
        } catch (InterruptedException | URISyntaxException | IOException e) {
            throw new StockAPIException("Internal error.");
        }
    }

    private BigDecimal findLatestClosePrice(String responseBody) {
        // the first element of the split is {"c":<price>
        String priceString = responseBody.split(",")[0].substring(5);
        return new BigDecimal(priceString);
    }
}
