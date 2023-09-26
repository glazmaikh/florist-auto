package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import org.openqa.selenium.JavascriptExecutor;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class OrderERPPage {
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
}
