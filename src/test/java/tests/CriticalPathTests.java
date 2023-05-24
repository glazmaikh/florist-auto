package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import models.city.CityDataDto;
import models.city.CityDataItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.MainPage;


public class CriticalPathTests extends TestBase{
    MainPage mainPage = new MainPage();
    CityDataItemDto city;
    TestData testData = new TestData();

    @BeforeEach
    @SneakyThrows
    public void settings() {
        RequestSpecification httpRequest = RestAssured.given();
        Response responseCity = httpRequest
                .auth().basic("florist_api", "123")
                .get("https://www.test.florist.local/api/city");
        ResponseBody bodyCity = responseCity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        CityDataDto data = objectMapper.readValue(bodyCity.asString(), CityDataDto.class);
        city = testData.getRandomCity(data.getData());
    }

    @Test
    void criticalPathTest() {
        mainPage.openMainPage()
                .closeCookiePopUp()
                .setDeliveryCity(city.getName());
//                .setBouquet(bouquet.getName());
    }
}