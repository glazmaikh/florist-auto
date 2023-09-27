package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;

import java.time.Duration;
import java.time.LocalTime;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class OrderERPPage {
    SelenideElement orderSection = $x("//input[@name='id']");
    SelenideElement bouquetSection = $x("//form[@name='main1']");
    SelenideElement recipientSection = $x("//td[@style='word-break: break-all']");
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

    public OrderERPPage assertRecipientInfo(String recipientName, String address, String phone, LocalTime timeFrom, LocalTime timeTo) {
        recipientSection.shouldHave(text(recipientName));
        recipientSection.shouldHave(text(address));
        recipientSection.shouldHave(text(phone));
        deliveryTimeSection.shouldHave(text(String.valueOf(timeFrom)));
        deliveryTimeSection.shouldHave(text(String.valueOf(timeTo)));
        return this;
    }
}