package pages;

import com.codeborne.selenide.Selenide;
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

    public OrderERPPage assertSuccessCreatedOrder(CurrencyType currencyType) {
        apiClient.getOrderData();
        String orderId = String.valueOf(apiClient.getOrderId());
        String totalDataPrice = HelperPage.priceCurrencyFormat(currencyType, apiClient.getOrderTotalPrice(currencyType));
        String orderCreatedDate = HelperPage.formatCreatedDate(apiClient.getOrderCreatedAt());

        orderSection.shouldHave(text(orderId));
        orderSection.shouldHave(text(totalDataPrice));
        orderSection.shouldHave(text(orderCreatedDate));
        return new OrderERPPage(apiClient);
    }

    public SuccessPage assertSuccessOrderStatus(String baseUrl) {
        header.shouldHave(textCaseSensitive("Спасибо за заказ"), Duration.ofSeconds(20));
        apiClient.getOrderData();
        webdriver().shouldHave(url(baseUrl + apiClient.getCitySlug() + "/order/payment/" + HelperPage.getOrderNumber() + "/success/" + HelperPage.getOrderAccessKey()), Duration.ofSeconds(20));
        assertTrue(getPaidOrderStatus(), "Таймаут. Не получил статус 'Заказ оплачен' за 30 сек.");
        return this;
    }

    public SuccessPage assertSuccessPSOrderStatus(String baseUrl) {
        header.shouldHave(textCaseSensitive("Спасибо за заказ"), Duration.ofSeconds(20));
        apiClient.getOrderData();
        webdriver().shouldHave(url(baseUrl + apiClient.getCityPSSlug() + "/order/payment/" + HelperPage.getOrderNumber() + "/success/" + HelperPage.getOrderAccessKey()), Duration.ofSeconds(20));
        assertTrue(getPaidOrderStatus(), "Таймаут. Не получил статус 'Заказ оплачен' за 30 сек.");
        return this;
    }

    private boolean getPaidOrderStatus() {
        long startTime = System.currentTimeMillis();
        long timeoutInMilliseconds = 30000;
        boolean orderPaid = false;

        while (System.currentTimeMillis() - startTime < timeoutInMilliseconds) {
            String orderStatus = apiClient.getOrderStatus();
            if (orderStatus.contains("Оплачен")) {
                orderPaid = true;
                break;
            } else {
                System.out.println("Ожидание оплаты...");
                Selenide.sleep(5000);
            }
        }
        return orderPaid;
    }
}