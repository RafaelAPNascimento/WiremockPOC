package other;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public abstract class AbstractServiceTest {

    public static final int PORT = 8989;
    public static final String HOST = "localhost";
    public static final String TOKEN_URI = "/eaigefor/v1/auth";

    //public static final String CONSULTA_UNIDADE_TRANSITO = "/eaigefor/consulta";

    protected static WireMockServer wireMockServer = new WireMockServer(PORT);

    @BeforeClass
    public static void before() throws Exception {
        wireMockServer.start();
        configureFor(HOST, PORT);
        createMockUriForToken();
        createMockUriForConsultaUnidadeTransito();
    }

    @AfterClass
    public static void after() {

        if (wireMockServer != null && wireMockServer.isRunning())
            wireMockServer.shutdown();
    }

    protected static void createMockUriForToken() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode mockResponse = mapper.createObjectNode();
        ((ObjectNode) mockResponse).put("access_token", "eyJhbGciOiJIUzI1NiJ9.eyJhdXRvcml6YWNhbyI6MzIsInNjb3BlIjoiQ09OU1VMVEFfVU5JREFERV9UUkFOU0lUTyIsImlkVXN1YXJpbyI6IjQiLCJjYW5hbE9wZXJhZG9yIjoiOTE5NCIsImV4cGlyYXRpb24iOjE2MDAyMDExNDA3NTQsImNucGoiOiI2MjU3NzkyOTAwMDEzNSIsImNhbmFsIjoiMyJ9.qVmt8uR7pJvtcVKoqABxi-LQQGeaDGPjvrQ8SwKncP0");
        ((ObjectNode) mockResponse).put("token_type", "Bearer");
        ((ObjectNode) mockResponse).put("expires_in", 2400);
        ((ObjectNode) mockResponse).put("scope", "CONSULTA_UNIDADE_TRANSITO");

        stubFor(
                post(urlEqualTo(TOKEN_URI))
                        .withHeader("Content-Type", containing("application/json"))
                        .withHeader("Accept", containing("application/json"))
                        .withRequestBody(equalToJson ("{ \"password\": \"123456\", \"scope\": \"CONSULTA_UNIDADE_TRANSITO\", \"username\": \"totem\" }"))
                        .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(mockResponse))));
    }

    protected static void createMockUriForConsultaUnidadeTransito() throws JsonProcessingException {

//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode mockResponse = mapper.createObjectNode();
//        ((ObjectNode) mockResponse).put("nomeMunicipio", "GUATAPARA");
//        ((ObjectNode) mockResponse).put("codigoUnidadeTransito", "15");
//        ((ObjectNode) mockResponse).put( "nomeUnidadeTransito", "CIR-RIBEIRAO PRETO");
//
//        String mockPath = HOST + CONSULTA_UNIDADE_TRANSITO + "/[\\w]/[\\d]";
//
//        stubFor(
//                get(urlPathMatching(mockPath))
//                        .withHeader("Accept", containing("*"))
//                        .withHeader("Authorization", equalTo("Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdXRvcml6YWNhbyI6MzIsInNjb3BlIjoiQ09OU1VMVEFfVU5JREFERV9UUkFOU0lUTyIsImlkVXN1YXJpbyI6IjQiLCJjYW5hbE9wZXJhZG9yIjoiOTE5NCIsImV4cGlyYXRpb24iOjE2MDAyMDExNDA3NTQsImNucGoiOiI2MjU3NzkyOTAwMDEzNSIsImNhbmFsIjoiMyJ9.qVmt8uR7pJvtcVKoqABxi-LQQGeaDGPjvrQ8SwKncP0"))
//                        .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(mockResponse))));
    }
}