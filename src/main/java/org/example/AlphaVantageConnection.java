package org.example;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AlphaVantageConnection {

    private final String API_KEY = "YBSKDVEH4XRPVHJ2";
    private final int HTTP_OK_CODE = 200;

    public BigDecimal getStockPriceByCompanyCode(String companyCode) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = sendRequestAndReturnResponse(constructRequest(companyCode));
        return findLatestClosePrice(response.body());
    }

    private String constructRequest(String companyCode) {
        return "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" +
                companyCode +
                "&interval=5min&apikey=" +
                API_KEY;
    }

    private HttpResponse<String> sendRequestAndReturnResponse(String request) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(request))
                .build();
        return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    private BigDecimal findLatestClosePrice(String response) {
        int searchedLineFirstIndex = response.indexOf("\"4. close\"");
        int nextLineFirstIndex = response.indexOf("\"5. volume\"");
        String searchedLine = response.substring(searchedLineFirstIndex, nextLineFirstIndex);
        String[] splitLine = searchedLine.split("\"");
        String closePrice = splitLine[splitLine.length - 2];

        return new BigDecimal(closePrice.substring(0, closePrice.length() - 2));
    }

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        AlphaVantageConnection c = new AlphaVantageConnection();
        System.out.println(c.findLatestClosePrice(c.sendRequestAndReturnResponse(c.constructRequest("IBM")).body()));
    }
}
