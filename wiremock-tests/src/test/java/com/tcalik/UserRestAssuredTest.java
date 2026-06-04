package com.tcalik;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@WireMockTest
class UserRestAssuredTest {

    @Test
    void shouldGetUser(WireMockRuntimeInfo wm) {
        stubFor(get(urlEqualTo("/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"name\":\"Tomasz\"}")));

        given()
                .baseUri(wm.getHttpBaseUrl())
                .when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Tomasz"));

        verify(getRequestedFor(urlEqualTo("/users/1")));
    }

    @Test
    void shouldCreateUser(WireMockRuntimeInfo wm) {

        stubFor(post(urlEqualTo("/users"))
                .withRequestBody(containing("Tomasz"))
                .willReturn(aResponse()
                        .withStatus(201)));

        String requestBody =
                "{\"id\":1,\"name\":\"Tomasz\"}";

        given()
                .baseUri(wm.getHttpBaseUrl())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                .statusCode(201);

        verify(postRequestedFor(urlEqualTo("/users"))
                .withRequestBody(containing("Tomasz")));
    }

    @Test
    void shouldGetUserUsingPathParam(WireMockRuntimeInfo wm) {
        stubFor(get(urlEqualTo("/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"name\":\"Tomasz\"}")));

        given()
                .baseUri(wm.getHttpBaseUrl())
                .pathParam("id", 1)
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Tomasz"));

        verify(getRequestedFor(urlEqualTo("/users/1")));
    }

    @Test
    void shouldGetUserUsingQueryParam(WireMockRuntimeInfo wm) {
        stubFor(get(urlPathEqualTo("/users"))
                .withQueryParam("id", com.github.tomakehurst.wiremock.client.WireMock.equalTo("1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"name\":\"Tomasz\"}")));

        given()
                .baseUri(wm.getHttpBaseUrl())
                .queryParam("id", 1)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Tomasz"));

        verify(getRequestedFor(urlPathEqualTo("/users"))
                .withQueryParam("id", com.github.tomakehurst.wiremock.client.WireMock.equalTo("1")));
    }

    @Test
    void shouldGetUserWithAuthorizationHeader(WireMockRuntimeInfo wm) {
        stubFor(get(urlEqualTo("/users/1"))
                .withHeader("Authorization", com.github.tomakehurst.wiremock.client.WireMock.equalTo("Bearer test-token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"name\":\"Tomasz\"}")));

        given()
                .baseUri(wm.getHttpBaseUrl())
                .header("Authorization", "Bearer test-token")
                .when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .body("name", equalTo("Tomasz"));

        verify(getRequestedFor(urlEqualTo("/users/1"))
                .withHeader("Authorization", com.github.tomakehurst.wiremock.client.WireMock.equalTo("Bearer test-token")));
    }

}