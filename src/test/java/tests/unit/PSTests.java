package tests.unit;

import api.PartnerProfileDao;
import com.codeborne.selenide.Configuration;
import modelsDB.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
    void partnerInfoTest() {
        String log = "lapina79@inbox.ru";
        String pas = "12032004lotos";

        PartnerProfileDao dao = new PartnerProfileDao();
        String dbName = dao.getPartnerNameByEmail(log);

        given()
                .relaxedHTTPSValidation()
                .queryParam("_token", getToken)
                .contentType("application/json")
                .body("{\"login\":\"" + log + "\",\"password\":\"" + pas + "\"}")
                .when()
                .post("/api/partner/login")
                .then()
                .statusCode(200);

        String apiName = given()
                .relaxedHTTPSValidation()
                .queryParam("_token", getToken)
                .contentType("application/json")
                .when()
                .get("/api/partner/info")
                .then()
                .statusCode(200)
                .extract()
                .path("data.partner_profile.name");
        assertEquals(dbName, apiName);
    }
}