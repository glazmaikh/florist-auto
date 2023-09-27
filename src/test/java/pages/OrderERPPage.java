package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import org.openqa.selenium.JavascriptExecutor;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class OrderERPPage {
    SelenideElement orderSection = $x("//input[@name='id']");
    SelenideElement bouquetSection = $x("//form[@name='main1']");
    private ApiClient apiClient;

    public OrderERPPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public OrderERPPage openOrder(String baseUrl, String orderId) {
        baseUrl = baseUrl.replaceAll("https://www\\.", "");
        String url = "https://s_orlovskiy:SdfrFr5548@" + baseUrl + "dbnew/db/purchase.php?id=" + orderId;
        Selenide.executeJavaScript("window.open('"+ url + "')");
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

    // String recipientName, String address, LocalTime timeFrom, LocalTime timeTo
}
