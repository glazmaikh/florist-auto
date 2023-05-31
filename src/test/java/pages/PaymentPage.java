package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class PaymentPage {
    private final SelenideElement cardNumberInput = $(byName("cardNumber"));
    private final SelenideElement expireInput = $(byName("cardValidity"));
    private final SelenideElement cvcInput = $(byName("cardCvv"));
    private final SelenideElement submitButton = $x("//button[@type='submit']");
    private final SelenideElement confirmSubmitButton = $x("//input[@name='SET']");
    private final SelenideElement thanksFor = $x("//h1[text() ='Спасибо за заказ']");

    public PaymentPage fillCard() {
        cardNumberInput.sendKeys("");
        expireInput.click();
        expireInput.sendKeys("");
        cvcInput.sendKeys("");
        return this;
    }

    public PaymentPage pay() {
        submitButton.click();
        return this;
    }

    public void confirm() {
        confirmSubmitButton.shouldBe(exist, Duration.ofSeconds(20))
                .click();
        thanksFor.shouldBe(visible);
    }
}
