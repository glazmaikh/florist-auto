package tests.unit;

import api.PartnerProfileDao;
import com.codeborne.selenide.Configuration;
import config.BaseConfig;
import entityDB.BankEntity;
import entityDB.LegalEntity;
import entityDB.UserEntity;
import entityDB.WorkTimeEntity;
import helpers.HelperPage;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("api")
public class PSTests {
    public static String getToken;
    public Long id;
    public PartnerProfileDao dao;
    public static String partnerLogin;
    public static String partnerPassword;

    //https://partner.k8s-dev.florist.local/t/VSt9dLSn

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

    @Test
    void createWorkTimeTest() {
        Object[] fromValues = {1.25, 2.50, 3.75, 4.00, 11.00, 0.00, null};
        Object[] toValues = {11.75, 12.50, 13.75, 14.00, 22.00, 24.00, null};
        Object[] workdayValues = {true, true, true, true, true, true, false};
        Object[] aroundTheClockValues = {false, false, false, false, false, true, false};

        String jsonData = createWorkTimeBuildJson(fromValues, toValues, workdayValues, aroundTheClockValues);

        Map<String, Map<String, Object>> apiResponse = given()
                .relaxedHTTPSValidation()
                .queryParam("_token", getToken)
                .contentType("application/json")
                .body(jsonData)
                .when()
                .post("/api/partner/createWorkTime")
                .then()
                .statusCode(200)
                .extract()
                .path("data.days");

        List<WorkTimeEntity> time = dao.getWorkTime(id);

        for (WorkTimeEntity workTime : time) {
            int weekday = workTime.getWeekday();
            Map<String, Object> dayInfo = apiResponse.get(String.valueOf(weekday - 1));

            BigDecimal from = workTime.getFromTime();
            BigDecimal to = workTime.getToTime();
            int workday = workTime.getWorkday();
            int aroundTheClock = workTime.getAroundTheClock();

            Object fromApi = dayInfo.get("from");
            Object toApi = dayInfo.get("to");

            String formattedFrom = from != null ? from.toString() : null;
            String formattedTo = to != null ? to.toString() : null;

            assertEquals(formattedFrom, HelperPage.formatFromApi(fromApi));
            assertEquals(formattedTo, HelperPage.formatFromApi(toApi));
            assertEquals(workday, (boolean) dayInfo.get("workday") ? 1 : 0);
            assertEquals(aroundTheClock, (boolean) dayInfo.get("around_the_clock") ? 1 : 0);
        }
    }

    @Test
    void partnerWorkTimeTest() {
        List<WorkTimeEntity> time = dao.getWorkTime(id);

        Map<String, Map<String, Object>> apiResponse = given()
                .relaxedHTTPSValidation()
                .queryParam("_token", getToken)
                .contentType("application/json")
                .when()
                .get("/api/partner/workTime")
                .then()
                .statusCode(200)
                .extract()
                .path("data.days");

        for (WorkTimeEntity workTime : time) {
            int weekday = workTime.getWeekday();
            Map<String, Object> dayInfo = apiResponse.get(String.valueOf(weekday - 1));

            BigDecimal from = workTime.getFromTime();
            BigDecimal to = workTime.getToTime();
            int workday = workTime.getWorkday();
            int aroundTheClock = workTime.getAroundTheClock();

            Object fromApi = dayInfo.get("from");
            Object toApi = dayInfo.get("to");

            String formattedFrom = from != null ? from.toString() : null;
            String formattedTo = from != null ? to.toString() : null;

            assertEquals(formattedFrom, HelperPage.formatFromApi(fromApi));
            assertEquals(formattedTo, HelperPage.formatFromApi(toApi));
            assertEquals(workday, (boolean) dayInfo.get("workday") ? 1 : 0);
            assertEquals(aroundTheClock, (boolean) dayInfo.get("around_the_clock") ? 1 : 0);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "1.11, 2.22",
            "20.21, 23.99",
            "00.01, 01.01"
    })
    void validRangeOfParamCreateWorkTimeNegativeTest(double from, double to) {
        Object[] fromValues = {from, from, from, 4.00, 2.50, 3.75, null};
        Object[] toValues = {to, to, to, 13.25, 13.75, 14.00, null};
        Object[] workdayValues = {true, true, true, true, true, true, false};
        Object[] aroundTheClockValues = {false, false, false, false, false, true, false};

        String jsonData = createWorkTimeBuildJson(fromValues, toValues, workdayValues, aroundTheClockValues);

        given()
                .relaxedHTTPSValidation()
                .queryParam("_token", getToken)
                .contentType("application/json")
                .body(jsonData)
                .when()
                .post("/api/partner/createWorkTime")
                .then()
                .statusCode(200);
    }

