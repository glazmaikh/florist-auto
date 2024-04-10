package tests.unit;

import api.HibernateUtil;
import api.PartnerProfileDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entityDB.*;
import helpers.HelperPage;
import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import tests.TestBase;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@Tag("api")
public class PSTests extends TestBase {
    public static String authPartnerToken;
    public static String token;
    public static Long id;
    public static Long supplierId;
    public static PartnerProfileDao dao;
    public static ObjectMapper mapper;
    public static ClassLoader classLoader;

    @BeforeAll
    static void setClass() {
        HibernateUtil.getFlowersSession();
        HibernateUtil.getPsSession();

        mapper = new ObjectMapper();
        classLoader = PSTests.class.getClassLoader();

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
    void partnerCreateProductTest() throws IOException {
        classLoader = PSTests.class.getClassLoader();
        String postJson = IOUtils.toString(Objects.requireNonNull(classLoader.getResourceAsStream("requests/createPSProduct.json")),
                StandardCharsets.UTF_8);

        Response apiResponse = given()
                .relaxedHTTPSValidation()
                .header("Cookie", "auth_partner_token=" + authPartnerToken)
                .queryParam("_token", token)
                .contentType("application/json")
                .body(postJson)
                .when()
                .post("api/partner/product")
                .then()
                .statusCode(200)
                .extract().response();

        JsonNode responseNode = mapper.readTree(apiResponse.getBody().asString());
        JsonNode dataNode = responseNode.path("data");
        Long productId = responseNode.path("data").path("id").asLong();
        JsonNode variationNode = dataNode.path("variation").get("0");

        PartnerProductEntity productEntityDB = dao.getPartnerProductById(productId);
        VariationEntity variationEntityDB = dao.getVariationByProductId(productId);

        String supplierIdApi = HelperPage.getSplitSupplierId(dataNode.path("supplier").path("@id").asText());
        String variationIdApi = HelperPage.getSplitSupplierId(variationNode.path("@id").asText());
        String variationSupplIdApi = HelperPage.getSplitSupplierId(variationNode.path("product").asText());

        JsonNode tagsNode = dataNode.get("tags");
        StringBuilder tags = new StringBuilder();
        if (tagsNode != null && tagsNode.isObject()) {
            tagsNode.fields().forEachRemaining(entry -> {
                JsonNode tagObject = entry.getValue();
                tagObject.fields().forEachRemaining(tagEntry -> {
                    tags.append(tagEntry.getKey()).append(",");
                });
            });
        }
        if (tags.length() > 0) {
            tags.deleteCharAt(tags.length() - 1);
        }

        JsonNode imagesNode = variationNode.get("images");
        StringBuilder images = new StringBuilder("[");
        if (imagesNode != null && imagesNode.isObject()) {
            imagesNode.elements().forEachRemaining(valueNode -> {
                images.append("\"");
                String escapedValue = valueNode.asText().replaceAll("/", "\\\\/");
                images.append(escapedValue);
                images.append("\",");
            });
        }
        images.deleteCharAt(images.length() - 1);
        images.append("]");

        JsonNode compositionsNode = variationNode.get("compositions");
        StringBuilder compositions = new StringBuilder("[");
        if (compositionsNode != null && compositionsNode.isObject()) {
            compositionsNode.fields().forEachRemaining(entry -> {
                JsonNode compositionObject = entry.getValue();
                StringBuilder composition = new StringBuilder("{");
                compositionObject.fields().forEachRemaining(param -> {
                    String paramName = param.getKey();
                    JsonNode paramValueNode = param.getValue();
                    if (paramValueNode != null && paramValueNode.isTextual() && (paramName.equals("id") || paramName.equals("count"))) {
                        String paramValue = paramValueNode.asText();
                        composition.append("\"").append(paramName).append("\":\"").append(paramValue).append("\",");
                    }
                });
                if (composition.length() > 1) {
                    composition.deleteCharAt(composition.length() - 1);
                }
                composition.append("}");
                compositions.append(composition).append(",");
            });
        }
        if (compositions.length() > 1) {
            compositions.deleteCharAt(compositions.length() - 1);
        }
        compositions.append("]");

        JsonNode dimensionsNode = variationNode.get("dimensions");
        StringBuilder dimensions = new StringBuilder("{");
        if (dimensionsNode != null && dimensionsNode.isObject()) {
            dimensionsNode.fields().forEachRemaining(entry -> {
                dimensions.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue().asText()).append("\",");
            });
        }
        dimensions.deleteCharAt(dimensions.length() - 1);
        dimensions.append("}");

        assertEquals(productEntityDB.getId(), dataNode.get("id").asLong());
        assertEquals(productEntityDB.getTitle(), dataNode.get("title").asText());
        assertEquals(productEntityDB.getDescription(), dataNode.get("description").asText());
        assertEquals(productEntityDB.getUpdatedAt(), HelperPage.regexDateISO8601(dataNode.get("updatedAt").asText()));
        assertEquals(productEntityDB.getCreatedAt(), HelperPage.regexDateISO8601(dataNode.get("createdAt").asText()));
        assertEquals(productEntityDB.getSupplierId(), Long.valueOf(supplierIdApi));
        assertEquals(productEntityDB.getHidden(), dataNode.get("hidden").asInt());
        assertEquals(productEntityDB.getTags(), tags.toString());
        assertEquals(productEntityDB.getType(), HelperPage.getTagCutter(dataNode.get("type").toString()));
        assertEquals(productEntityDB.getColor(), HelperPage.getTagCutter(dataNode.get("color").toString()));

        assertEquals(variationEntityDB.getId(), Long.valueOf(variationIdApi));
        assertEquals(variationEntityDB.getProductId(), variationSupplIdApi);
        assertEquals(variationEntityDB.getTitle(), variationNode.get("title").asText());
        assertEquals(variationEntityDB.getCreatedAt(), HelperPage.regexDateISO8601(variationNode.get("createdAt").asText()));
        assertEquals(variationEntityDB.getUpdatedAt(), HelperPage.regexDateISO8601(variationNode.get("updatedAt").asText()));
        assertEquals(variationEntityDB.getPrice(), variationNode.get("price").asText());
        assertEquals(variationEntityDB.getCurrency(), variationNode.get("currency").asText());
        assertEquals(variationEntityDB.getHidden(), variationNode.get("hidden").asInt());
        assertEquals(variationEntityDB.getImages(), images.toString());
        assertEquals(variationEntityDB.getCompositions(), compositions.toString());
        assertEquals(variationEntityDB.getDimensions(), dimensions.toString());
        boolean isDefaultFromApi = variationNode.get("isDefault").asInt() == 1;
        assertEquals(variationEntityDB.isDefault(), isDefaultFromApi);
        assertEquals(variationEntityDB.getFormationTime(), variationNode.get("formationTime").asInt());
    }

