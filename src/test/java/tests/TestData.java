package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import models.bouquet.BouquetDataDto;
import models.bouquet.BouquetDataItemDto;
import models.city.CityDataDto;
import models.city.CityDataItemDto;
import models.cityAlias.Data;
import models.cityAlias.CityDataAliasDto;

import java.util.*;

public class TestData {
    CityDataItemDto city;
    BouquetDataItemDto bouquet;
    Faker faker = new Faker(new Locale("ru"));
    String yourName = faker.name().fullName();
    String yourEmail = faker.internet().emailAddress("en");
    String yourPhone = faker.phoneNumber().cellPhone();
    String name = faker.name().firstName();
    String phone = faker.phoneNumber().cellPhone();
    String address = faker.address().streetAddress();

    Data data;
    public String getYourName() {
        return yourName;
    }

    public String getYourEmail() {
        return yourEmail;
    }

    public String getYourPhone() {
        return yourPhone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    @SneakyThrows
    public TestData() {
        RequestSpecification httpRequest = RestAssured.given();
        Response responseCity = httpRequest
                .auth().basic("florist_api", "123")
                .get("https://www.test.florist.local/api/city");
        ResponseBody bodyCity = responseCity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        CityDataDto cityData = objectMapper.readValue(bodyCity.asString(), CityDataDto.class);
        city = getRandomCity(cityData.getData());

        Response responseCitySlug = httpRequest
                .auth().basic("florist_api", "123")
                .param("alias", city.getSlug())
                .get("http://www.test.florist.local/api/city/0");
        ResponseBody responseBody = responseCitySlug.getBody();

        CityDataAliasDto cityDataAliasDto = objectMapper.readValue(responseBody.asString(), CityDataAliasDto.class);
        data = cityDataAliasDto.getData();

        Response responseBouquet = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", city.getId())
                .param("showPrices", 1)
                .param("includeIflorist", 1)
                .get("https://www.test.florist.local/api/bouquet");
        ResponseBody bodyBouquet = responseBouquet.getBody();

        BouquetDataDto bouquetData = objectMapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);
        bouquet = getRandomBouquet(bouquetData.getData());
    }

    public String getRandomCityName() {
        return city.getName();
    }

    public String getBouquetName() {
        return bouquet.getName();
    }

    public int getDeliveryPrice() {
        return data.getDelivery().get("RUB").intValue();
    }

    public int getBouquetId() {
        return bouquet.getId();
    }

    public int getBouquetPrice() {
        return bouquet.getMin_price().getRub();
    }

    public String getCitySlug() {
        return city.getSlug();
    }

    public BouquetDataItemDto getBouquet() {
        return bouquet;
    }

    public CityDataItemDto getRandomCity(Map<String, CityDataItemDto> cityMap) {
        List<CityDataItemDto> values = new ArrayList<>(cityMap.values());
        return values.get(new Random().nextInt(values.size()));
    }

    public BouquetDataItemDto getRandomBouquet(Map<String, BouquetDataItemDto> bouquetMap) {
        List<BouquetDataItemDto> values =  new ArrayList<>(bouquetMap.values());
        return values.get(new Random().nextInt(values.size()));
    }
}