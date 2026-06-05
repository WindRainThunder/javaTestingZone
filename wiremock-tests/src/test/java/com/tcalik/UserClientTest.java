package com.tcalik;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest
class UserClientTest {

    @Test
    void shouldGetUserById(WireMockRuntimeInfo wm) throws Exception {
        stubFor(get(urlEqualTo("/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"name\":\"Tomasz\"}")));

        UserClient client = new UserClient(wm.getHttpBaseUrl());

        String response = client.getUserById(1);

        assertThat(response).contains("Tomasz");

        verify(getRequestedFor(urlEqualTo("/users/1")));
    }

    @Test
    void shouldCreateUser(WireMockRuntimeInfo wm) throws Exception {
        stubFor(post(urlEqualTo("/users"))
                .withHeader("Content-Type", containing("application/json"))
                .withRequestBody(containing("Tomasz"))
                .willReturn(aResponse()
                        .withStatus(201)));

        UserClient client = new UserClient(wm.getHttpBaseUrl());

        int statusCode = client.createUser("{\"name\":\"Tomasz\"}");

        assertThat(statusCode).isEqualTo(201);

        verify(postRequestedFor(urlEqualTo("/users"))
                .withRequestBody(containing("Tomasz")));
    }

    @Test
    void shouldReturn404WhenUserDoesNotExist(WireMockRuntimeInfo wm) throws Exception {
        stubFor(get(urlEqualTo("/users/999"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("User not found")));

        UserClient client = new UserClient(wm.getHttpBaseUrl());

        ApiResponse response = client.getUserResponseById(999);

        assertThat(response.getStatusCode()).isEqualTo(404);
        assertThat(response.getBody()).isEqualTo("User not found");

        verify(getRequestedFor(urlEqualTo("/users/999")));
    }


    @Test
    void shouldReturn500WhenServerHasError(WireMockRuntimeInfo wm) throws Exception {
        stubFor(get(urlEqualTo("/users/1"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody("Internal Server Error")));

        UserClient client = new UserClient(wm.getHttpBaseUrl());

        ApiResponse response = client.getUserResponseById(1);

        assertThat(response.getStatusCode()).isEqualTo(500);
        assertThat(response.getBody()).contains("Internal Server Error");

        verify(getRequestedFor(urlEqualTo("/users/1")));
    }

    @Test
    void shouldSimulateSlowResponse(WireMockRuntimeInfo wm) throws Exception {

        stubFor(get(urlEqualTo("/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withFixedDelay(3000)));

        long start = System.currentTimeMillis();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(wm.getHttpBaseUrl() + "/users/1"))
                .GET()
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());

        long duration = System.currentTimeMillis() - start;

        assertThat(duration).isGreaterThanOrEqualTo(3000);
    }

    @Test
    void shouldThrowTimeoutWhenApiRespondsTooSlowly(WireMockRuntimeInfo wm) {
        stubFor(get(urlEqualTo("/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withFixedDelay(3000)
                        .withBody("{\"id\":1,\"name\":\"Tomasz\"}")));

        UserClient client = new UserClient(wm.getHttpBaseUrl());

        assertThatThrownBy(() -> client.getUserResponseById(1))
                .isInstanceOf(HttpTimeoutException.class);

        verify(getRequestedFor(urlEqualTo("/users/1")));
    }

    @Test
    void shouldMapUserJsonToUserObject(WireMockRuntimeInfo wm) throws Exception {
        stubFor(get(urlEqualTo("/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"name\":\"Tomasz\"}")));

        UserClient client = new UserClient(wm.getHttpBaseUrl());

        User user = client.getUserObjectById(1);

        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getName()).isEqualTo("Tomasz");

        verify(getRequestedFor(urlEqualTo("/users/1")));
    }

    @Test
    void shouldSendAuthorizationHeader(WireMockRuntimeInfo wm) throws Exception {
        stubFor(get(urlEqualTo("/users/1"))
                .withHeader("Authorization", equalTo("Bearer test-token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"id\":1,\"name\":\"Tomasz\"}")));

        UserClient client = new UserClient(wm.getHttpBaseUrl());

        ApiResponse response = client.getUserWithToken(1, "test-token");

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody()).contains("Tomasz");

        verify(getRequestedFor(urlEqualTo("/users/1"))
                .withHeader("Authorization", equalTo("Bearer test-token")));
    }

    @Test
    void shouldSendQueryParameter(WireMockRuntimeInfo wm) throws Exception {
        stubFor(get(urlPathEqualTo("/users"))
                .withQueryParam("id", equalTo("1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"id\":1,\"name\":\"Tomasz\"}")));

        UserClient client = new UserClient(wm.getHttpBaseUrl());

        ApiResponse response = client.getUserByQueryParam(1);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody()).contains("Tomasz");

        verify(getRequestedFor(urlPathEqualTo("/users"))
                .withQueryParam("id", equalTo("1")));
    }

    @Test
    void shouldUsePolymorphism() throws Exception {

        UserService service =
                new MockUserService();

        User user =
                service.getUserObjectById(1);

        assertThat(user.getName())
                .isEqualTo("Mock User");
    }

    @Test
    void shouldUseMockUserServiceThroughInterface() throws Exception {
        UserService service = new MockUserService();

        User user = service.getUserObjectById(1);

        assertThat(user.getId()).isEqualTo(999);
        assertThat(user.getName()).isEqualTo("Mock User");
    }

    @Test
    void shouldCreateUserUsingBuilder() {

        User user = User.builder()
                .id(1)
                .name("Tomasz")
                .build();

        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getName()).isEqualTo("Tomasz");
    }

    @Test
    void shouldCreateMockServiceUsingFactory() throws Exception {

        UserService service =
                UserServiceFactory.createMockService();

        User user =
                service.getUserObjectById(1);

        assertThat(user.getName())
                .isEqualTo("Mock User");
    }
}