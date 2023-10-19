package pages;

import com.codeborne.selenide.SelenideElement;
import fixtures.AssertFixturesPage;
import helpers.ApiClient;
import helpers.CurrencyType;
import helpers.HelperPage;
import lombok.SneakyThrows;

import java.time.Duration;

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
    private final SelenideElement orderSection = $(".AEYhRIG-");
    private final SelenideElement header = $x("//h1");
    //private final SelenideElement thanksFor1 = $x("//h1");
    private final SelenideElement thanksFor = $x("//h1[text() ='Спасибо за заказ']");
    private final SelenideElement checkOnPromoCodeInput = $x("//div[@class='_2ke-1fXm']//span");
    private final SelenideElement promoCodeInput = $x("//span[text()='Введите промокод']//preceding-sibling::input");
    private final SelenideElement promoCodeAppliedPopup = $x("//span[text()='Промокод применен']");
    private final SelenideElement promoCodeAppliedArea = $("._3aKs9p4n");
    private final SelenideElement promoCodeAppliedButton = $x("//span[text()='Применить']");
    private final ApiClient apiClient;
    private AssertFixturesPage assertFixturesPage;

    public PaymentPage(ApiClient apiClient, AssertFixturesPage assertFixturesPage) {
        this.apiClient = apiClient;
        this.assertFixturesPage = assertFixturesPage;
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
        assertTrue(HelperPage.isOrderSectionContainsAllFromBouquets(orderSection, apiClient.getBouquetNameList()),
                "bouquets names not equals");
        return this;
    }

    public PaymentPage assertDeliveryPrice(CurrencyType currencyType) {
        assertFixturesPage.performAssertDeliveryPrice(orderSection, currencyType);
        return this;
    }

    public PaymentPage assertBouquetPrice(CurrencyType currencyType) {
        assertFixturesPage.performAssertBouquetPriceList(orderSection, currencyType);
        return this;
    }

    public PaymentPage assertExtrasPrice(CurrencyType currencyType) {
        assertFixturesPage.performAssertExtrasPrice(orderSection, currencyType);
        return this;
    }

    public String getTotalPrice(CurrencyType currencyType) {
        apiClient.getOrderData();
        return apiClient.getOrderTotalPrice(currencyType);
    }

    public PaymentPage assertTotalPrice(CurrencyType currencyType) {
        apiClient.getOrderData();
        String totalDataPrice = HelperPage.priceCurrencyFormat(currencyType, apiClient.getOrderTotalPrice(currencyType));
        orderSection.shouldHave(text(totalDataPrice));
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
        iframeAssist.shouldBe(exist, Duration.ofSeconds(20));
        switchTo().frame(iframeAssist);
        confirmSubmitButton.shouldBe(visible, Duration.ofSeconds(15)).click();
        switchTo().defaultContent();
        thanksFor.shouldBe(visible, Duration.ofSeconds(20));
        return new SuccessPage(apiClient);
    }

    public CatalogPage backOnPrevious() {
        back();
        return new CatalogPage(apiClient);
    }

    public PaymentPage setPromoCode(String promo, CurrencyType currencyType) {
        checkOnPromoCodeInput.shouldBe(exist).click();
        promoCodeInput.shouldBe(exist).sendKeys(promo);
        promoCodeAppliedButton.shouldBe(exist).click();
        promoCodeAppliedPopup.shouldBe(visible);
        promoCodeAppliedArea.shouldBe(visible);

        double sum = apiClient.getBouquetPriceList(currencyType).stream()
                .map(Double::valueOf)
                .mapToInt(price -> (int) (price * 0.1))
                .sum();

        orderSection.shouldHave(text("Скидка по промокоду"));

        if (HelperPage.containsDecimalNumber(String.valueOf(sum))) {
            orderSection.shouldHave(text(String.valueOf(sum)));
        } else {
            orderSection.shouldHave(text(HelperPage.priceRegex(String.valueOf(sum).replaceAll("\\.(\\d+)", ""))));
        }
        return this;
    }
}