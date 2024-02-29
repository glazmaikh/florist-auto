package tests.unit;

import api.PartnerProfileDao;
import com.codeborne.selenide.Configuration;
import config.BaseConfig;
import entityDB.BankEntity;
import entityDB.LegalEntity;
import entityDB.UserEntity;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PSTests {
    public static String getToken;
    public Long id;
    public PartnerProfileDao dao;
    public static String partnerLogin;
    public static String partnerPassword;

    @BeforeAll
    static void setBasic() throws IOException {
        String propertiesFilePath = "src/test/resources/test.properties";

        Properties properties = new Properties();
        properties.load(new FileInputStream(propertiesFilePath));

        BaseConfig config = ConfigFactory.create(BaseConfig.class, System.getProperties());
        partnerLogin = properties.getProperty("partner.login", config.getPartnerLogin());
        partnerPassword = properties.getProperty("partner.pass", config.getPartnerPass());

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

    @BeforeEach
    void setUp() {
        id = 5699L;
        dao = new PartnerProfileDao();

        given()
                .relaxedHTTPSValidation()
                .queryParam("_token", getToken)
                .contentType("application/json")
                .body("{\"login\":\"" + partnerLogin + "\",\"password\":\"" + partnerPassword + "\"}")
                .when()
                .post("/api/partner/login")
                .then()
                .statusCode(200);
    }

    @Test
    void partnerInfoTest() {
        UserEntity user = dao.getUserInfo(id);

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
        assertEquals(user.getName(), apiResponse.get("name"));
        assertEquals(user.getLocation(), apiResponse.get("location"));
        assertEquals(user.getTimezone(), apiResponse.get("timezone"));
        assertEquals(user.getCurrency(), apiResponse.get("currency"));
        assertEquals(user.getAgencyFee(), Long.valueOf(apiResponse.get("agency_fee").toString()));
    }

    @Test
    void partnerLegalTest() {
        LegalEntity legal = dao.getLegal(id);

        Map<String, Object> apiResponse = given()
                .relaxedHTTPSValidation()
                .queryParam("_token", getToken)
                .contentType("application/json")
                .when()
                .get("/api/partner/legal")
                .then()
                .statusCode(200)
                .extract()
                .path("data.partner_legal");

        assertEquals(legal.getHeadFullName(), apiResponse.get("head_fullname"));
        assertEquals(id, Long.valueOf(apiResponse.get("id").toString()));
        assertEquals(legal.getInn(), apiResponse.get("inn").toString());
        assertEquals(legal.getLegalAddress(), apiResponse.get("legal_address"));
        assertEquals(legal.getLegalName(), apiResponse.get("legalname"));
        assertEquals(legal.getOgrn(), apiResponse.get("ogrn"));
        assertEquals(legal.getPostAddress(), apiResponse.get("post_address"));
        assertEquals(legal.getRealAddress(), apiResponse.get("real_address"));
    }

    @Test
    void partnerBankTest() {
        BankEntity bank = dao.getBank(id);

        Map<String, Object> apiResponse = given()
                .relaxedHTTPSValidation()
                .queryParam("_token", getToken)
                .contentType("application/json")
                .when()
                .get("/api/partner/bank")
                .then()
                .statusCode(200)
                .extract()
                .path("data.partner_bank");

        assertEquals(bank.getBankAddress(), apiResponse.get("bank_address"));
        assertEquals(bank.getBankName(), apiResponse.get("bank_name"));
        assertEquals(bank.getBik(), apiResponse.get("bik"));
        assertEquals(id, Long.valueOf(apiResponse.get("id").toString()));
        assertEquals(bank.getKs(), apiResponse.get("ks"));
        assertEquals(bank.getRs(), apiResponse.get("rs"));
    }
}