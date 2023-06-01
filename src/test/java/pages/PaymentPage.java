package pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.*;

public class PaymentPage {
    private final SelenideElement cardNumberInput = $(byName("cardNumber"));
    private final SelenideElement expireInput = $(byName("cardValidity"));
    private final SelenideElement cvcInput = $(byName("cardCvv"));
    private final SelenideElement submitButton = $x("//button[@type='submit']");
    private final SelenideElement confirmSubmitButton = $(byName("SET"));
    private final SelenideElement thanksFor = $x("//h1[text() ='Спасибо за заказ']");
    private final SelenideElement iframeAssist = $x("//div[@id='modal-overlay']//iframe");

    public PaymentPage fillCard() {
        cardNumberInput.sendKeys("4111111111111111");
        expireInput.click();
        expireInput.sendKeys("1223");
        cvcInput.sendKeys("123");
        return this;
    }

    public PaymentPage pay() {
        submitButton.click();
        return this;
    }

    public void confirm() {
        switchTo().frame(iframeAssist);
        confirmSubmitButton.shouldBe(exist).click();
        thanksFor.shouldBe(visible, Duration.ofSeconds(10));
    }
}