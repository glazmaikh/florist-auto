package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import models.order.OrderData;

public class APIClient {
    @SneakyThrows
    public OrderData getOrderData() {
        RequestSpecification httpRequest = RestAssured.given();
        System.out.println(HelperPage.getOrderNumber() + " order");
        System.out.println(HelperPage.getOrderAccessKey() + " key");
        Response responseOrderData = httpRequest
                .auth().basic("florist_api", "123")
                .param("id", HelperPage.getOrderNumber())
                .param("access_key", HelperPage.getOrderAccessKey())
                .get("http://www.test.florist.local/api/order/byAccessKey");
        ResponseBody orderBody = responseOrderData.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(orderBody.asString(), OrderData.class);
    }
}
