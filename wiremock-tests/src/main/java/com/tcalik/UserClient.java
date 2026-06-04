package com.tcalik;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UserClient {

    private final HttpClient httpClient;
    private final String baseUrl;

    public UserClient(String baseUrl) {
        this.httpClient = HttpClient.newHttpClient();
        this.baseUrl = baseUrl;
    }

    public String getUserById(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/users/" + id))
                .GET()
                .build();

        return httpClient
                .send(request, HttpResponse.BodyHandlers.ofString())
                .body();
    }

    public int createUser(String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/users"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return httpClient
                .send(request, HttpResponse.BodyHandlers.ofString())
                .statusCode();
    }
}