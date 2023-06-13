package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import helpers.HelperPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class OrderPage {
    private final SelenideElement yourNameInput = $(byName("customerName"));
    private final SelenideElement yourEmailInput = $(byName("customerEmail"));
    private final SelenideElement yourPhoneInput = $(byName("customerPhone"));
    private final SelenideElement nameInput = $(byName("recipientName"));
    private final SelenideElement phoneInput = $(byName("recipientPhone"));
    private final SelenideElement addressDaDataInput = $(byName("recipientAddressSource"));
    private final SelenideElement addressInput = $(byName("recipientAddress"));
    private final SelenideElement dateDeliveryInput = $x("//span[text()='Выберите дату']//preceding-sibling::input");
    private final ElementsCollection deliveryDay =
            $$x("//div[@class='react-calendar__month-view__days']//button[not(@disabled)]");
    private final SelenideElement payButton = $(byText("Оплатить"));
    private final SelenideElement priceSection = payButton.$(".no-wrap");
    private final SelenideElement orderList = $("._2pTgtswS  ");
    private final ElementsCollection orderListPrices = orderList.$$(".no-wrap");
    private final PaymentPage paymentPage = new PaymentPage();

    public OrderPage simpleFillForm(String yourName, String yourEmail, String yourPhone, String name, String phone, String address) {
        yourNameInput.val(yourName);
        yourEmailInput.val(yourEmail);
        yourPhoneInput.val(yourPhone);
        nameInput.val(name);
        phoneInput.val(phone);

        try {
            addressDaDataInput.shouldBe(exist).val(address);
        } catch (AssertionError e) {
            addressInput.shouldNotBe(hidden).val(address);
        }

        dateDeliveryInput.click();
        return this;
    }

    public PaymentPage pressPayButton() {
        priceSection.shouldBe(Condition.visible, Duration.ofSeconds(5)).click();

        Selenide.Wait().until(webDriver -> {
            return ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete");
        });
        return paymentPage;
    }

    public void assertOrderList(String bouquetName, int bouquetPrice, int deliveryPrice) {
        orderList.shouldBe(text(bouquetName));
        HelperPage.assertBouquetPrice(bouquetPrice, orderListPrices.get(0));

        if (deliveryPrice > 100) {
            HelperPage.assertDeliveryPrice(deliveryPrice, orderListPrices.get(1));
            assertThat(HelperPage.priceRegex(orderListPrices.get(2)),
                    equalTo(String.valueOf(HelperPage.totalPrice(bouquetPrice, deliveryPrice))));
        } else {
            orderList.shouldBe(text("бесплатно"));
            assertThat(HelperPage.priceRegex(orderListPrices.get(2)), equalTo(String.valueOf(bouquetPrice)));
        }
    }

    // 1. передавать ВСЕ недизейбл дни
    // 2. сделать тесты для выбора конкретного дня
    public String getRandomDeliveryDay() {
        SelenideElement randomDeliveryDay = deliveryDay.get(new Random().nextInt(deliveryDay.size()));
        randomDeliveryDay.click();
        return randomDeliveryDay.getText();
    }
}