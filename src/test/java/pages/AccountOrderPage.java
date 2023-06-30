package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import helpers.HelperPage;
import models.order.OrderData;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountOrderPage {
    private final ApiClient apiClient;
    private final SelenideElement nameField = $x("//strong");
    private final ElementsCollection table = $$x("//td//span");
    public AccountOrderPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public AccountOrderPage assertCreatedOrder(String baseUrl, String accountName) {
        webdriver().shouldHave(url(baseUrl + apiClient.getSlug() + "/account/orders"));

        //OrderData orderData = apiClient.getOrderData();

        String orderId = String.valueOf(apiClient.getOrderId());
        String orderDeliveryDate = apiClient.getOrderDeliveryDate();
        String orderStatus = apiClient.getOrderStatus();

        System.out.println(orderId);
        System.out.println(orderDeliveryDate);
        System.out.println(accountName);
        System.out.println(orderStatus);

        assertAll(
                "Проверка состава заказа на странице успешно оплаченного заказа",
                () -> assertTrue(table.stream().anyMatch(e -> e.getOwnText().replaceAll("\\s+", "")
                        .equals(orderId)), "incorrect order number"),
//                () -> assertTrue(table.stream().anyMatch(e -> e.getOwnText().trim().equals(orderCreatedDate)),
//                        "incorrect order created time"),
                () -> assertTrue(table.stream().anyMatch(e -> e.getOwnText().equals(accountName)),
                        "incorrect order created time")
        );
        assertTrue(apiClient.getOrderStatus().contains("Оплачен"), "order has not been paid");

        //assertEquals(accountName, nameField.getText(), "Incorrect account name");
        return this;
    }

    public AccountOrderPage assertAuth(String baseUrl, String accountName) {
        webdriver().shouldHave(url(baseUrl + "account/orders"));
        assertEquals(accountName, nameField.getText(), "Incorrect account name");
        return this;
    }
}
