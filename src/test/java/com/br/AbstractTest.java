package com.br;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public abstract class AbstractTest {

    public static final String HOST = "localhost";
    public static final int PORT = 8989;

    public static final String TOKEN_URI = "/SevConsulta/rest/v1/auth";
    public static final String COMPRADOR = "/SevConsulta/rest/comprador/documento/[\\d]+/cep/[\\d]+/numero/[\\d]+";

    protected static WireMockServer wireMockServer = new WireMockServer(PORT);

    @BeforeClass
    public static void before() throws Exception {

        wireMockServer.start();
        configureFor(HOST, PORT);
        setStubForAuth();
        setStubRequest();
    }

    @AfterClass
    public static void after() {

        if (wireMockServer != null && wireMockServer.isRunning())
            wireMockServer.shutdown();
    }

    protected static void setStubForAuth() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode tokenRepsonse = mapper.createObjectNode();
        ((ObjectNode) tokenRepsonse).put("access_token", "eyJhbGciOiJIUzI1NiJ9.eyJhdXRvcml6YWNhbyI6NSwic2NvcGUiOiJDT05GSVJNQVJfRU5ERVJFQ09fQ09NUFJBRE9SIiwiaWRVc3VhcmlvIjoiMjIiLCJleHBpcmF0aW9uIjoxNTk5NjY2Mzk4MzQ2fQ.yFRqSvJu1RijQGWC68Bdnckddmjh2-j1rSIAjNzZogo");
        ((ObjectNode) tokenRepsonse).put("token_type", "Bearer");
        ((ObjectNode) tokenRepsonse).put("expires_in", 2400);
        ((ObjectNode) tokenRepsonse).put("scope", "CONFIRMAR_ENDERECO_COMPRADOR");

        stubFor(
                post(urlEqualTo(TOKEN_URI))
                .withRequestBody(equalToJson("{ \"password\": \"[\\d]+\", \"scope\": \"CONFIRMAR_ENDERECO_COMPRADOR\", \"username\": \"totem\" }"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(tokenRepsonse))));
    }

    protected static void setStubRequest() {

        stubFor(
                get(urlPathMatching(COMPRADOR))
                        .withHeader("Accept", containing("*"))
                        .withHeader("Authorization", equalTo("Bearer ababababa"))
                        .willReturn(aResponse().withStatus(200)));
    }

}
