package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import models.bouquet.BouquetDataItemDto;

import java.time.Duration;
import java.util.Random;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderPage {
    private final SelenideElement yourNameInput = $(byName("customerName"));
    private final SelenideElement yourEmailInput = $(byName("customerEmail"));
    private final SelenideElement yourPhoneInput = $(byName("customerPhone"));
    private final SelenideElement nameInput = $(byName("recipientName"));
    private final SelenideElement phoneInput = $(byName("recipientPhone"));
    private final SelenideElement addressInput = $(byName("recipientAddress"));
    private final SelenideElement dateDeliveryInput = $x("//span[text() ='Выберите дату']//preceding-sibling::input");
    private final ElementsCollection deliveryDay =
            $$x("//button[@class = 'react-calendar__tile react-calendar__month-view__days__day' and not(@disabled)]");

    private final SelenideElement payButton = $(byText("Оплатить"));
    private final SelenideElement priceSection = payButton.$(".no-wrap");

    public OrderPage simpleFillForm(String yourName, String yourEmail, String yourPhone, String name, String phone, String address, BouquetDataItemDto bouquet) {
        yourNameInput.val(yourName);
        yourEmailInput.val(yourEmail);
        yourPhoneInput.val(yourPhone);

        nameInput.val(name);
        phoneInput.val(phone);

        addressInput.val(address);
        dateDeliveryInput.click();
        getRandomDeliveryDay(deliveryDay).click();

        /* сделать проверку не на мин цену, а на среднюю
        String price = priceSection.getText().replaceAll(" ", "");
        assertEquals(String.valueOf(bouquet.getMin_price().getRub()), price.substring(0, price.length() - 1), "Incorrect price");
         */
        priceSection.shouldBe(Condition.visible, Duration.ofSeconds(5)).click();
        return this;
    }

    public SelenideElement getRandomDeliveryDay(ElementsCollection collection) {
        return collection.get(new Random().nextInt(collection.size()));
    }
}