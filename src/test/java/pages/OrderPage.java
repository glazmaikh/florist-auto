package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.selector.ByText;

import java.time.Duration;
import java.util.Random;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class OrderPage {
    private final SelenideElement yourNameInput = $(byName("customerName"));
    private final SelenideElement yourEmailInput = $(byName("customerEmail"));
    private final SelenideElement yourPhoneInput = $(byName("customerPhone"));
    private final SelenideElement nameInput = $(byName("recipientName"));
    private final SelenideElement phoneInput = $(byName("recipientPhone"));
    private final SelenideElement addressInput = $(byName("recipientAddressSource"));
    private final SelenideElement addressDroppedElement = $("._3e9oSM9z");
    private final SelenideElement dateDeliveryInput = $(byText("Выберите дату"));
    private final ElementsCollection deliveryDay =
            $$x("//button[@class = 'react-calendar__tile react-calendar__month-view__days__day' and not(@disabled)]");

    public OrderPage simpleFillForm(String yourName, String yourEmail, String yourPhone, String name, String phone, String address) {
        yourNameInput.val(yourName);
        yourEmailInput.val(yourEmail);
        yourPhoneInput.val(yourPhone);

        nameInput.val(name);
        phoneInput.val(phone);

        addressInput.val(address);
        addressDroppedElement.shouldBe(Condition.exist, Duration.ofSeconds(5)).click();
        dateDeliveryInput.click();
        getRandomDeliveryDay().click();
        return this;
    }

    public SelenideElement getRandomDeliveryDay() {
        return deliveryDay.get(new Random().nextInt());
    }
}