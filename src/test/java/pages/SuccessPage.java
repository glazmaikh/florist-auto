package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import helpers.HelperPage;
import lombok.SneakyThrows;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.*;

public class SuccessPage {
    private final SelenideElement orderList = $("._3QUYNQ-9");
    private final ApiClient apiClient;

    public SuccessPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @SneakyThrows
    public SuccessPage assertSuccessCreatedOrder(String baseUrl) {
        webdriver().shouldHave(url(baseUrl + apiClient.getCitySlug() + "/order/payment/" + HelperPage.getOrderNumber() + "/success/" + HelperPage.getOrderAccessKey()), Duration.ofSeconds(20));
        apiClient.getOrderData();

        String orderId = String.valueOf(apiClient.getOrderId());
        String orderCreatedDate = HelperPage.formatCreatedDate(apiClient.getOrderCreatedAt());
        String totalDataPrice = HelperPage.formatPriceRub(String.valueOf(apiClient.getOrderTotalPrice()));

        orderList.shouldHave(text(orderId));
        orderList.shouldHave(text(orderCreatedDate));
        orderList.shouldHave(text(totalDataPrice));

        assertTrue(apiClient.getOrderStatus().contains("Оплачен"), "order has not been paid");
        return this;
    }
}