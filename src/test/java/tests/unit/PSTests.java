package tests.unit;

import api.PartnerProfileDao;
import com.codeborne.selenide.Configuration;
import modelsDB.PartnerProfile;
import modelsDB.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

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
        String login = "lapina79@inbox.ru";
        String password = "12032004lotos";
        Long id = 5699L;

        PartnerProfileDao dao = new PartnerProfileDao();
        PartnerProfile profile;
        profile = dao.getPartnerProfileByAccountId(id);

        given()
                .relaxedHTTPSValidation()
                .queryParam("_token", getToken)
                .contentType("application/json")
                .body("{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}")
                .when()
                .post("/api/partner/login")
                .then()
                .statusCode(200);

        Map<String, Object> apiResponse = given()
                .relaxedHTTPSValidation()
                .queryParam("_token", getToken)
                .contentType("application/json")
                .when()
                .get("/api/partner/info")
                .then()
                .statusCode(200)
                .extract()
                .path("data.partner_profile");

        assertEquals(id, Long.valueOf(apiResponse.get("id").toString()));
        assertEquals(profile.getName(), apiResponse.get("name"));
        assertEquals(profile.getLocation(), apiResponse.get("location"));
        assertEquals(profile.getTimezone(), apiResponse.get("timezone"));
        assertEquals(profile.getCurrency(), apiResponse.get("currency"));
        assertEquals(profile.getAgency_fee(), Long.valueOf(apiResponse.get("agency_fee").toString()));
    }
}