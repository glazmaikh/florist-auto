package helpers;

import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import models.order.OrderData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelperPage {
    public static int totalPrice(int bouquetPrice, int deliveryPrice) {
        return bouquetPrice + deliveryPrice;
    }

    public static void assertBouquetPrice(int bouquetPrice, SelenideElement element) {
        assertEquals(String.valueOf(bouquetPrice), priceRegex(element));
    }

    public static void assertDeliveryPrice(int deliveryPrice, SelenideElement element) {
        assertEquals(String.valueOf(deliveryPrice), priceRegex(element));
    }
    public static String priceRegex(SelenideElement element) {
        return element.getText().replaceAll("[\\s₽]", "");
    }

    public static String getOrderNumber() {
        String path = webdriver().driver().url();
        Pattern pattern = Pattern.compile("/order/payment/(\\d+)/");
        Matcher matcher = pattern.matcher(path);
        if (!matcher.find()) {
            return null;
        } return matcher.group(1);
    }
    public static String getOrderAccessKey() {
        String url = webdriver().driver().url();
        Pattern pattern = Pattern.compile("\\/\\d+\\/([^\\/]+)$");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            return null;
        } return matcher.group(1);
    }

    @SneakyThrows
    public static OrderData getOrderData() {
        RequestSpecification httpRequest = RestAssured.given();
        Response responseOrderData = httpRequest
                .auth().basic("florist_api", "123")
                .param("id", getOrderNumber())
                .param("access_key", getOrderAccessKey())
                .get("http://www.test.florist.local/api/order/byAccessKey");
        ResponseBody orderBody = responseOrderData.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(orderBody.asString(), OrderData.class);
    }
}