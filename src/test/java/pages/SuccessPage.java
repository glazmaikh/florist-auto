package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import helpers.HelperPage;
import lombok.SneakyThrows;
import models.order.OrderData;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.*;

public class SuccessPage {
    private final ElementsCollection orderList = $$x("//li");
    private final SelenideElement totalPrice = $(".no-wrap");
    private final ApiClient apiClient;

    public SuccessPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @SneakyThrows
    public SuccessPage assertSuccessCreatedOrder(String baseUrl) {
        //webdriver().shouldHave(url(baseUrl + apiClient.getCitySlug() + "/order/payment/" + HelperPage.getOrderNumber() + "/success/" + HelperPage.getOrderAccessKey()), Duration.ofSeconds(10));
        apiClient.getOrderData();

        String orderId = String.valueOf(apiClient.getOrderId());
        String orderCreatedDate = HelperPage.formatCreatedDate(apiClient.getOrderCreatedAt());
        String totalDataPrice = HelperPage.formatPriceRub(String.valueOf(apiClient.getOrderTotalPrice()));

        assertAll(
                "Проверка состава заказа на странице успешно оплаченного заказа",
                () -> assertTrue(orderList.stream().anyMatch(e -> e.getOwnText().replaceAll("\\s+", "")
                                .equals(orderId)), "incorrect order number"),
                () -> assertTrue(orderList.stream().anyMatch(e -> e.getOwnText().trim().equals(orderCreatedDate)),
                        "incorrect order created time"),
                () -> assertEquals(totalDataPrice, totalPrice.text().replaceAll("[ ,.\"\\n]", "").trim() ,
                        "incorrect total price")
        );
        assertTrue(apiClient.getOrderStatus().contains("Оплачен"), "order has not been paid");
        return this;
    }
}