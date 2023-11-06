package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ArithmeticClient {

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        makeRequest("http://localhost:8080/add?first=23&second=56");
        makeRequest("http://localhost:8080/subtract?first=11&second=5");
        makeRequest("http://localhost:8080/multiply?first=23&second=12");
        makeRequest("http://localhost:8080/divide?first=10&second=5");
        makeRequest("http://localhost:8080/divide?first=cartof&second=5");
    }

    private static void makeRequest(String url) throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        String responseBody = response.body();

        if (statusCode == 200) {
            System.out.println("Success: " + responseBody);
        } else if (statusCode == 400) {
            System.out.println("Error: " + responseBody);
        } else {
            System.out.println("Unexpected response with status code: " + statusCode);
        }
    }
}
