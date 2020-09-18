package other;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class _03_Test extends AbstractServiceTest{


//    @Before
//    public void init() throws JsonProcessingException {
//
//    }

    @Test
    public void authRequestTest() {

        Response response =  given().baseUri(String.format("http://%s:%s", HOST, PORT))
                .basePath(TOKEN_URI)
                .contentType(ContentType.JSON)
                .request().body("{ \"password\": \"123456\", \"scope\": \"CONSULTA_UNIDADE_TRANSITO\", \"username\": \"totem\" }")
                .when().post().peek();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        response.peek();
    }

    @Test
    public void authRequestTest02() {
        String endpoint = String.format("http://%s:%s", HOST, PORT);

        Client client = new ResteasyClientBuilder().build();
        WebTarget target = client.target(endpoint + TOKEN_URI);
        javax.ws.rs.core.Response r = target.request(MediaType.APPLICATION_JSON)
                .post(Entity.entity("{ \"password\": \"123456\", \"scope\": \"CONSULTA_UNIDADE_TRANSITO\", \"username\": \"totem\" }", MediaType.APPLICATION_JSON));


        System.out.println("============= \n"+r.getStatus());

        assertTrue(r.getStatus() == 200);
    }

}
