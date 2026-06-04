package com.tcalik;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest
class WireMockFirstTest {

    @Test
    void shouldReturnMockedUser(WireMockRuntimeInfo wm) throws Exception {
        stubFor(get(urlEqualTo("/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"name\":\"Tomasz\"}")));

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(wm.getHttpBaseUrl() + "/users/1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("Tomasz");

        verify(getRequestedFor(urlEqualTo("/users/1")));
    }

    @Test
    void shouldReturnUser(WireMockRuntimeInfo wm) throws Exception {

        stubFor(get(urlEqualTo("/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"name\":\"Tomasz\"}")));

        UserClient client = new UserClient(wm.getHttpBaseUrl());

        String response = client.getUserById(1);

        assertThat(response).contains("Tomasz");

        verify(getRequestedFor(urlEqualTo("/users/1")));
    }

    @Test
    void shouldReturn404(WireMockRuntimeInfo wm) throws Exception {

        stubFor(get(urlEqualTo("/users/999"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("User not found")));

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(wm.getHttpBaseUrl() + "/users/999"))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request,
                        HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(404);
        assertThat(response.body()).contains("User not found");
    }

    @Test
    void shouldSimulateSlowApi(WireMockRuntimeInfo wm) throws Exception {

        stubFor(get(urlEqualTo("/users/1"))
                .willReturn(aResponse()
                        .withFixedDelay(3000)
                        .withStatus(200)));

        HttpClient client = HttpClient.newHttpClient();

        long start = System.currentTimeMillis();

        HttpResponse<String> response =
                client.send(
                        HttpRequest.newBuilder()
                                .uri(URI.create(wm.getHttpBaseUrl() + "/users/1"))
                                .GET()
                                .build(),
                        HttpResponse.BodyHandlers.ofString());

        long duration = System.currentTimeMillis() - start;

        assertThat(duration).isGreaterThanOrEqualTo(3000);
    }
}