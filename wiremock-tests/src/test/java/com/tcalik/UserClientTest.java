package com.tcalik;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

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
}