    // statusCode(200) - нужно 400
    @ParameterizedTest
    @CsvSource({
            "25.101, 39.299"
    })
    void invalidRangeOfParamCreateWorkTimeNegativeTest(double from, double to) {
        Object[] fromValues = {from, 4.00, 4.00, 4.00, 2.50, 3.75, null};
        Object[] toValues = {to, 13.25, 13.25, 13.25, 13.75, 14.00, null};
        Object[] workdayValues = {true, true, true, true, true, true, false};
        Object[] aroundTheClockValues = {false, false, false, false, false, true, false};

        String jsonData = createWorkTimeBuildJson(fromValues, toValues, workdayValues, aroundTheClockValues);

        given()
                .relaxedHTTPSValidation()
                .queryParam("_token", getToken)
                .contentType("application/json")
                .body(jsonData)
                .when()
                .post("/api/partner/createWorkTime")
                .then()
                .statusCode(200);
    }

    // в statusCode(200) - нужен статус 400
    @ParameterizedTest
    @ValueSource(strings = {"", "asd", "~"})
    void invalidParamCreateWorkTimeTest(String param) {
        Object[] fromValues = {param, 2.50, 3.75, 4.00, param, param, null};
        Object[] toValues = {11.75, param, 13.75, 14.00, param, param, null};
        Object[] workdayValues = {true, true, param, true, true, param, false};
        Object[] aroundTheClockValues = {false, false, false, param, false, param, false};

        String jsonData = createWorkTimeBuildJson(fromValues, toValues, workdayValues, aroundTheClockValues);

        given()
                .relaxedHTTPSValidation()
                .queryParam("_token", getToken)
                .contentType("application/json")
                .body(jsonData)
                .when()
                .post("/api/partner/createWorkTime")
                .then()
                .statusCode(500); // Ожидаем статус код 400 для невалидных входных данных
    }

    // нужно сообщение поменять поставщика/партнера для логина, для пасс - ок
    @ParameterizedTest()
    @ValueSource(strings = {"invalidLogin", "123123"})
    void invalidLoginTest(String login) {
        String invalidLoginMessage = given()
                .relaxedHTTPSValidation()
                .queryParam("_token", getToken)
                .contentType("application/json")
                .body("{\"login\":\"" + login + "\",\"password\":\"" + partnerPassword + "\"}")
                .when()
                .post("/api/partner/login")
                .then()
                .statusCode(200)
                .extract()
                .path("error");

        assertEquals("Неверный логин или пароль поставщика", invalidLoginMessage);
    }

    @ParameterizedTest()
    @ValueSource(strings = {"invalidPass", "", "123123"})
    void invalidPasswordTest(String password) {
        String invalidPasswordMessage = given()
                .relaxedHTTPSValidation()
                .queryParam("_token", getToken)
                .contentType("application/json")
                .body("{\"login\":\"" + partnerLogin + "\",\"password\":\"" + password + "\"}")
                .when()
                .post("/api/partner/login")
                .then()
                .statusCode(200)
                .extract()
                .path("error");

        assertEquals("Неверный логин или пароль партнера", invalidPasswordMessage);
    }

    @ParameterizedTest
    @ValueSource(strings = {"info", "legal", "bank", "workTime"})
    void partnerNoTokenTest(String endpoint) {
        String wrongAccessMessage = given()
                .relaxedHTTPSValidation()
                .contentType("application/json")
                .when()
                .get("/api/partner/" + endpoint)
                .then()
                .statusCode(401)
                .extract()
                .path("error");

        assertEquals("Wrong access credentials", wrongAccessMessage);
    }

    public String createWorkTimeBuildJson(Object[] fromValues, Object[] toValues, Object[] workdayValues, Object[] aroundTheClockValues) {
        StringBuilder jsonDataBuilder = new StringBuilder();
        jsonDataBuilder.append("{");
        jsonDataBuilder.append("\"ok\": 0,");
        jsonDataBuilder.append("\"error\": \"string\",");
        jsonDataBuilder.append("\"data\": {");
        jsonDataBuilder.append("\"id\": 0,");
        jsonDataBuilder.append("\"days\": {");

        for (int i = 0; i < fromValues.length; i++) {
            jsonDataBuilder.append("\"").append(i).append("\": {");
            jsonDataBuilder.append("\"from\": ").append(fromValues[i]).append(",");
            jsonDataBuilder.append("\"to\": ").append(toValues[i]).append(",");
            jsonDataBuilder.append("\"workday\": ").append(workdayValues[i]).append(",");
            jsonDataBuilder.append("\"around_the_clock\": ").append(aroundTheClockValues[i]);
            jsonDataBuilder.append("}");
            if (i < fromValues.length - 1) {
                jsonDataBuilder.append(",");
            }
        }

        jsonDataBuilder.append("}},");
        jsonDataBuilder.append("\"meta\": {\"total\": 0},");
        jsonDataBuilder.append("\"t\": 0}");

        return jsonDataBuilder.toString();
    }
}