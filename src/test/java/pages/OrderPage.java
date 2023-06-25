package pages;

import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderPage {
    private final ApiClient apiClient;
    private final SelenideElement nameField = $x("//strong");
    public OrderPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public OrderPage assertAuth(String baseUrl, String accountName) {
        webdriver().shouldHave(url(baseUrl + "account/orders"));
        assertEquals(accountName, nameField.getText(), "Incorrect account name");
        return this;
    }

}
