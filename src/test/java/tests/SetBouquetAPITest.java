package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import models.bouquet.BouquetDataDto;
import models.bouquet.BouquetDataItemDto;
import models.city.CityDataDto;
import models.city.CityDataItemDto;
import org.junit.jupiter.api.Test;

public class SetBouquetAPITest {
    TestData testData = new TestData();
    CityDataItemDto city;
    BouquetDataItemDto bouquet;
    @Test
    @SneakyThrows
    void test() {
        RequestSpecification httpRequest = RestAssured.given();
        Response responseCity = httpRequest
                .auth().basic("florist_api", "123")
                .get("https://www.test.florist.local/api/city");
        ResponseBody bodyCity = responseCity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        CityDataDto cityData = objectMapper.readValue(bodyCity.asString(), CityDataDto.class);
        city = testData.getRandomCity(cityData.getData());

        Response responseBouquet = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", city.getId())
                .get("https://www.test.florist.local/api/bouquet");
        ResponseBody bodyBouquet = responseBouquet.getBody();

        BouquetDataDto bouquetData = objectMapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);
        bouquet = testData.getRandomBouquet(bouquetData.getData());

        System.out.println(bouquet.getName());
        System.out.println(bouquet.getMin_price().getEur());
    }
}
