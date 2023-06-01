package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;
import java.util.Random;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.hidden;
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
    private final SelenideElement addressInput1 = $(byName("recipientAddress"));
    private final SelenideElement dateDeliveryInput = $x("//span[text()='Выберите дату']//preceding-sibling::input");
    //input[@type='submit'
    private final ElementsCollection deliveryDay =
            $$x("//div[@class='react-calendar__month-view__days']//button[not(@disabled)]");

    private final SelenideElement payButton = $(byText("Оплатить"));
    private final SelenideElement priceSection = payButton.$(".no-wrap");

    public OrderPage simpleFillForm(String yourName, String yourEmail, String yourPhone, String name, String phone, String address) {
        yourNameInput.val(yourName);
        yourEmailInput.val(yourEmail);
        yourPhoneInput.val(yourPhone);

        nameInput.val(name);
        phoneInput.val(phone);

        try {
            addressInput.shouldBe(exist).val(address);
        } catch (AssertionError e) {
            addressInput1.shouldNotBe(hidden).val(address);
        }

        dateDeliveryInput.click();
        getRandomDeliveryDay(deliveryDay).click();

        // сделать проверку не на мин цену, а на среднюю (десериализация json get bouquet)
        priceSection.shouldBe(Condition.visible, Duration.ofSeconds(5)).click();
        return this;
    }

    public SelenideElement getRandomDeliveryDay(ElementsCollection collection) {
        return collection.get(new Random().nextInt(collection.size()));
    }
}