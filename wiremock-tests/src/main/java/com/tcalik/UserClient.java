package com.tcalik;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserClient implements UserService {

    private final HttpClient httpClient;
    private final String baseUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger log =
            LoggerFactory.getLogger(UserClient.class);



    public UserClient(String baseUrl) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();
        this.baseUrl = baseUrl;
    }

    public User getUserOrThrow(int id) throws IOException, InterruptedException {
        ApiResponse response = getUserResponseById(id);

        if (response.getStatusCode() == 404) {
            log.warn("User with id {} was not found", id);
            throw new UserNotFoundException(id);
        }

        if (response.getStatusCode() >= 500) {
            log.error("External API returned server error: {}", response.getStatusCode());
            throw new ApiException("External API error: " + response.getStatusCode());
        }

        return objectMapper.readValue(response.getBody(), User.class);
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

    public ApiResponse getUserResponseById(int id) throws IOException, InterruptedException {
        String url = baseUrl + "/users/" + id;

        log.info("Calling user API: {}", url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(1))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        log.info("Received response with status code: {}", response.statusCode());

        return new ApiResponse(response.statusCode(), response.body());
    }

    @Override
    public User getUserObjectById(int id) throws IOException, InterruptedException {
        String responseBody = getUserById(id);

        return objectMapper.readValue(responseBody, User.class);
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

    public ApiResponse getUserWithToken(int id, String token) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/users/" + id))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        return new ApiResponse(response.statusCode(), response.body());
    }

    public ApiResponse getUserByQueryParam(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/users?id=" + id))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        return new ApiResponse(response.statusCode(), response.body());
    }


}