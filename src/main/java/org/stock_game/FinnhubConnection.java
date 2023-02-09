package org.stock_game;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FinnhubConnection implements StockAPIConnection{

    private final String API_KEY = "cficf51r01qq9nt20fjgcficf51r01qq9nt20fk0";

    @Override
    public BigDecimal getStockPriceByCompanyCode(String companyCode) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = sendRequestAndReturnResponse(constructRequest(companyCode));
        return findLatestClosePrice(response.body());
    }

    private String constructRequest(String companyCode) {
        return "https://finnhub.io/api/v1/quote?symbol=" +
                companyCode +
                "&token=" +
                API_KEY;
    }

    private HttpResponse<String> sendRequestAndReturnResponse(String request) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(request))
                .build();
        return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    private BigDecimal findLatestClosePrice(String responseBody) {
        // the first element of the split is {"c":<price>
        String priceString = responseBody.split(",")[0].substring(5);
        return new BigDecimal(priceString);
    }
}
