package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import helpers.CurrencyType;
import helpers.HelperPage;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderERPPage {
    SelenideElement orderSection = $x("//input[@name='id']");
    SelenideElement bouquetSection = $x("//form[@name='main1']");
    SelenideElement recipientSection = $x("//td[@style='word-break: break-all']");
    SelenideElement addressSection = $("#card-recipient-address");
    SelenideElement paymentCompleteCheckbox = $("#seed1");
    SelenideElement deliveryTimeSection = $x("//font[@size='+1']");
    private ApiClient apiClient;

    public OrderERPPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public OrderERPPage openOrder(String baseUrl, String orderId) {
        baseUrl = baseUrl.replaceAll("https://www\\.", "");
        String url = "https://s_orlovskiy:SdfrFr5548@" + baseUrl + "dbnew/db/purchase.php?id=" + orderId;
        Selenide.executeJavaScript("window.open('" + url + "')");
        Selenide.switchTo().window(1);
        return this;
    }

    public OrderERPPage assertBouquetInfo(String orderId, CurrencyType currencyType) {
        orderSection.shouldHave(attribute("value", orderId));
        HelperPage.isOrderSectionContainsAllFromBouquets(bouquetSection, apiClient.getBouquetIds());
        HelperPage.isOrderSectionContainsAllFromBouquets(bouquetSection, apiClient.getBouquetNames());
        HelperPage.isOrderSectionContainsAllFromBouquets(bouquetSection, apiClient.getBouquetPrices(currencyType));
        return this;
    }

    public OrderERPPage assertPrices(CurrencyType currencyType, String totalPrice) {
        bouquetSection.shouldHave(text(apiClient.getDeliveryPrices(currencyType)));
        recipientSection.shouldHave(text(totalPrice));
        bouquetSection.shouldHave(text(apiClient.getDeliveryDate()));
        return this;
    }

    public OrderERPPage assertPaymentCompletedChecked() {
        paymentCompleteCheckbox.shouldBe(checked, Duration.ofSeconds(20));
        return this;
    }

    public OrderERPPage assertRecipientInfo(String deliveryTimeFrom) {
        recipientSection.shouldHave(text(apiClient.getRecipientName()));
        assertTrue(addressSection.getText().contains(apiClient.getRecipientAddress()));
        recipientSection.shouldHave(text(apiClient.getRecipientPhone()));
        deliveryTimeSection.shouldHave(text(deliveryTimeFrom.replaceAll("^0", "")));
        return this;
    }
}