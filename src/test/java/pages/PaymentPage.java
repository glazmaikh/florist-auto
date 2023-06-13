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
    private final ElementsCollection orderList = $$x("//div[@class='AEYhRIG-']//div");
    private final SelenideElement successPrice = $x("//main[@id='main']//span");
    private final SelenideElement header = $x("//h1");
    private final ERPPurchaseItemPage erpItemPage = new ERPPurchaseItemPage();
    private final SuccessPage successPage = new SuccessPage();


    public PaymentPage assertOrderList() {
        header.shouldHave(textCaseSensitive("Оплата заказа"));
        assertEquals(HelperPage.getOrderNumber(), header.getText().replaceAll("[^0-9]", ""),
                "incorrect order number on PaymentPage");

        OrderData orderData = HelperPage.getOrderData();

        assertTrue(orderData.getData().getStatus_text().contains("Ожидает оплаты"));

        for (SelenideElement element : orderList) {
            element.shouldHave(text(orderData.getData().getCart().get("0").getName()));
        }

        for (SelenideElement element : orderList) {
            element.shouldHave(text(orderData.getData().getCart().get("0").getVariation()));
        }

        for (SelenideElement element : orderList) {
            element.shouldHave(text(String.valueOf(orderData.getData().getCart().get("0").getCount())));
        }

        for (SelenideElement element : orderList) {
            element.shouldHave(text(orderData.getData().getCart().get("1").getName()));
        }

//        for (SelenideElement element : orderList) {
//            String bouquetName = orderData.getData().getCart().get("0").getName();
//            String variation = orderData.getData().getCart().get("0").getVariation();
//            int count = orderData.getData().getCart().get("0").getCount();
//            String cityName = orderData.getData().getCart().get("1").getName();
//
//            element.shouldHave(text(bouquetName), text(variation), text(String.valueOf(count)), text(cityName));
//        }


        //assertTrue(orderList.stream().anyMatch(e -> e.text().equals(orderData.getData().getCart().get("0").getName())));
        //assertTrue(orderList.stream().anyMatch(e -> e.text().equals(orderData.getData().getTotal().getRUB())));

//        System.out.println(orderData.getData().getCart().get("0").getName() + " name");
//        System.out.println(orderData.getData().getCart().get("0").getVariation() + " variation");
//        System.out.println(orderData.getData().getCart().get("0").getCount() + " count");
//        System.out.println(orderData.getData().getCart().get("0").getPrice().getRUB() + " getPriceRub");
//
//        System.out.println(orderData.getData().getCart().get("1").getName() + " deliveryName");
//        System.out.println(orderData.getData().getCart().get("1").getPrice() + " delivePrice");
//        System.out.println(orderData.getData().getTotal().getRUB() + " getTotalRub");

        return this;
    }

//    public PaymentPage assertOrderList(String bouquetName, int bouquetPrice, int deliveryPrice) {
//        orderList.shouldBe(text(bouquetName));
//        HelperPage.assertBouquetPrice(bouquetPrice, orderListPrices.get(0));
//
//        if (deliveryPrice > 100) {
//            HelperPage.assertDeliveryPrice(deliveryPrice, orderListPrices.get(1));
//            assertThat(HelperPage.priceRegex(orderListPrices.get(2)),
//                    equalTo(String.valueOf(HelperPage.totalPrice(bouquetPrice, deliveryPrice))));
//        } else {
//            orderList.shouldBe(text("бесплатно"));
//            assertThat(HelperPage.priceRegex(orderListPrices.get(2)),
//                    equalTo(String.valueOf(bouquetPrice)));
//        }
//        return this;
//    }
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