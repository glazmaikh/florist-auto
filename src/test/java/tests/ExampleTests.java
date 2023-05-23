package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import models.DataDto;
import models.DataItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.MainPage;


public class ExampleTests extends TestBase{
    MainPage mainPage = new MainPage();
    DataItemDto city;
    TestData testData = new TestData();

    @BeforeEach
    @SneakyThrows
    public void settings() {
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest
                .auth().basic("florist_api", "123")
                .get("https://www.test.florist.local/api/city");
        ResponseBody body = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        DataDto data = objectMapper.readValue(body.asString(), DataDto.class);
        city = testData.getRandomCity(data.getData());
    }

    @Test
    void criticalPathTest() {
        mainPage.openMainPage()
                .closeCookiePopUp()
                .setDeliveryCity(city.getName())
                .setBouquet(city.getName());
    }
}