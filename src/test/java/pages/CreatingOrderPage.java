package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import helpers.HelperPage;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

public class CreatingOrderPage {
    private final SelenideElement yourNameInput = $(byName("customerName"));
    private final SelenideElement yourEmailInput = $(byName("customerEmail"));
    private final SelenideElement yourPhoneInput = $(byName("customerPhone"));
    private final SelenideElement nameInput = $(byName("recipientName"));
    private final SelenideElement phoneInput = $(byName("recipientPhone"));
    private final SelenideElement addressDaDataInput = $(byName("recipientAddressSource"));
    private final SelenideElement addressInput = $(byName("recipientAddress"));
    private final SelenideElement deliveryDateInput = $(byName("deliveryDateSource"));
    private ElementsCollection deliveryAllDays = $$x("//button[contains(@class, 'react-calendar__tile') and not(@disabled)]/abbr");
    private final SelenideElement payButton = $(byText("Оплатить"));
    private final SelenideElement priceSection = payButton.$(".no-wrap");
    private final SelenideElement orderList = $("._2pTgtswS  ");
    private final ElementsCollection orderListPrices = orderList.$$(".no-wrap");
    private final SelenideElement header = $x("//h1");
    private final SelenideElement createdOrderText = $("._2fUGBItB");
    private final SelenideElement returnToPayButton = $x("//a[@class='btn']");
    private final SelenideElement nextMonthButton = $(".react-calendar__navigation__next-button");
    private final SelenideElement timeDropped = $(".css-oboqqt-menu");
    private final ElementsCollection timeIntervals = $$x("//div[@class='_2zwatJ-h' and count(span) = 1]");
    private final ElementsCollection timeEarlyIntervals = $$x("//div[@class='_2zwatJ-h' and count(span) = 2]");
    private final SelenideElement timeFromInput = $x("//span[text()='Время доставки с']/parent::label");
    private final SelenideElement timeToInput = $x("//span[text()='До']/parent::label");
    private final ApiClient apiClient;
    private String deliveryDate;

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
        assertTrue(HelperPage.isOrderListContainsAllFromBouquets(orderList, apiClient.getBouquetNameList()),"bouquets names not equals");

        List<String> bouquetsPrices = apiClient.getBouquetPriceRubList().stream()
                .map(String::valueOf)
                .map(HelperPage::priceRegexRub)
                .toList();
        assertTrue(HelperPage.isOrderListContainsAllFromBouquets(orderList, bouquetsPrices), "bouquets prices not equals");

        int deliveryPrice = HelperPage.doubleToIntRound(apiClient.getDeliveryPrice());
        int totalPrice = HelperPage.sumIntegerList(apiClient.getBouquetPriceRubList());
        if (deliveryPrice > 100) {
            assertTrue(orderList.getText().contains(String.valueOf(deliveryPrice)), "delivery prices not equals");

            totalPrice += deliveryPrice;
            assertTrue(orderList.getText().contains(HelperPage.priceRegexRub(String.valueOf(totalPrice))), "total price not equals");
        } else {
            orderList.shouldBe(text("бесплатно"));
            assertTrue(orderList.getText().contains(HelperPage.priceRegexRub(String.valueOf(totalPrice))), "total price not equals");
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
    public CreatingOrderPage getRandomDeliveryDate() {
        List<String> disabledDaysList = apiClient.getDisabledDeliveryDaysList();
        deliveryDate = HelperPage.getRandomDeliveryDayWithoutDisabled(disabledDaysList);

        boolean foundDate = false;
        while (!foundDate) {
            for (SelenideElement se : deliveryAllDays) {
                if (Objects.requireNonNull(se.getAttribute("aria-label")).contains(HelperPage.formatDateDeliveryDateParse(deliveryDate))) {
                    se.shouldBe(exist).click();
                    foundDate = true;
                    break;
                }
            }
            if (!foundDate) {
                nextMonthButton.shouldBe(exist).click();
                deliveryAllDays = $$x("//button[contains(@class, 'react-calendar__tile') and not(@disabled)]/abbr");
            }
        }
        return this;
    }

    public CreatingOrderPage getRandomDeliveryTime() {
        apiClient.getDeliveryDateInterval(deliveryDate);
        LocalTime timeFrom = HelperPage.doubleToTime(apiClient.getDeliveryTimeFrom());
        LocalTime timeTo = HelperPage.doubleToTime(apiClient.getDeliveryTimeTo());
        String time = HelperPage.getRandomTimeInterval(timeFrom, timeTo);
        String timeBeforeInterval = timeTo.plusMinutes(15).toString();
        String timeAfterInterval = timeFrom.minusMinutes(15).toString();

        timeFromInput.shouldBe(exist).click();
        timeDropped.shouldBe(exist);

        assertTrue(timeIntervals.stream().anyMatch(e -> e.getText().equals(time)), "time intervals contains correct delivery time");
        assertTrue(timeIntervals.stream().noneMatch(e -> e.getText().equals(timeBeforeInterval)), "time intervals not contains before time delivery");
        assertTrue(timeIntervals.stream().noneMatch(e -> e.getText().equals(timeAfterInterval)), "time intervals not contains after time delivery");

        for (SelenideElement se : timeIntervals) {
            if (se.getText().equals(time)) {
                se.shouldBe(exist).click();
                break;
            }
        }

        return this;
    }

    public PaymentPage assertOrderAndBackToPay() {
        header.scrollTo().shouldHave(textCaseSensitive("Заказ оформлен"));
        createdOrderText.shouldHave(text(String.valueOf(apiClient.getOrderId())));
        createdOrderText.shouldHave(text(HelperPage.regexMaxPaidDate(apiClient.getMaxPaidDate())));
        assertTrue(apiClient.getOrderStatus().contains("Ожидает оплаты"));

        returnToPayButton.shouldBe(exist).click();
        return new PaymentPage(apiClient);
    }
}