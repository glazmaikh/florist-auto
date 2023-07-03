package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import models.auth.AuthDto;
import models.bouquet.BouquetDataDto;
import models.bouquet.BouquetDataItemDto;
import models.bouquet.PriceItemDto;
import models.city.CityDataDto;
import models.city.CityDataItemDto;
import models.cityAlias.Data;
import models.cityAlias.CityDataAliasDto;
import models.disabledDelivery.DisabledDeliveryDateResponse;
import models.order.OrderData;
import models.register.User;
import models.register.UserWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiClient {
    private final CityDataItemDto city = getRandomCityFromList();
    private final BouquetDataItemDto bouquet = getRandomBouquetByCityID(city.getId());
    private Data data = getDeliveryPriceByCitySlug(city.getSlug());
    private final List<String> disabledDates = getDisabledDate();
    private OrderData orderData;

    // получение рандомного города из списка всех городов
    @SneakyThrows
    private CityDataItemDto getRandomCityFromList() {
        RequestSpecification httpRequest = given();
        Response responseCity = httpRequest
                .auth().basic("florist_api", "123")
                .get("api/city");
        ResponseBody bodyCity = responseCity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        CityDataDto cityData = objectMapper.readValue(bodyCity.asString(), CityDataDto.class);
        CityDataItemDto city = getRandomCityObject(cityData.getData());
        while (city.getCountry().getId().equals("2")) {
            city = getRandomCityObject(cityData.getData());
        }
        return city;
    }

    // получение рандомного букета по ID города
    @SneakyThrows
    public BouquetDataItemDto getRandomBouquetByCityID(String cityId) {
        RequestSpecification httpRequest = given();
        Response responseBouquet = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", cityId)
                .param("showPrices", 1)
                .param("includeIflorist", 1)
                .get("api/bouquet");
        ResponseBody bodyBouquet = responseBouquet.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        BouquetDataDto bouquetData = objectMapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);
        return getRandomBouquetObject(bouquetData.getData());
    }

    // получение цены доставки по slug города
    @SneakyThrows
    public Data getDeliveryPriceByCitySlug(String citySlug) {
        RequestSpecification httpRequest = given();
        Response responseCitySlug = httpRequest
                .auth().basic("florist_api", "123")
                .param("alias", citySlug)
                .get("api/city/0");
        ResponseBody responseBody = responseCitySlug.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        CityDataAliasDto cityDataAliasDto = objectMapper.readValue(responseBody.asString(), CityDataAliasDto.class);
        data = cityDataAliasDto.getData();
        return data;
    }

    // получение даты о заказе из ERP
//    @SneakyThrows
//    public OrderData getOrderData() {
//        RequestSpecification httpRequest = given();
//        Response responseOrderData = httpRequest
//                .auth().basic("florist_api", "123")
//                .param("id", HelperPage.getOrderNumber())
//                .param("access_key", HelperPage.getOrderAccessKey())
//                .get("api/order/byAccessKey");
//        ResponseBody orderBody = responseOrderData.getBody();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.readValue(orderBody.asString(), OrderData.class);
//    }

    @SneakyThrows
    public void getOrderData() {
        RequestSpecification httpRequest = given();
        Response responseOrderData = httpRequest
                .auth().basic("florist_api", "123")
                .param("id", HelperPage.getOrderNumber())
                .param("access_key", HelperPage.getOrderAccessKey())
                .get("api/order/byAccessKey");
        ResponseBody orderBody = responseOrderData.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        orderData = objectMapper.readValue(orderBody.asString(), OrderData.class);
    }

    public int getOrderId() {
        return orderData.getData().getId();
    }

    public String getOrderBouquetName() {
        return orderData.getData().getCart().get("0").getName();
    }

    public int getOrderPrice() {
        return orderData.getData().getCart().get("0").getPrice().getRUB();
    }

    public String getOrderVariation() {
        return orderData.getData().getCart().get("0").getVariation();
    }

    public int getOrderCount() {
        return orderData.getData().getCart().get("0").getCount();
    }

    public String getOrderDeliveryDate() {
        return orderData.getData().getCart().get("1").getName();
    }

    public int getOrderDeliveryPrice() {
        return orderData.getData().getCart().get("1").getPrice().getRUB();
    }

    public int getOrderTotalPrice() {
        return orderData.getData().getTotal().getRUB();
    }

    public String getOrderStatus() {
        return orderData.getData().getStatus_text();
    }

    public String getOrderCreatedAt() {
        return orderData.getData().getCreated_at();
    }

    public String getRecipientName() {
        return orderData.getData().getRecipient_name();
    }

    // для чего это?
    @SneakyThrows
    public models.auth.User getUser(String login, String password) {
        RequestSpecification httpRequest = given();
        Response userAuthData = httpRequest
                .auth().basic("florist_api", "123")
                .param("login", login)
                .param("password", password)
                .get("api/user/login");
        ResponseBody userAuthDataBody = userAuthData.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        AuthDto authDto = objectMapper.readValue(userAuthDataBody.asString(), AuthDto.class);
        models.auth.User user = authDto.getUser();
        return user;
    }

    // метод регистрации клиента на сайте
    public void apiRegisterUser(String login, String email, String phone, String password) {
        User user = new User(login, email, phone, password);
        UserWrapper userWrapper = new UserWrapper(user);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String requestBody = objectMapper.writeValueAsString(userWrapper);
            Response response = given()
                    .auth().basic("florist_api", "123")
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .post("api/user");

            response.then().statusCode(200);

            response.then().assertThat()
                    .body("data.name", equalTo(user.getName()))
                    .body("data.email", equalTo(user.getEmail()))
                    .body("data.phone", equalTo(user.getPhone()))
                    .body("data.password", equalTo(user.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    private List<String> getDisabledDate() {
        RequestSpecification httpRequest = given();
        Response responseDisabledData = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", city.getId())
                .param("ids", bouquet.getId())
                .get("/api/delivery/date");
        ResponseBody responseBody = responseDisabledData.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        DisabledDeliveryDateResponse disabledDate = objectMapper.readValue(responseBody.asString(), DisabledDeliveryDateResponse.class);
        return new ArrayList<>(disabledDate.getData().getDisabled_dates().values());
    }

    public String getRandomCityName() {
        return city.getName();
    }

    public String getSlug() {
        return city.getSlug();
    }

    public double getDeliveryPrice() {
        return data.getDelivery().get("RUB");
    }

    public String getBouquetName() {
        return bouquet.getName();
    }

    public int getBouquetPrice() {
        return bouquet.getMin_price().getRub();
    }

    public int getBouquetId() {
        return bouquet.getId();
    }

    public List<PriceItemDto> getPriceList() {
        return bouquet.getPriceList();
    }

    private CityDataItemDto getRandomCityObject(Map<String, CityDataItemDto> cityMap) {
        List<CityDataItemDto> values = new ArrayList<>(cityMap.values());
        return values.get(new Random().nextInt(values.size()));
    }

    // иногда error index must be positive
    private BouquetDataItemDto getRandomBouquetObject(Map<String, BouquetDataItemDto> bouquetMap) {
        List<BouquetDataItemDto> values = new ArrayList<>(bouquetMap.values());
        return values.get(new Random().nextInt(values.size()));
    }

    public List<String> getDisabledDeliveryDaysList() {
        return disabledDates;
    }
}