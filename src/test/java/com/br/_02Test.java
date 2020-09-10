package com.br;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class _02Test extends AbstractTest {

    @Test
    public void authRequestTest() {

        Response response =  given().baseUri(String.format("http://%s:%s", HOST, PORT))
                .basePath(TOKEN_URI)
                .request().body("{ \"password\": \"123456\", \"scope\": \"CONFIRMAR_ENDERECO_COMPRADOR\", \"username\": \"totem\" }")
                .when().post().peek();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        response.peek();
    }

    @Test
    public void requestWithTokenTest() {

        given().baseUri(String.format("http://%s:%s", HOST, PORT))
                .basePath("/SevConsulta/rest")
                .pathParam("cpfcnpj", "11111")
                .pathParam("cep", "11111")
                .pathParam("numero", "11111")
                .headers(HttpHeaders.AUTHORIZATION, "Bearer ababababa")
                .contentType(ContentType.JSON)
                .request()
                .when().get("/comprador/documento/{cpfcnpj}/cep/{cep}/numero/{numero}")
                .peek()
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK);
    }
}
