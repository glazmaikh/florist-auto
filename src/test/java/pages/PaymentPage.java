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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private final SelenideElement successPrice = $x("//main[@id='main']//span");
    private final SelenideElement header = $x("//h1");
    private final ERPPurchaseItemPage erpItemPage = new ERPPurchaseItemPage();
    private final SuccessPage successPage = new SuccessPage();

    @SneakyThrows
    public PaymentPage assertOrderNumber() {
        header.shouldHave(textCaseSensitive("Оплата заказа"));
        assertEquals(HelperPage.getOrderNumber(), header.getText().replaceAll("[^0-9]", ""),
                "incorrect order number on PaymentPage");

        RequestSpecification httpRequest = RestAssured.given();
        Response responseOrderData = httpRequest
                .auth().basic("florist_api", "123")
                .param("id", HelperPage.getOrderNumber())
                .param("access_key", HelperPage.getOrderAccessKey())
                .get("http://www.test.florist.local/api/order/byAccessKey");
        ResponseBody orderBody = responseOrderData.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        OrderData orderData = objectMapper.readValue(orderBody.asString(), OrderData.class);

        System.out.println(HelperPage.getOrderNumber());
        System.out.println(HelperPage.getOrderAccessKey());

        System.out.println(orderData.getData().getCart().get("0").getName());
        System.out.println(orderData.getData().getCart().get("1").getName());
        return this;
    }

    public PaymentPage assertOrderList(String bouquetName, int bouquetPrice, int deliveryPrice) {
        orderList.shouldBe(text(bouquetName));
        HelperPage.assertBouquetPrice(bouquetPrice, orderListPrices.get(0));

        if (deliveryPrice > 100) {
            HelperPage.assertDeliveryPrice(deliveryPrice, orderListPrices.get(1));
            assertThat(HelperPage.priceRegex(orderListPrices.get(2)),
                    equalTo(String.valueOf(HelperPage.totalPrice(bouquetPrice, deliveryPrice))));
        } else {
            orderList.shouldBe(text("бесплатно"));
            assertThat(HelperPage.priceRegex(orderListPrices.get(2)),
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

    public ERPPurchaseItemPage assertERP() {
        String path = webdriver().driver().url();
        Pattern pattern = Pattern.compile("payment/(\\d+)/success/(\\w+)");
        Matcher matcher = pattern.matcher(path);

        if (matcher.find()) {
            String paymentId = matcher.group(1); // 30814813
            String successId = matcher.group(2); // d1206c16486d07396b8c63d5038f11bf

            System.out.println(paymentId);
            System.out.println(successId);
        }
        return erpItemPage;
    }
}