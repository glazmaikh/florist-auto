package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import fixtures.AssertFixturesPage;
import helpers.ApiClient;
import helpers.CurrencyType;
import helpers.DeliveryDateType;
import helpers.HelperPage;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class CheckoutPage {
    private final SelenideElement yourNameInput = $(byName("customerName"));
    private final SelenideElement yourEmailInput = $(byName("customerEmail"));
    private final SelenideElement yourPhoneInput = $(byName("customerPhone"));
    private final SelenideElement nameInput = $(byName("recipientName"));
    private final SelenideElement phoneInput = $(byName("recipientPhone"));
    private final SelenideElement addressDaDataInput = $(byName("recipientAddressSource"));
    private final SelenideElement addressDroppedValue = $x("//span[text()='Выберите вариант или продолжите ввод']/following::div[1]");
    private final SelenideElement addressInput = $(byName("recipientAddress"));
    private final SelenideElement deliveryDateInput = $(byName("deliveryDateSource"));
    private ElementsCollection deliveryAllDays = $$x("//button[contains(@class, 'react-calendar__tile') and not(@disabled)]/abbr");
    private final SelenideElement payButton = $(byText("Оплатить"));
    private final SelenideElement priceSection = payButton.$(".no-wrap");
    private final SelenideElement orderSection = $("._2pTgtswS  ");
    private final SelenideElement header = $x("//h1");
    private final SelenideElement createdOrderText = $("._2fUGBItB");
    private final SelenideElement returnToPayButton = $x("//a[@class='btn']");
    private final SelenideElement nextMonthButton = $(".react-calendar__navigation__next-button");
    private final SelenideElement timeDropped = $(".css-11unzgr");
    private final ElementsCollection timeIntervals = $$x("//div[@class='_2zwatJ-h' and count(span) = 1]");
    private final ElementsCollection timeEarlyIntervals = $$x("//div[@class='_2zwatJ-h' and count(span) = 2]");
    private final SelenideElement timeFromInput = $x("//span[text()='Время доставки с']/parent::label");
    private final SelenideElement timeToInput = $x("//span[text()='До']/parent::label");
    private final SelenideElement removeFromCard = $(byCssSelector("div.YVf8EOae > svg"));
    private final ApiClient apiClient;
    private AssertFixturesPage assertFixturesPage;
    private String deliveryDate;

    public CheckoutPage(ApiClient apiClient, AssertFixturesPage assertFixturesPage) {
        this.apiClient = apiClient;
        this.assertFixturesPage = assertFixturesPage;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public CheckoutPage simpleFillForm(String name, String phone, String address) {
        nameInput.shouldBe(exist, Duration.ofSeconds(10)).val(name);
        phoneInput.val(phone);

        try {
            addressDaDataInput.shouldBe(exist).val(address);
            addressDroppedValue.click();
        } catch (AssertionError e) {
            addressInput.shouldBe(exist).val(address);
            addressDroppedValue.click();
        }
        deliveryDateInput.click();
        return this;
    }

    public CheckoutPage simpleFillForm(String yourName, String yourEmail, String yourPhone, String name, String phone, String address) {
        yourNameInput.shouldBe(exist, Duration.ofSeconds(10)).val(yourName);
        yourEmailInput.val(yourEmail);
        yourPhoneInput.val(yourPhone);
        nameInput.val(name);
        phoneInput.val(phone);

        try {
            addressDaDataInput.shouldBe(exist).val(address);
            addressDroppedValue.click();
        } catch (AssertionError e) {
            addressInput.shouldBe(exist).val(address);
            addressDroppedValue.click();
        }
        deliveryDateInput.click();
        return this;
    }

    public CheckoutPage assertBouquetName() {
        assertTrue(HelperPage.isOrderSectionContainsAllFromBouquets(orderSection, apiClient.getBouquetNameList()),
                "bouquets names not equals");
        return this;
    }

    public CheckoutPage assertDeliveryPrice(CurrencyType currencyType) {
        assertFixturesPage.performAssertDeliveryPrice(orderSection, currencyType);
        return this;
    }

        public CheckoutPage assertBouquetPrice(CurrencyType currencyType, DeliveryDateType deliveryDateType) {
        assertFixturesPage.performAssertBouquetPriceList(orderSection, currencyType, deliveryDateType);
        return this;
    }

    public CheckoutPage assertExtrasPrice(CurrencyType currencyType) {
        assertFixturesPage.performAssertExtrasPrice(orderSection, currencyType);
        return this;
    }

    public CheckoutPage assertTotalPrice(CurrencyType currencyType, DeliveryDateType deliveryDateType) {
        double bouquetPrices = apiClient.getBouquetPriceList(currencyType, deliveryDateType).stream()
                .mapToDouble(Double::parseDouble)
                .sum();

        double extrasPrices = apiClient.getExtrasPriceList(currencyType).stream()
                .mapToDouble(Double::parseDouble)
                .sum();

        double totalPrice = bouquetPrices + extrasPrices;
        if (!apiClient.getDeliveryPrice(currencyType).equals("Бесплатно")) {
            totalPrice += Double.parseDouble(apiClient.getDeliveryPrice(currencyType));
        }
        orderSection.shouldHave(text(HelperPage.priceCurrencyFormat(currencyType, String.valueOf(totalPrice))));
        return this;
    }

    public PaymentPage goToPaymentPage() {
        priceSection.shouldBe(Condition.visible, Duration.ofSeconds(5)).click();
        Selenide.Wait().until(webDriver -> {
            return ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete");
        });
        return new PaymentPage(apiClient, assertFixturesPage);
    }

    public CheckoutPage setRandomDeliveryDate(DeliveryDateType deliveryDateType) throws Exception {
        List<LocalDate> disabledDaysList = apiClient.getDisabledDeliveryDaysList();

        switch (deliveryDateType) {
            case LOW -> deliveryDate = HelperPage.getRandomLowDeliveryDay(disabledDaysList);
            case HiGH_FEBRUARY -> deliveryDate = HelperPage.getRandomHighFebruaryDeliveryDay(disabledDaysList);
            case HIGH_MARCH -> deliveryDate = HelperPage.getRandomHighMarchDeliveryDay(disabledDaysList);
        }

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

    public String setRandomDeliveryTime() {
        apiClient.getDeliveryDateInterval(deliveryDate);
        LocalTime timeFrom = HelperPage.doubleToTime(apiClient.getDeliveryTimeFrom());
        LocalTime timeTo = HelperPage.doubleToTime(apiClient.getDeliveryTimeTo());
        String time = String.valueOf(HelperPage.getRandomTimeInterval(timeFrom, timeTo.minusHours(2)));

        timeFromInput.shouldBe(exist).click();
        timeDropped.shouldBe(exist);
        timeDropped.shouldHave(text(time));
        timeIntervals.findBy(text(time)).click();
        return time;
    }

    public PaymentPage assertOrderAndBackToPay() {
        header.scrollTo().shouldHave(textCaseSensitive("Заказ оформлен"));
        createdOrderText.shouldHave(text(String.valueOf(apiClient.getOrderId())));
        createdOrderText.shouldHave(text(HelperPage.regexMaxPaidDate(apiClient.getMaxPaidDate())));
        //assertTrue(apiClient.getOrderStatus().contains("Ожидает оплаты"));
        assertTrue(getWaitPaidOrderStatus(), "Таймаут. Не получил статус 'Ожидает оплаты' за 30 сек.");
        returnToPayButton.shouldBe(exist).click();
        return new PaymentPage(apiClient, assertFixturesPage);
    }

    private boolean getWaitPaidOrderStatus() {
        long startTime = System.currentTimeMillis();
        long timeoutInMilliseconds = 30000;
        boolean orderPaid = false;

        while (System.currentTimeMillis() - startTime < timeoutInMilliseconds) {
            String orderStatus = apiClient.getOrderStatus();
            if (orderStatus.contains("Ожидает оплаты")) {
                orderPaid = true;
                break;
            } else {
                Selenide.sleep(5000);
            }
        }
        return orderPaid;
    }

    public CheckoutPage removeFromCard() {
        removeFromCard.shouldBe(exist).click();
        header.shouldHave(text("В вашей корзине пока пусто"));
        return this;
    }
}