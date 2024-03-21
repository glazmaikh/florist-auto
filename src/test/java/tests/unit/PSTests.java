package tests.unit;

import api.HibernateUtil;
import api.PartnerProfileDao;
import entityDB.*;
import helpers.HelperPage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import tests.TestBase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("api")
public class PSTests extends TestBase {
    public static String authPartnerToken;
    public static String token;
    public static Long id;
    public static Long supplierId;
    public static PartnerProfileDao dao;

    @BeforeAll
    static void setClass() {
        HibernateUtil.getFlowersSession();
        HibernateUtil.getPsSession();

        id = 5699L;
        supplierId = 295L;
        dao = new PartnerProfileDao();

        authPartnerToken = given()
                .relaxedHTTPSValidation()
                .auth().basic("florist_api", "123")
                .when()
                .get("/api/auth/partner")
                .then()
                .statusCode(200)
                .extract().path("data.token").toString();
    }

    @BeforeEach
    void setTest() {
        token = given()
                .relaxedHTTPSValidation()
                .auth().basic("florist_api", "123")
                .header("Cookie", "auth_partner_token=" + authPartnerToken)
                .contentType("application/json")
                .when()
                .post("api/partner/login/byShort/Y19fu1Gb")
                .then()
                .statusCode(200)
                .extract()
                .path("data.token");
    }

    @Test
    void partnerLoginTest() {
        UserPSEntity userDB = dao.getUserPSInfo(supplierId);

        Map<String, Object> apiResponse = given()
                .relaxedHTTPSValidation()
                .auth().basic("florist_api", "123")
                .header("Cookie", "auth_partner_token=" + authPartnerToken)
                .when()
                .post("api/partner/login/byShort/Y19fu1Gb")
                .then()
                .statusCode(200)
                .extract()
                .path("data");

        assertEquals(userDB.getId(), Long.valueOf(apiResponse.get("user").toString()));
        assertEquals(userDB.getName(), apiResponse.get("name"));
        assertEquals(userDB.getEmail(), apiResponse.get("email"));
    }

    @Test
    void supplierLoginTest() {
        AccountEntity accountDB = dao.getSupplierInfo(id);

        Map<String, Object> apiResponse = given()
                .relaxedHTTPSValidation()
                .queryParam("_token", authPartnerToken)
                .contentType("application/json")
                .body("{\"login\":\"" + supplierLogin + "\",\"password\":\"" + supplierPassword + "\"}")
                .when()
                .post("api/partner/login")
                .then()
                .statusCode(200)
                .extract()
                .path("data");

        assertEquals(accountDB.getCity(), apiResponse.get("city"));
        assertEquals(accountDB.getEmail(), apiResponse.get("email"));
        assertEquals(id, Long.valueOf(apiResponse.get("id").toString()));
        assertEquals(accountDB.getPartnerLogin(), apiResponse.get("login"));
        assertEquals(accountDB.getName(), apiResponse.get("name"));
        assertEquals(accountDB.getPhones(), apiResponse.get("phone"));
        assertEquals(accountDB.getUseProductsstorage(), apiResponse.get("useProductsStorage"));
    }

    @Test
    void partnerInfoTest() {
        UserEntity user = dao.getPartnerInfo(id);

        Map<String, Object> apiResponse = given()
                .relaxedHTTPSValidation()
                .queryParam("_token", token)
                .contentType("application/json")
                .when()
                .get("api/partner/info")
                .then()
                .statusCode(200)
                .extract()
                .path("data.partner_profile");

        System.out.println(apiResponse);

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
                .queryParam("_token", token)
                .contentType("application/json")
                .when()
                .get("api/partner/legal")
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
                .queryParam("_token", token)
                .contentType("application/json")
                .when()
                .get("api/partner/bank")
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
                .queryParam("_token", token)
                .contentType("application/json")
                .body(jsonData)
                .when()
                .post("api/partner/createWorkTime")
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
                .queryParam("_token", token)
                .contentType("application/json")
                .when()
                .get("api/partner/workTime")
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
                .queryParam("_token", token)
                .contentType("application/json")
                .body(jsonData)
                .when()
                .post("api/partner/createWorkTime")
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
                .queryParam("_token", token)
                .contentType("application/json")
                .body(jsonData)
                .when()
                .post("api/partner/createWorkTime")
                .then()
                .statusCode(200); // statusCode(200) - нужен статус 400
    }

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
                .queryParam("_token", token)
                .contentType("application/json")
                .body(jsonData)
                .when()
                .post("api/partner/createWorkTime")
                .then()
                .statusCode(500); // Ожидаем статус код 400 для невалидных входных данных
    }

    // нужно сообщение поменять поставщика/партнера для логина, для пасс - ок
    @ParameterizedTest()
    @ValueSource(strings = {"invalidLogin", "123123"})
    void invalidLoginTest(String login) {
        String invalidLoginMessage = given()
                .relaxedHTTPSValidation()
                .queryParam("_token", token)
                .contentType("application/json")
                .body("{\"login\":\"" + login + "\",\"password\":\"" + supplierPassword + "\"}")
                .when()
                .post("api/partner/login")
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
                .queryParam("_token", token)
                .contentType("application/json")
                .body("{\"login\":\"" + supplierLogin + "\",\"password\":\"" + password + "\"}")
                .when()
                .post("api/partner/login")
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
                .get("api/partner/" + endpoint)
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