package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import models.auth.AuthDto;
import models.auth.User;
import models.bouquet.BouquetDataDto;
import models.bouquet.BouquetDataItemDto;
import models.bouquet.PriceItemDto;
import models.city.CityDataDto;
import models.city.CityDataItemDto;
import models.cityAlias.Data;
import models.cityAlias.CityDataAliasDto;
import models.order.OrderData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ApiClient {
    private final RequestSpecification httpRequest = RestAssured.given();
    private final CityDataItemDto city = getRandomCityFromList();
    private final BouquetDataItemDto bouquet = getRandomBouquetByCityID(city.getId());
    private Data data = getDeliveryPriceByCitySlug(city.getSlug());
    //private User user = getUser("test123123@test.ru", "123123");

    // получение рандомного города из списка всех городов
    @SneakyThrows
    private CityDataItemDto getRandomCityFromList() {
        Response responseCity = httpRequest
                .auth().basic("florist_api", "123")
                .get("https://www.test.florist.local/api/city");
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
        Response responseBouquet = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", cityId)
                .param("showPrices", 1)
                .param("includeIflorist", 1)
                .get("https://www.test.florist.local/api/bouquet");
        ResponseBody bodyBouquet = responseBouquet.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
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

        ObjectMapper objectMapper = new ObjectMapper();
        CityDataAliasDto cityDataAliasDto = objectMapper.readValue(responseBody.asString(), CityDataAliasDto.class);
        data = cityDataAliasDto.getData();
        return data;
    }

    @SneakyThrows
    public OrderData getOrderData() {
        RequestSpecification httpRequest = RestAssured.given();
        Response responseOrderData = httpRequest
                .auth().basic("florist_api", "123")
                .param("id", HelperPage.getOrderNumber())
                .param("access_key", HelperPage.getOrderAccessKey())
                .get("http://www.test.florist.local/api/order/byAccessKey");
        ResponseBody orderBody = responseOrderData.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(orderBody.asString(), OrderData.class);
    }

    @SneakyThrows
    public User getUser(String login, String password) {
        System.out.println(login + " login");
        System.out.println(password + " pass");
        Response userAuthData = httpRequest
                .auth().basic("florist_api", "123")
                .param("login", login)
                .param("password", password)
                .get("http://www.test.florist.local/api/user/login");
        ResponseBody userAuthDataBody = userAuthData.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        AuthDto authDto = objectMapper.readValue(userAuthDataBody.asString(), AuthDto.class);
        User user = authDto.getUser();
        System.out.println(user + " user");
        System.out.println(user.getName() + " userName");
        return user;
    }

//    public String getUserName() {
//        return user.getName();
//    }

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