package pages;

import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import helpers.CurrencyType;
import helpers.HelperPage;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountOrderPage {
    private final ApiClient apiClient;
    private final SelenideElement nameField = $x("//strong");
    private final SelenideElement table = $("._2hlJfhiV");
    private final SelenideElement orderPopUpButton = $("._1y09KZOl");
    private final SelenideElement orderPopUp = $("#modal-overlay");

    public AccountOrderPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public AccountOrderPage assertCreatedOrderExist(String baseUrl) {
        webdriver().shouldHave(url(baseUrl + apiClient.getCitySlug() + "/account/orders"));
        table.shouldHave(text(String.valueOf(apiClient.getOrderId())));
        return this;
    }

    public AccountOrderPage assertBouquetName() {
        orderPopUpButton.shouldBe(exist).click();
        orderPopUp.shouldHave(text(apiClient.getBouquetName()));
        return this;
    }

    public AccountOrderPage assertBouquetPrice(CurrencyType currencyType) {
        orderPopUp.shouldHave(text(HelperPage.priceCurrencyFormat(currencyType, apiClient.getBouquetMinPrice(currencyType))));
        return this;
    }

    public AccountOrderPage assertDeliveryDate() {
        orderPopUp.shouldHave(text(apiClient.getDeliveryDate()));
        return this;
    }

    public AccountOrderPage assertAuth(String baseUrl, String accountName) {
        webdriver().shouldHave(url(baseUrl + "account/orders"));
        assertEquals(accountName, nameField.getText(), "Incorrect account name");
        return this;
    }

    public AccountOrderPage assertRecipientData(String recipientName, String phone, String address) {
        orderPopUp.shouldHave(text(recipientName));
        orderPopUp.shouldHave(text(phone));
        orderPopUp.shouldHave(text(address));
        return this;
    }

    public AccountOrderPage assertDeliveryPrice(CurrencyType currencyType) {
        orderPopUp.shouldHave(text(apiClient.getDeliveryPrice(currencyType)));
        return this;
    }

    public AccountOrderPage assertTotalPrice(CurrencyType currencyType) {
        orderPopUp.shouldHave(text(HelperPage.priceCurrencyFormat(currencyType, apiClient.getOrderTotalPrice(currencyType))));
        return this;
    }
}
