package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
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
import models.order.OrderData;
import models.register.User;
import models.register.UserWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ApiClient {
    private final RequestSpecification httpRequest = RestAssured.given();
    private final CityDataItemDto city = getRandomCityFromList();
    private final BouquetDataItemDto bouquet = getRandomBouquetByCityID(city.getId());
    private Data data = getDeliveryPriceByCitySlug(city.getSlug());
    private final ObjectMapper objectMapper = new ObjectMapper();

    // получение рандомного города из списка всех городов
    @SneakyThrows
    private CityDataItemDto getRandomCityFromList() {
        Response responseCity = httpRequest
                .auth().basic("florist_api", "123")
                .get("https://www.test.florist.local/api/city");
        ResponseBody bodyCity = responseCity.getBody();

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
        Response responseBouquet = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", cityId)
                .param("showPrices", 1)
                .param("includeIflorist", 1)
                .get("https://www.test.florist.local/api/bouquet");
        ResponseBody bodyBouquet = responseBouquet.getBody();

        BouquetDataDto bouquetData = objectMapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);
        return getRandomBouquetObject(bouquetData.getData());
    }

    @SneakyThrows
    public Data getDeliveryPriceByCitySlug(String citySlug) {
        Response responseCitySlug = httpRequest
                .auth().basic("florist_api", "123")
                .param("alias", citySlug)
                .get("http://www.test.florist.local/api/city/0");
        ResponseBody responseBody = responseCitySlug.getBody();

        CityDataAliasDto cityDataAliasDto = objectMapper.readValue(responseBody.asString(), CityDataAliasDto.class);
        data = cityDataAliasDto.getData();
        return data;
    }

    @SneakyThrows
    public OrderData getOrderData() {
        Response responseOrderData = httpRequest
                .auth().basic("florist_api", "123")
                .param("id", HelperPage.getOrderNumber())
                .param("access_key", HelperPage.getOrderAccessKey())
                .get("http://www.test.florist.local/api/order/byAccessKey");
        ResponseBody orderBody = responseOrderData.getBody();
        return objectMapper.readValue(orderBody.asString(), OrderData.class);
    }

    @SneakyThrows
    public models.auth.User getUser(String login, String password) {
        Response userAuthData = httpRequest
                .auth().basic("florist_api", "123")
                .param("login", login)
                .param("password", password)
                .get("http://www.test.florist.local/api/user/login");
        ResponseBody userAuthDataBody = userAuthData.getBody();

        AuthDto authDto = objectMapper.readValue(userAuthDataBody.asString(), AuthDto.class);
        models.auth.User user = authDto.getUser();
        return user;
    }

    public void registerUser(String login, String email, String phone, String password) {
        User user = new User(login, email, phone, password);
        UserWrapper userWrapper = new UserWrapper(user);

        try {
            String requestBody = objectMapper.writeValueAsString(userWrapper);
            System.out.println(requestBody);

            RestAssured.given()
                    .auth().basic("florist_api", "123")
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .post("https://www.test.florist.local/api/user")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .extract()
                    .response();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private BouquetDataItemDto getRandomBouquetObject(Map<String, BouquetDataItemDto> bouquetMap) {
        List<BouquetDataItemDto> values = new ArrayList<>(bouquetMap.values());
        return values.get(new Random().nextInt(values.size()));
    }
}