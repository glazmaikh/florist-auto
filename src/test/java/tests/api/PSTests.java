package tests.api;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class PSTests extends TestBase {
    public static String baseUrl;
    private static String token;
    private static final String shortLink = "https://partner.k8s-dev.florist.local/t/KJ25jHNF";

    @BeforeAll
    static void setUp() throws IOException {
        Response response = given()
                .auth().basic("florist_api", "123")
                .when()
                .get(shortLink)
                .then()
                .statusCode(302)
                .extract().response();

        token  = response.header("Set-Cookie");
    }

    @Test
    void getTokenTest() {
        System.out.println(token);
    }
}