package tests.api;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PSTests {
    public static String baseUrl;
    public static String getToken;

    @BeforeAll
    static void setUp() {
        Configuration.remote = "http://10.201.0.139:4444/wd/hub";
        baseURI = "https://partner.k8s-dev.florist.local";

        getToken = given()
                .relaxedHTTPSValidation()
                .auth().basic("florist_api", "123")
                .when()
                .get("/api/auth/partner")
                .then()
                .statusCode(200)
                .extract().path("data.token").toString();
    }

    @Test
    void afterLoginEmailTest() {
        String login = given()
                .relaxedHTTPSValidation()
                .queryParam("_token", getToken)
                .contentType("application/json")
                .body("{\"login\":\"lapina79@inbox.ru\",\"password\":\"12032004lotos\"}")
                .when()
                .post("/api/partner/login")
                .then()
                .statusCode(200)
                .extract()
                .path("data.login");
        assertEquals(login, "lapina79@inbox.ru");
    }
}