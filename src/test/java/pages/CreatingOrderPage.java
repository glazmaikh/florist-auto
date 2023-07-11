package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import helpers.HelperPage;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreatingOrderPage {
    private final SelenideElement yourNameInput = $(byName("customerName"));
    private final SelenideElement yourEmailInput = $(byName("customerEmail"));
    private final SelenideElement yourPhoneInput = $(byName("customerPhone"));
    private final SelenideElement nameInput = $(byName("recipientName"));
    private final SelenideElement phoneInput = $(byName("recipientPhone"));
    private final SelenideElement addressDaDataInput = $(byName("recipientAddressSource"));
    private final SelenideElement addressInput = $(byName("recipientAddress"));
    private final SelenideElement deliveryDateInput = $(byName("deliveryDateSource"));
    private final ElementsCollection deliveryAllDays = $$x("//button[contains(@class, 'react-calendar__tile') and not(@disabled)]/abbr");
    private final SelenideElement payButton = $(byText("Оплатить"));
    private final SelenideElement priceSection = payButton.$(".no-wrap");
    private final SelenideElement orderList = $("._2pTgtswS  ");
    private final ElementsCollection orderListPrices = orderList.$$(".no-wrap");
    private final ApiClient apiClient;

    public CreatingOrderPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public CreatingOrderPage simpleFillForm(String name, String phone, String address) {
        nameInput.shouldBe(exist, Duration.ofSeconds(10)).val(name);
        phoneInput.val(phone);

        try {
            addressDaDataInput.shouldBe(exist).val(address);
        } catch (AssertionError e) {
            addressInput.shouldNotBe(hidden).val(address);
        }

        deliveryDateInput.click();
        return this;
    }

    public CreatingOrderPage simpleFillForm(String yourName, String yourEmail, String yourPhone, String name, String phone, String address) {
        yourNameInput.shouldBe(exist, Duration.ofSeconds(10)).val(yourName);
        yourEmailInput.val(yourEmail);
        yourPhoneInput.val(yourPhone);
        nameInput.val(name);
        phoneInput.val(phone);

        try {
            addressDaDataInput.shouldBe(exist).val(address);
        } catch (AssertionError e) {
            addressInput.shouldNotBe(hidden).val(address);
        }

        deliveryDateInput.click();
        return this;
    }

    public CreatingOrderPage assertOrderList() {
        orderList.shouldBe(text(apiClient.getBouquetName()));
        HelperPage.assertPrice(apiClient.getBouquetPrice(), orderListPrices.get(0));

        int deliveryPrice = HelperPage.doubleToIntRound(apiClient.getDeliveryPrice());
        if (deliveryPrice > 100) {
            HelperPage.assertPrice(deliveryPrice, orderListPrices.get(1));
            assertThat(HelperPage.priceRegex(orderListPrices.get(2)),
                    equalTo(String.valueOf(HelperPage.totalPrice(apiClient.getBouquetPrice(), deliveryPrice))));
        } else {
            orderList.shouldBe(text("бесплатно"));
            assertThat(HelperPage.priceRegex(orderListPrices.get(1)),
                    equalTo(String.valueOf(apiClient.getBouquetPrice())));
        }
        return this;
    }

    public PaymentPage pressPayButton() {
        priceSection.shouldBe(Condition.visible, Duration.ofSeconds(5)).click();

        Selenide.Wait().until(webDriver -> {
            return ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete");
        });
        return new PaymentPage(apiClient);
    }

    // 1. сделать тесты для выбора конкретного дня
    // 2. указать время доставки
    // 3. вызвать getRandomDay
    public CreatingOrderPage getDeliveryDateWithoutDisabled() {
        List<String> disabledDaysList = apiClient.getDisabledDeliveryDaysList();
        List<String> convertedDisabledDaysList = HelperPage.convertDates(disabledDaysList);

        List<String> uiDaysList = HelperPage.getListFromAriaLabelAttribute(deliveryAllDays);
        uiDaysList.removeAll(convertedDisabledDaysList);

        String randomDeliveryDay = HelperPage.getRandomStringFromList(uiDaysList);
        deliveryAllDays.filterBy(Condition.attribute("aria-label", randomDeliveryDay))
                .first()
                .click();
        return this;
    }
}