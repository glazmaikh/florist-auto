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

import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class TestData {

    CityDataItemDto city;
    BouquetDataItemDto bouquet;
    Faker faker = new Faker(new Locale("ru"));
    String yourName = faker.name().fullName();
    String yourEmail = faker.internet().emailAddress();
    String yourPhone = faker.phoneNumber().cellPhone();
    String name = faker.name().firstName();
    String email = faker.internet().emailAddress();
    String phone = faker.phoneNumber().cellPhone();
    String address = faker.address().fullAddress();

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

        Response responseBouquet = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", city.getId())
                .get("https://www.test.florist.local/api/bouquet");
        ResponseBody bodyBouquet = responseBouquet.getBody();

        BouquetDataDto bouquetData = objectMapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);
        bouquet = getRandomBouquet(bouquetData.getData());
    }

    public String getRandomCityName() {
        return city.getName();
    }

    public String getRandomBouquetName() {
        return bouquet.getName();
    }

    public String getCitySlug() {
        return city.getSlug();
    }

    public BouquetDataItemDto getBouquet() {
        return bouquet;
    }

    public CityDataItemDto getRandomCity(Map<String, CityDataItemDto> cityMap) {
        Object[] values = cityMap.values().toArray();
        return (CityDataItemDto) values[new Random().nextInt(values.length)];
    }

    public BouquetDataItemDto getRandomBouquet(Map<String, BouquetDataItemDto> bouquetMap) {
        Object[] values = bouquetMap.values().toArray();
        return (BouquetDataItemDto) values[new Random().nextInt(values.length)];
    }
}