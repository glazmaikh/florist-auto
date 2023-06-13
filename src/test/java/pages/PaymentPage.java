package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.HelperPage;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import models.city.CityDataDto;
import models.order.OrderData;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentPage {
    private final SelenideElement cardNumberInput = $(byName("cardNumber"));
    private final SelenideElement expireInput = $(byName("cardValidity"));
    private final SelenideElement cvcInput = $(byName("cardCvv"));
    private final SelenideElement submitButton = $x("//button[@type='submit']");
    private final SelenideElement confirmSubmitButton = $(byName("SET"));
    private final SelenideElement thanksFor = $x("//h1[text() ='Спасибо за заказ']");
    private final SelenideElement iframeAssist = $x("//div[@id='modal-overlay']//iframe");
    private final ElementsCollection orderList = $$x("//div[@class='AEYhRIG-']//span");
    private final SelenideElement successPrice = $x("//main[@id='main']//span");
    private final SelenideElement header = $x("//h1");
    private final ERPPurchaseItemPage erpItemPage = new ERPPurchaseItemPage();
    private final SuccessPage successPage = new SuccessPage();

    @SneakyThrows
    public PaymentPage assertOrderList() {
        header.shouldHave(textCaseSensitive("Оплата заказа"));
        assertEquals(HelperPage.getOrderNumber(), header.getText().replaceAll("[^0-9]", ""),
                "incorrect order number on PaymentPage");

        OrderData orderData = HelperPage.getOrderData();
        String bouquetName =  orderData.getData().getCart().get("0").getName();
        String price = HelperPage.formatPrice(orderData.getData().getCart().get("0").getPrice().getRUB());
        String variation = orderData.getData().getCart().get("0").getVariation();
        String count = orderData.getData().getCart().get("0").getCount() + " шт.";
        String deliveryName = orderData.getData().getCart().get("1").getName();
        String deliveryPrice = HelperPage.formatPrice(orderData.getData().getCart().get("1").getPrice().getRUB());
        String totalPrice = HelperPage.formatPrice(orderData.getData().getTotal().getRUB());

        assertAll(
                "Проверка состава заказа на странице оплаты",
                () -> assertTrue(orderData.getData().getStatus_text().contains("Ожидает оплаты")),
                () -> assertTrue(orderList.stream().anyMatch(e -> e.text().equals(bouquetName)), "incorrect bouquet name"),
                () -> assertTrue(orderList.stream().anyMatch(e -> e.text().equals(variation)), "incorrect bouquet variation"),
                () -> assertTrue(orderList.stream().anyMatch(e -> e.text().equals(count)), "incorrect bouquet count"),
                () -> assertTrue(orderList.stream().anyMatch(e -> e.text().equals(price)), "incorrect bouquet price"),
                () -> assertTrue(orderList.stream().anyMatch(e -> e.text().equals(deliveryName)), "incorrect delivery city"),
                () -> assertTrue(orderList.stream().anyMatch(e -> e.text().equals(deliveryPrice)), "incorrect delivery price"),
                () -> assertTrue(orderList.stream().anyMatch(e -> e.text().equals(totalPrice)), "incorrect total price")
        );
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

    public PaymentPage confirm() {
        iframeAssist.shouldBe(exist, Duration.ofSeconds(15));
        switchTo().frame(iframeAssist);
        confirmSubmitButton.shouldBe(exist, Duration.ofSeconds(15)).click();
        return this;
    }

    public PaymentPage assertRedirectOnSuccessPage() {
        assertTrue(webdriver().driver().url().contains("success"));
        thanksFor.shouldBe(visible, Duration.ofSeconds(15));
        //assertThat(HelperPage.priceRegex(successPrice), equalTo(String.valueOf(HelperPage.totalPrice(bouquetPrice, deliveryPrice))));

        return this;
    }
}