    @Test
    void partnerPutProductTest() throws IOException {
        classLoader = PSTests.class.getClassLoader();
        String putJson = IOUtils.toString(Objects.requireNonNull(classLoader.getResourceAsStream("requests/updatePSProduct.json")),
                StandardCharsets.UTF_8);

        PartnerProductEntity productEntityDB = dao.getOnePartnerPSProduct(supplierId);
        Long productId = productEntityDB.getId();

        VariationEntity variationEntityDB = dao.getVariationByProductId(productId);
        Long variationId = variationEntityDB.getId();

        JsonNode jsonNode = mapper.readTree(putJson);
        ((ObjectNode) jsonNode).put("id", productId);
        ((ObjectNode) jsonNode.path("variation").get(0)).put("id", variationId);
        String updatedJson = jsonNode.toString();

        Response apiResponse = given()
                .relaxedHTTPSValidation()
                .header("Cookie", "auth_partner_token=" + authPartnerToken)
                .queryParam("_token", token)
                .contentType("application/json")
                .body(updatedJson)
                .when()
                .put("api/partner/product/" + productId)
                .then()
                .statusCode(200)
                .extract().response();

        JsonNode responseNode = mapper.readTree(apiResponse.getBody().asString());
        JsonNode responseDataNode = responseNode.path("data");
        JsonNode variationNode = responseDataNode.path("variation").get("0");

        String updatedProductData = dao.getUpdatedProductData(productId);
        String convertedDataToJson = HelperPage.convertUpdatedPsProductToJson(updatedProductData);
        JsonNode updatedDataNodeDB = mapper.readTree(convertedDataToJson);

        PartnerProductEntity productEntityDbUpdated = dao.getPartnerProductById(productId);
        VariationEntity variationEntityDbUpdated = dao.getVariationByProductId(productId);

        String supplierIdApi = HelperPage.getSplitSupplierId(responseDataNode.path("supplier").path("@id").asText());
        String variationIdApi = HelperPage.getSplitSupplierId(variationNode.path("@id").asText());
        String variationSupplIdApi = HelperPage.getSplitSupplierId(variationNode.path("product").asText());

        JsonNode tagsNode = responseDataNode.get("tags");
        StringBuilder tags = new StringBuilder();
        if (tagsNode != null && tagsNode.isObject()) {
            tagsNode.fields().forEachRemaining(entry -> {
                JsonNode tagObject = entry.getValue();
                tagObject.fields().forEachRemaining(tagEntry -> {
                    tags.append(tagEntry.getKey()).append(",");
                });
            });
        }
        if (tags.length() > 0) {
            tags.deleteCharAt(tags.length() - 1);
        }

        JsonNode imagesNode = variationNode.get("images");
        StringBuilder images = new StringBuilder("[");
        if (imagesNode != null && imagesNode.isObject()) {
            imagesNode.elements().forEachRemaining(valueNode -> {
                images.append("\"");
                String escapedValue = valueNode.asText().replaceAll("/", "\\\\/");
                images.append(escapedValue);
                images.append("\",");
            });
        }
        images.deleteCharAt(images.length() - 1);
        images.append("]");

        JsonNode compositionsNode = variationNode.get("compositions");
        StringBuilder compositions = new StringBuilder("[");
        if (compositionsNode != null && compositionsNode.isObject()) {
            compositionsNode.fields().forEachRemaining(entry -> {
                JsonNode compositionObject = entry.getValue();
                StringBuilder composition = new StringBuilder("{");
                compositionObject.fields().forEachRemaining(param -> {
                    String paramName = param.getKey();
                    JsonNode paramValueNode = param.getValue();
                    if (paramValueNode != null && paramValueNode.isTextual() && (paramName.equals("id") || paramName.equals("count"))) {
                        String paramValue = paramValueNode.asText();
                        composition.append("\"").append(paramName).append("\":\"").append(paramValue).append("\",");
                    }
                });
                if (composition.length() > 1) {
                    composition.deleteCharAt(composition.length() - 1);
                }
                composition.append("}");
                compositions.append(composition).append(",");
            });
        }
        if (compositions.length() > 1) {
            compositions.deleteCharAt(compositions.length() - 1);
        }
        compositions.append("]");

        JsonNode dimensionsNode = variationNode.get("dimensions");
        StringBuilder dimensions = new StringBuilder("{");
        if (dimensionsNode != null && dimensionsNode.isObject()) {
            dimensionsNode.fields().forEachRemaining(entry -> {
                dimensions.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue().asText()).append("\",");
            });
        }
        dimensions.deleteCharAt(dimensions.length() - 1);
        dimensions.append("}");

