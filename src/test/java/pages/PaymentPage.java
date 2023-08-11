package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import helpers.CurrencyType;
import helpers.HelperPage;
import lombok.SneakyThrows;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.*;

import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentPage {
    private final SelenideElement cardNumberInput = $(byName("cardNumber"));
    private final SelenideElement expireInput = $(byName("cardValidity"));
    private final SelenideElement cvcInput = $(byName("cardCvv"));
    private final SelenideElement submitButton = $x("//button[@type='submit']");
    private final SelenideElement confirmSubmitButton = $(byName("SET"));
    private final SelenideElement iframeAssist = $x("//div[@id='modal-overlay']//iframe");
    private final SelenideElement orderList = $(".AEYhRIG-");
    private final SelenideElement header = $x("//h1");
    private final SelenideElement thanksFor = $x("//h1[text() ='Спасибо за заказ']");
    private final SelenideElement checkOnPromoCodeInput = $x("//div[@class='_2ke-1fXm']//span");
    private final SelenideElement promoCodeInput = $x("//span[text()='Введите промокод']//preceding-sibling::input");
    private final SelenideElement promoCodeAppliedPopup = $x("//span[text()='Промокод применен']");
    private final SelenideElement promoCodeAppliedArea = $("._3aKs9p4n");
    private final SelenideElement promoCodeAppliedButton = $x("//span[text()='Применить']");
    private final ApiClient apiClient;

    public PaymentPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @SneakyThrows
    public PaymentPage assertPaymentStatus(String baseUrl) {
        header.shouldHave(textCaseSensitive("Оплата заказа"), Duration.ofSeconds(20));
        webdriver().shouldHave(url(baseUrl + apiClient.getCitySlug() + "/order/payment/" + HelperPage.getOrderNumber() + "/" + HelperPage.getOrderAccessKey()));
        apiClient.getOrderData();
        assertEquals(String.valueOf(apiClient.getOrderId()), header.getText().replaceAll("[^0-9]", ""),
                "incorrect order number on PaymentPage");

        assertTrue(apiClient.getOrderStatus().contains("Ожидает оплаты"));
        return this;
    }

    public PaymentPage assertBouquetName() {
        assertTrue(HelperPage.isOrderListContainsAllFromBouquets(orderList, apiClient.getBouquetNameList()),
                "bouquets names not equals");
        return this;
    }

    public PaymentPage assertDeliveryPrice() {
//        int deliveryPrice = HelperPage.doubleToIntRound(apiClient.getDeliveryPrice());
//        if (deliveryPrice > 100) {
//            orderList.shouldHave(text(HelperPage.priceRegexRub(String.valueOf(deliveryPrice))));
//        } else {
//            orderList.shouldHave(text("бесплатно"));
//        }
        return this;
    }

    public PaymentPage assertBouquetPrice(CurrencyType currencyType) {
        List<String> bouquetsPrices = apiClient.getBouquetPriceRubList(currencyType).stream()
                .map(String::valueOf)
                .map(HelperPage::priceRegexRub)
                .toList();
        assertTrue(HelperPage.isOrderListContainsAllFromBouquets(orderList, bouquetsPrices),
                "bouquets prices not equals");
        return this;
    }

    public PaymentPage assertExtrasPrice() {
        List<String> extrasPrices = apiClient.getExtrasPriceRubList().stream()
                .map(String::valueOf)
                .map(HelperPage::priceRegexRub)
                .toList();
        assertTrue(HelperPage.isOrderListContainsAllFromBouquets(orderList, extrasPrices),
                "extrases prices not equals");
        return this;
    }

    public PaymentPage assertTotalPrice() {
        apiClient.getOrderData();
        String totalDataPrice = HelperPage.priceRegexRub(String.valueOf(apiClient.getOrderTotalPrice()));
        orderList.shouldHave(text(totalDataPrice));
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
        confirmSubmitButton.shouldBe(visible, Duration.ofSeconds(15)).click();
        thanksFor.shouldBe(visible, Duration.ofSeconds(15));
        return new SuccessPage(apiClient);
    }

    public CatalogPage backOnPrevious() {
        back();
        return new CatalogPage(apiClient);
    }

//    public PaymentPage setPromoCode(String promo, CurrencyType currencyType) {
//        checkOnPromoCodeInput.shouldBe(exist).click();
//        promoCodeInput.shouldBe(exist).sendKeys(promo);
//        promoCodeAppliedButton.shouldBe(exist).click();
//        promoCodeAppliedPopup.shouldBe(visible);
//        promoCodeAppliedArea.shouldBe(visible);
//
//        int sum = apiClient.getBouquetPriceRubList(currencyType).stream()
//                .map(price -> (int) (price * 0.1))
//                .mapToInt(Integer::intValue)
//                .sum();
//
//        orderList.shouldHave(text("Скидка по промокоду"));
//        orderList.shouldHave(text(HelperPage.priceRegexRub(String.valueOf(sum))));
//        return this;
//    }
}