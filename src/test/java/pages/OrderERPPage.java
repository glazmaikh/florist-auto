package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.Matchers.endsWith;
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

    public OrderERPPage assertBouquetInfo(String orderId, String bouquetId, String bouquetName, String bouquetPrice) {
        System.out.println(orderId + " orderId");
        orderSection.shouldHave(attribute("value", orderId));
        bouquetSection.shouldHave(text(bouquetId));
        bouquetSection.shouldHave(text(bouquetName));
        bouquetSection.shouldHave(text(bouquetPrice));
        return this;
    }

    public OrderERPPage assertPrices(String deliveryPrice, String totalPrice, String deliveryDate) {
        bouquetSection.shouldHave(text(deliveryPrice));
        bouquetSection.shouldHave(text(totalPrice));
        bouquetSection.shouldHave(text(deliveryDate));
        return this;
    }

    public OrderERPPage assertPaymentCompletedChecked() {
        paymentCompleteCheckbox.shouldBe(checked, Duration.ofSeconds(20));
        return this;
    }

    public OrderERPPage assertRecipientInfo(String recipientName, String address, String phone, String deliveryTimeFrom) {
        recipientSection.shouldHave(text(recipientName));
        assertTrue(addressSection.getText().contains(address));
        recipientSection.shouldHave(text(phone));
        deliveryTimeSection.shouldHave(text(deliveryTimeFrom));
        return this;
    }
}