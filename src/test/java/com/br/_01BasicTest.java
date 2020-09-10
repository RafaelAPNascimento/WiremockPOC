package com.br;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class _01BasicTest {

    private static WireMockServer wireMockServer = new WireMockServer(8989);

    @BeforeClass
    public static void init() throws JsonProcessingException {
        wireMockServer.start();
        configureFor("localhost", 8989);
        stubFor(get(urlEqualTo("/maria")).willReturn(aResponse().withStatus(200).withBody("Welcome to Maria!")));
        stubFor(get(urlEqualTo("/rafael")).willReturn(aResponse().withStatus(201).withBody("Welcome Rafael!")));
        stubFor(post(urlEqualTo("/ohmy")).willReturn(aResponse().withStatus(500).withBody("Erro 500")));
    }

    @AfterClass
    public static void end() {
        if (wireMockServer != null && wireMockServer.isRunning())
            wireMockServer.shutdown();
    }

    @Test
    public void test() {

        final String URI =  "http://localhost:8989";

        given().baseUri(URI).pathParam("welcome", "maria")
                .request()
                .when()
                .get("/{welcome}")
                .peek()
                .then().assertThat().statusCode(HttpStatus.SC_OK);

        given().baseUri(URI).pathParam("welcome", "rafael")
                .request()
                .when()
                .get("/{welcome}")
                .peek()
                .then().assertThat().statusCode(HttpStatus.SC_CREATED);

        given().baseUri(URI).basePath("ohmy")
                .request()
                .when()
                .post()
                .peek()
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