        assertEquals(productEntityDbUpdated.getId(), responseDataNode.get("id").asLong());
        assertEquals(updatedDataNodeDB.get("title").asText(), responseDataNode.get("title").asText());
        assertEquals(updatedDataNodeDB.get("description").asText(), responseDataNode.get("description").asText());
        assertEquals(productEntityDbUpdated.getUpdatedAt(), HelperPage.regexDateISO8601(responseDataNode.get("updatedAt").asText()));
        assertEquals(productEntityDbUpdated.getCreatedAt(), HelperPage.regexDateISO8601(responseDataNode.get("createdAt").asText()));
        assertEquals(productEntityDbUpdated.getSupplierId(), Long.valueOf(supplierIdApi));
        assertEquals(productEntityDbUpdated.getHidden(), responseDataNode.get("hidden").asInt());
        assertEquals(productEntityDbUpdated.getTags(), tags.toString());
        assertEquals(productEntityDbUpdated.getType(), HelperPage.getTagCutter(responseDataNode.get("type").toString()));
        assertEquals(productEntityDbUpdated.getColor(), HelperPage.getTagCutter(responseDataNode.get("color").toString()));

        assertEquals(variationEntityDbUpdated.getId(), Long.valueOf(variationIdApi));
        assertEquals(variationEntityDbUpdated.getProductId(), variationSupplIdApi);
//        assertEquals(variationEntityDbUpdated.getTitle(), variationNode.get("title").asText());
        assertEquals(variationEntityDbUpdated.getCreatedAt(), HelperPage.regexDateISO8601(variationNode.get("createdAt").asText()));
        assertEquals(variationEntityDbUpdated.getUpdatedAt(), HelperPage.regexDateISO8601(variationNode.get("updatedAt").asText()));
        assertEquals(variationEntityDbUpdated.getPrice(), variationNode.get("price").asText());
        assertEquals(variationEntityDbUpdated.getCurrency(), variationNode.get("currency").asText());
        assertEquals(variationEntityDbUpdated.getHidden(), variationNode.get("hidden").asInt());
        assertEquals(variationEntityDbUpdated.getImages(), images.toString());
        assertEquals(variationEntityDbUpdated.getCompositions(), compositions.toString());
        assertEquals(variationEntityDbUpdated.getDimensions(), dimensions.toString());
        boolean isDefaultFromApi = variationNode.get("isDefault").asInt() == 1;
        assertEquals(variationEntityDbUpdated.isDefault(), isDefaultFromApi);
        assertEquals(variationEntityDbUpdated.getFormationTime(), variationNode.get("formationTime").asInt());
    }

    @Test
    void partnerGetDeliveryListTest() throws JsonProcessingException {
        List<PartnerDeliveryEntity> deliveryEntityListDB = dao.getPartnerDeliveryList(id);

        Response response = given()
                .relaxedHTTPSValidation()
                .auth().basic("florist_api", "123")
                .queryParam("_token", token)
                .contentType("application/json")
                .when()
                .get("api/partner/delivery/list")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonNode apiResponse = mapper.readTree(response.getBody().asString());
        JsonNode deliveryNode = apiResponse.path("data").path("delivery");

        Map<Long, JsonNode> deliveryMap = new HashMap<>();
        for (JsonNode node : deliveryNode) {
            deliveryMap.put(node.path("id").asLong(), node);
        }

        for (PartnerDeliveryEntity deliveryEntityDB : deliveryEntityListDB) {
            JsonNode dbNode = deliveryMap.get(deliveryEntityDB.getId());

            assertEquals(deliveryEntityDB.getCost(), dbNode.path("cost").asInt());
            assertEquals(deliveryEntityDB.getCurrency(), dbNode.path("currency").asText());
            assertEquals(deliveryEntityDB.getHidden(), dbNode.path("hidden").asInt());
            assertEquals(deliveryEntityDB.getId(), dbNode.path("id").asLong());
            assertEquals(deliveryEntityDB.getLocationId(), dbNode.path("location_id").asInt());
            assertEquals(deliveryEntityDB.getLocationType(), dbNode.path("location_type").asInt());
            assertEquals(deliveryEntityDB.getName(), dbNode.path("name").asText());
            assertEquals(deliveryEntityDB.getTime(), dbNode.path("time").asInt());
        }

        int size = dao.getPartnerDeliveryListSize(id);
        JsonNode responseSize = mapper.readTree(response.getBody().asString());
        JsonNode responseSizeNode = responseSize.path("meta");

        assertEquals(size, responseSizeNode.get("total").asInt());
    }

    @Test
    void partnerPostDeliveryListTest() throws JsonProcessingException {
        String deliveryJson = "{\"location_id\":1," +
                "\"location_type\":1," +
                "\"cost\":123," +
                "\"time\":123," +
                "\"currency\":\"RUB\"," +
                "\"hidden\":0}";

        Response jsonResponse = given()
                .relaxedHTTPSValidation()
                .auth().basic("florist_api", "123")
                .queryParam("_token", token)
                .contentType("application/json")
                .body(deliveryJson)
                .when()
                .post("api/partner/createDelivery")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonNode apiResponse = mapper.readTree(jsonResponse.getBody().asString());
        JsonNode deliveryNode = apiResponse.path("data").path("delivery");

        PartnerDeliveryEntity deliveryEntityDB = dao.getPartnerDeliveryById(deliveryNode.get("id").asLong());
        assertEquals(deliveryEntityDB.getId(), deliveryNode.get("id").asLong());
        assertEquals(deliveryEntityDB.getLocationId(), deliveryNode.get("location_id").asInt());
        assertEquals(deliveryEntityDB.getLocationType(), deliveryNode.get("location_type").asInt());
        assertEquals(HelperPage.extractCity(deliveryEntityDB.getName()), deliveryNode.get("name").asText());
        assertEquals(deliveryEntityDB.getCost(), deliveryNode.get("cost").asInt());
        assertEquals(deliveryEntityDB.getTime(), deliveryNode.get("time").asInt());
        assertEquals(deliveryEntityDB.getHidden(), deliveryNode.get("hidden").asInt());

        Response jsonResponseForDel = given()
                .relaxedHTTPSValidation()
                .auth().basic("florist_api", "123")
                .queryParam("_token", token)
                .contentType("application/json")
                .when()
                .delete("api/partner/deleteDelivery/" + deliveryNode.get("id").asLong())
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonNode apiResponseForDel = mapper.readTree(jsonResponseForDel.getBody().asString());
        JsonNode deliveryNodeForDel = apiResponseForDel.path("data");
        assertEquals(deliveryNode.get("id").asLong(), deliveryNodeForDel.get("id").asLong());

        deliveryEntityDB = dao.getPartnerDeliveryById(deliveryNode.get("id").asLong());
        assertEquals(deliveryEntityDB.getHidden(), 1);
    }

    @Test
    void partnerOfertaTest() throws IOException {
        String ofertaHtml = new String(Files.readAllBytes(Paths.get("src/test/resources/oferta/oferta-2.html")));

        Response response = given()
                .relaxedHTTPSValidation()
                .auth().basic("florist_api", "123")
                .queryParam("_token", token)
                .contentType("application/json")
                .when()
                .get("api/partner/oferta")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonNode dataNode = mapper.convertValue(response.path("data"), JsonNode.class);
        assertEquals(2, dataNode.get("version").asInt());
        assertEquals(ofertaHtml, dataNode.get("oferta").asText());
    }

    @Test
    void partnerPriceModifierTest() {
        PriceModifierEntity priceModifierDB = dao.getPriceModifier(id);

        Map<String, Object> apiResponse = given()
                .relaxedHTTPSValidation()
                .auth().basic("florist_api", "123")
                .queryParam("_token", token)
                .contentType("application/json")
                .when()
                .get("api/partner/priceModifier")
                .then()
                .statusCode(200)
                .extract()
                .path("data");

        assertEquals(priceModifierDB.getAccountId(), Long.valueOf(apiResponse.get("id").toString()));
        assertEquals(priceModifierDB.getPriceModifier(), apiResponse.get("price_modifier"));
    }

    @Test
    void partnerPostPriceModifierTest() {
        PriceModifierEntity priceModifierDB = dao.getPriceModifier(id);
        int priceModifier = -1;

        Map<String, Object> apiResponse = given()
                .relaxedHTTPSValidation()
                .auth().basic("florist_api", "123")
                .queryParam("_token", token)
                .contentType("application/json")
                .body("{\"price_modifier\": " + priceModifier + "}")
                .when()
                .post("api/partner/priceModifier")
                .then()
                .statusCode(200)
                .extract()
                .path("data");

        assertEquals(priceModifierDB.getAccountId(), Long.valueOf(apiResponse.get("id").toString()));
        assertEquals(priceModifier, apiResponse.get("price_modifier"));
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