package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import helpers.HelperPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PaymentPage {
    private final SelenideElement cardNumberInput = $(byName("cardNumber"));
    private final SelenideElement expireInput = $(byName("cardValidity"));
    private final SelenideElement cvcInput = $(byName("cardCvv"));
    private final SelenideElement submitButton = $x("//button[@type='submit']");
    private final SelenideElement confirmSubmitButton = $(byName("SET"));
    private final SelenideElement thanksFor = $x("//h1[text() ='Спасибо за заказ']");
    private final SelenideElement iframeAssist = $x("//div[@id='modal-overlay']//iframe");
    private final SelenideElement orderList = $(".AEYhRIG-");
    private final ElementsCollection orderListPrices = orderList.$$(".no-wrap");

    public PaymentPage assertOrderList(String bouquetName, int bouquetPrice, int deliveryPrice) {
        orderList.shouldBe(text(bouquetName));
        HelperPage.assertBouquetPrice(bouquetPrice, orderListPrices.get(0));

        if (deliveryPrice > 100) {
            HelperPage.assertDeliveryPrice(deliveryPrice, orderListPrices.get(1));
            assertThat(orderListPrices.get(2).getText().replaceAll("[\\s₽]", ""),
                    equalTo(String.valueOf(HelperPage.totalPrice(bouquetPrice, deliveryPrice))));
        } else {
            orderList.shouldBe(text("бесплатно"));
            assertThat(orderListPrices.get(2).getText().replaceAll("[\\s₽]", ""),
                    equalTo(String.valueOf(bouquetPrice)));
        }
        return this;
    }
    public PaymentPage fillCard(String cardNumber, String expireNumber, String cvcNumber) {
        cardNumberInput.sendKeys(cardNumber);
        expireInput.click();
        expireInput.sendKeys(expireNumber);
        cvcInput.sendKeys(cvcNumber);
        return this;
    }

    public PaymentPage pay() {
        submitButton.click();
        return this;
    }

    public void confirm() {
        iframeAssist.shouldBe(exist, Duration.ofSeconds(15));
        switchTo().frame(iframeAssist);
        confirmSubmitButton.shouldBe(exist, Duration.ofSeconds(15)).click();
        thanksFor.shouldBe(visible, Duration.ofSeconds(15));
    }
}