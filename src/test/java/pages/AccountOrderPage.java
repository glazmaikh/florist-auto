package pages;

import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountOrderPage {
    private final ApiClient apiClient;
    private final SelenideElement nameField = $x("//strong");
    private final SelenideElement table = $("._2hlJfhiV");

    public AccountOrderPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public AccountOrderPage assertCreatedOrderFromAuthUser(String baseUrl, String accountName) {
        webdriver().shouldHave(url(baseUrl + apiClient.getCitySlug() + "/account/orders"));

        table.shouldHave(text(String.valueOf(apiClient.getOrderId())));
        //table.shouldHave(text(HelperPage.deliveryDataRegex(apiClient.getOrderDeliveryDate())));
        table.shouldHave(text(apiClient.getRecipientName()));
        table.shouldHave(text(apiClient.getOrderStatus()));
        assertTrue(apiClient.getOrderStatus().contains("Оплачен"), "order has not been paid");

        assertEquals(accountName, nameField.getText(), "Incorrect account name");
        return this;
    }

    public AccountOrderPage assertAuth(String baseUrl, String accountName) {
        webdriver().shouldHave(url(baseUrl + "account/orders"));
        assertEquals(accountName, nameField.getText(), "Incorrect account name");
        return this;
    }
}
