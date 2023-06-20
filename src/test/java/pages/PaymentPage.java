package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import helpers.HelperPage;
import lombok.SneakyThrows;
import models.order.OrderData;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentPage {
    private final SelenideElement cardNumberInput = $(byName("cardNumber"));
    private final SelenideElement expireInput = $(byName("cardValidity"));
    private final SelenideElement cvcInput = $(byName("cardCvv"));
    private final SelenideElement submitButton = $x("//button[@type='submit']");
    private final SelenideElement confirmSubmitButton = $(byName("SET"));
    private final SelenideElement iframeAssist = $x("//div[@id='modal-overlay']//iframe");
    private final ElementsCollection orderList = $$x("//div[@class='AEYhRIG-']//span");
    private final SelenideElement header = $x("//h1");
    private final SelenideElement thanksFor = $x("//h1[text() ='Спасибо за заказ']");
    private final ApiClient apiClient;

    public PaymentPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @SneakyThrows
    public PaymentPage assertOrderList() {
        header.shouldHave(textCaseSensitive("Оплата заказа"));

        OrderData orderData = apiClient.getOrderData();
        assertEquals(String.valueOf(orderData.getData().getId()), header.getText().replaceAll("[^0-9]", ""),
                "incorrect order number on PaymentPage");

        String bouquetName =  orderData.getData().getCart().get("0").getName();
        String price = HelperPage.formatPrice(orderData.getData().getCart().get("0").getPrice().getRUB());
        String variation = orderData.getData().getCart().get("0").getVariation();
        String count = orderData.getData().getCart().get("0").getCount() + " шт.";
        String deliveryDateData = "Доставка (" + orderData.getData().getRecipient_city() + ") " + orderData.getData().getDelivery_date();
        String deliveryPrice = HelperPage.formatPrice(orderData.getData().getCart().get("1").getPrice().getRUB());
        String totalPrice = HelperPage.formatPrice(orderData.getData().getTotal().getRUB());

        if (!deliveryPrice.equals("0")) {
            assertTrue(orderList.stream().anyMatch(e -> e.text().equals(deliveryPrice)), "incorrect delivery price");
        } else {
            assertTrue(orderList.stream().anyMatch(e -> e.text().equals("Бесплатно")), "incorrect delivery price");
        }

        assertAll(
                "Проверка состава заказа на странице оплаты",
                () -> assertTrue(orderData.getData().getStatus_text().contains("Ожидает оплаты")),
                () -> assertTrue(orderList.stream().anyMatch(e -> e.text().equals(bouquetName)), "incorrect bouquet name"),
                () -> assertTrue(orderList.stream().anyMatch(e -> e.text().equals(variation)), "incorrect bouquet variation"),
                () -> assertTrue(orderList.stream().anyMatch(e -> e.text().equals(count)), "incorrect bouquet count"),
                () -> assertTrue(orderList.stream().anyMatch(e -> e.text().equals(price)), "incorrect bouquet price"),
                () -> assertTrue(orderList.stream().anyMatch(e -> e.text().equals(deliveryDateData)), "incorrect delivery city"),
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

    public SuccessPage confirm() {
        iframeAssist.shouldBe(exist, Duration.ofSeconds(15));
        switchTo().frame(iframeAssist);
        confirmSubmitButton.shouldBe(exist, Duration.ofSeconds(15)).click();
        thanksFor.shouldBe(visible, Duration.ofSeconds(15));
        return new SuccessPage(apiClient);
    }
}