package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;
import java.util.Random;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        //передавать ВСЕ недизейбл дни
        getRandomDeliveryDay(deliveryDay).click();

        // сделать проверку не на мин цену, а на среднюю (десериализация json get bouquet)
        priceSection.shouldBe(Condition.visible, Duration.ofSeconds(5)).click();
        return this;
    }

    public OrderPage assertOrderList(String bouquetName, int bouquetPrice, String deliveryPrice) {
        orderList.shouldBe(text(bouquetName));

        assertAll("Prices of Order List",
                () -> assertTextEquals(String.valueOf(bouquetPrice), orderListPrices.get(0)),
                () -> assertTextEquals(String.valueOf(deliveryPrice), orderListPrices.get(1))
//                () -> assertTextEquals(bouquetPrice + deliveryPrice), orderListPrices.get(1)
        );
        return this;
    }

    private void assertTextEquals(String expected, SelenideElement element) {
        String actual = element.getText().replaceAll("[\\s₽]", "");
        assertEquals(expected, actual);
    }

    public SelenideElement getRandomDeliveryDay(ElementsCollection collection) {
        return collection.get(new Random().nextInt(collection.size()));
    }
}