package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import models.city.CityDataDto;
import models.city.CityDataItemDto;
import models.order.OrderData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class ApiClient {
    private ObjectMapper objectMapper = new ObjectMapper();
    private CityDataItemDto city;

    @SneakyThrows
    public OrderData getOrderData() {
        RequestSpecification httpRequest = RestAssured.given();
        Response responseOrderData = httpRequest
                .auth().basic("florist_api", "123")
                .param("id", HelperPage.getOrderNumber())
                .param("access_key", HelperPage.getOrderAccessKey())
                .get("http://www.test.florist.local/api/order/byAccessKey");
        ResponseBody orderBody = responseOrderData.getBody();

        objectMapper = new ObjectMapper();
        return objectMapper.readValue(orderBody.asString(), OrderData.class);
    }

    @SneakyThrows
    public String getRandomCityId() {
        RequestSpecification httpRequest = RestAssured.given();
        Response responseCity = httpRequest
                .auth().basic("florist_api", "123")
                .get("https://www.test.florist.local/api/city");
        ResponseBody bodyCity = responseCity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        CityDataDto cityData = objectMapper.readValue(bodyCity.asString(), CityDataDto.class);
        city = getRandomCity(cityData.getData());
        while (city.getCountry().getId().equals("2")) {
            city = getRandomCity(cityData.getData());
        }
        return city.getId();
    }

    //public String getRandomBouquetName()

    private CityDataItemDto getRandomCity(Map<String, CityDataItemDto> cityMap) {
        List<CityDataItemDto> values = new ArrayList<>(cityMap.values());
        return values.get(new Random().nextInt(values.size()));
    }

//    public List<String> getRussianCitySlugList() {
//        String response = given()
//                .auth().basic("florist_api", "123")
//                .get("https://www.test.florist.local/api/city")
//                .then()
//                .extract()
//                .response()
//                .asString();
//
//        JsonPath jsonPath = new JsonPath(response);
//        Map<String, Map<String, Object>> data = jsonPath.getMap("data");
//        List<String> objectIds = data.entrySet()
//                .stream()
//                .filter(e -> e.getValue().containsKey("country") &&
//                        ((Map<String, Object>) e.getValue().get("country")).containsValue("Россия"))
//                .map(Map.Entry::getKey)
//                .toList();
//
//        return objectIds.stream()
//                .map(objectId -> data.get(objectId).get("slug").toString())
//                .toList();
//    }
//
//    @SneakyThrows
//    public String getCityName(String citySlug) {
//        String response = given()
//                .auth().basic("florist_api", "123")
//                .param("alias", citySlug)
//                .get("https://www.test.florist.local/api/city/0")
//                .then()
//                .extract()
//                .response().getBody().asString();
//
//        objectMapper = new ObjectMapper();
//
//        ResponseData responseData = objectMapper.readValue(response, ResponseData.class);
//        return responseData.getData().getName();
//    }
//

}
