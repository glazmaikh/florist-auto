package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import models.bouquet.BouquetDataDto;
import models.city.CityDataDto;
import models.order.OrderData;

public class APIClient {
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
//
//    RequestSpecification httpRequest = RestAssured.given();
//    Response responseCity = httpRequest
//            .auth().basic("florist_api", "123")
//            .get("https://www.test.florist.local/api/city");
//    ResponseBody bodyCity = responseCity.getBody();
//
//    ObjectMapper objectMapper = new ObjectMapper();
//    CityDataDto cityData = objectMapper.readValue(bodyCity.asString(), CityDataDto.class);
//    city = getRandomCity(cityData.getData());
//
//    Response responseBouquet = httpRequest
//            .auth().basic("florist_api", "123")
//            .param("city", city.getId())
//            .param("showPrices", 1)
//            .param("includeIflorist", 1)
//            .get("https://www.test.florist.local/api/bouquet");
//    ResponseBody bodyBouquet = responseBouquet.getBody();
//
//    BouquetDataDto bouquetData = objectMapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);

}
