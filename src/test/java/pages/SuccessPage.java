package pages;

import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import helpers.CurrencyType;
import helpers.HelperPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.textCaseSensitive;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.*;

public class SuccessPage {
    private final SelenideElement header = $x("//h1");
    private final SelenideElement orderSection = $("._3QUYNQ-9");
    private final ApiClient apiClient;

    public SuccessPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public SuccessPage assertSuccessCreatedOrder(CurrencyType currencyType) {
        apiClient.getOrderData();
        String orderId = String.valueOf(apiClient.getOrderId());
        String totalDataPrice = HelperPage.priceCurrencyFormat(currencyType, apiClient.getOrderTotalPrice(currencyType));
        String orderCreatedDate = HelperPage.formatCreatedDate(apiClient.getOrderCreatedAt());

        orderSection.shouldHave(text(orderId));
        orderSection.shouldHave(text(totalDataPrice));
        orderSection.shouldHave(text(orderCreatedDate));
        return this;
    }

    public SuccessPage assertSuccessOrderStatus(String baseUrl) {
        header.shouldHave(textCaseSensitive("Спасибо за заказ"), Duration.ofSeconds(20));
        apiClient.getOrderData();
        webdriver().shouldHave(url(baseUrl + apiClient.getCitySlug() + "/order/payment/" + HelperPage.getOrderNumber() + "/success/" + HelperPage.getOrderAccessKey()), Duration.ofSeconds(20));
        assertTrue(apiClient.getOrderStatus().contains("Оплачен"), "order has not been paid");
        return this;
    }
}