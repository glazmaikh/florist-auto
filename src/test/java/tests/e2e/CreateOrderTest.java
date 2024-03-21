package tests.e2e;

import fixtures.AssertFixturesPage;
import helpers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.*;
import tests.TestBase;
import tests.TestData;
import tests.TimeZoneExtension;

import java.util.stream.Stream;

@Tag("e2e")
@ExtendWith(TimeZoneExtension.class)
public class CreateOrderTest extends TestBase {
    private final TestData testData = new TestData();
    private CatalogPage catalogPage;
    private BouquetPage bouquetPage;
    private CheckoutPage checkoutPage;
    private PaymentPage paymentPage;
    private SuccessPage successPage;
    private OrderERPPage orderERPPage;
    private AccountOrderPage accountOrderPage;
    private String yourName, yourEmail, yourPhone, recipientName, phone, address;
    private ApiClient apiClient;

    @BeforeEach
    void setData() {
        yourName = testData.getYourFullName();
        yourEmail = testData.getYourEmail();
        yourPhone = testData.getYourPhone();
        recipientName = testData.getRecipientName();
        phone = testData.getPhone();
        address = testData.getAddress();
        password = testData.getPassword();

        apiClient = new ApiClient();
        AssertFixturesPage assertFixturesPage = new AssertFixturesPage(apiClient);

        catalogPage = new CatalogPage(apiClient, assertFixturesPage);
        bouquetPage = new BouquetPage(apiClient, assertFixturesPage);
        checkoutPage = new CheckoutPage(apiClient, assertFixturesPage);
        paymentPage = new PaymentPage(apiClient, assertFixturesPage);
        successPage = new SuccessPage(apiClient);
        accountOrderPage = new AccountOrderPage(apiClient);
        orderERPPage = new OrderERPPage(apiClient);
    }

    @Test
    void createNewOrderWithExtrasTest() throws Exception {
        apiClient.initBouquet(BouquetType.FLORIST_RU);

        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setRandomBouquet(CurrencyType.RUB, DeliveryDateType.LOW);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName();
        bouquetPage.setRandomExtras(CurrencyType.RUB)
                .assertExtrasPrice(CurrencyType.RUB)
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalMinPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, recipientName, phone, address)
                .setRandomDeliveryDate(DeliveryDateType.LOW);

        checkoutPage.assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .assertExtrasPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .assertTotalPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .goToPaymentPage();

        paymentPage.assertPaymentStatus(baseUrl)
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .assertExtrasPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .assertTotalPrice(CurrencyType.RUB)
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessOrderStatus(baseUrl)
                .assertSuccessCreatedOrder(CurrencyType.RUB);
    }

    @Test
    void createNewOrderWith2BouquetsTest() throws Exception {
        apiClient.initBouquet(BouquetType.FLORIST_RU);

        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                //.setCurrency(CurrencyType.RUB)
                .setRandomBouquetTest(CurrencyType.RUB, DeliveryDateType.LOW);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalMinPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        apiClient.initBouquet(BouquetType.FLORIST_RU);

        catalogPage.openCatalogPage(baseUrl)
                .setRandomBouquetTest(CurrencyType.RUB, DeliveryDateType.LOW);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalMinPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, recipientName, phone, address)
                .setRandomDeliveryDate(DeliveryDateType.LOW);

        String deliveryTimeFrom = checkoutPage.setRandomDeliveryTime();

        checkoutPage.assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .assertTotalPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .goToPaymentPage();

        paymentPage.assertPaymentStatus(baseUrl)
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .assertTotalPrice(CurrencyType.RUB);

        String orderId = HelperPage.getOrderNumber();
        String totalPrice = paymentPage.getTotalPrice(CurrencyType.RUB);

        paymentPage.fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessOrderStatus(baseUrl)
                .assertSuccessCreatedOrder(CurrencyType.RUB);

        orderERPPage.openOrder(baseUrl, orderId)
                .assertBouquetInfo(orderId, CurrencyType.RUB)
                //передавать корректный CurrencyType в assertPrices() когда в ERP будет реализована цена в валютах
                .assertPrices(CurrencyType.RUB, totalPrice)
                .assertPaymentCompletedChecked()
                .assertRecipientInfo(deliveryTimeFrom);
    }

    @ParameterizedTest(name = "Тест на проверку оформления заказа и оплаты в валюте {0} доступной на сайте")
    @MethodSource("currencyEnumProvider")
    void createOrderDifferentCurrencyTest(CurrencyType currency) throws Exception {
        apiClient.initBouquet(BouquetType.FLORIST_RU);

        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setCurrency(currency)
                .setRandomBouquet(currency, DeliveryDateType.LOW);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertDeliveryPrice(currency)
                .assertTotalMinPrice(currency)
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, recipientName, phone, address)
                .setRandomDeliveryDate(DeliveryDateType.LOW);

        String deliveryTimeFrom = checkoutPage.setRandomDeliveryTime();

        checkoutPage.assertBouquetName()
                .assertDeliveryPrice(currency)
                .assertBouquetPrice(currency, DeliveryDateType.LOW)
                .assertTotalPrice(currency, DeliveryDateType.LOW)
                .goToPaymentPage();

        paymentPage.assertPaymentStatus(baseUrl)
                .assertBouquetName()
                .assertDeliveryPrice(currency)
                .assertBouquetPrice(currency, DeliveryDateType.LOW)
                .assertTotalPrice(currency);

        String orderId = HelperPage.getOrderNumber();
        String totalPrice = paymentPage.getTotalPrice(currency);

        paymentPage.fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessOrderStatus(baseUrl)
                .assertSuccessCreatedOrder(currency);

        orderERPPage.openOrder(baseUrl, orderId)
                .assertBouquetInfo(orderId, currency)
                //передавать корректный CurrencyType в assertPrices() когда в ERP будет реализована цена в валютах
                .assertPrices(CurrencyType.RUB, totalPrice)
                .assertPaymentCompletedChecked()
                .assertRecipientInfo(deliveryTimeFrom);
    }

    public static Stream<Arguments> currencyEnumProvider() {
        return Stream.of(CurrencyType.values())
                .map(Arguments::of);
    }

    @Test
    void createNewOrderAnAuthUserTest() throws Exception {
        apiClient.initBouquet(BouquetType.FLORIST_RU);

        catalogPage.apiRegisterUser(yourName, yourEmail, yourPhone, password)
                .openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openAuthModal()
                .fillAuthForm(yourEmail, password);

        accountOrderPage.assertAuth(baseUrl, yourName);

        catalogPage.openCatalogPage(baseUrl)
                .setDeliveryCity()
                .setRandomBouquet(CurrencyType.RUB, DeliveryDateType.LOW);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalMinPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(recipientName, phone, address)
                .setRandomDeliveryDate(DeliveryDateType.LOW);

        checkoutPage.assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .assertTotalPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .goToPaymentPage();

        paymentPage.assertPaymentStatus(baseUrl)
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .assertTotalPrice(CurrencyType.RUB);

        paymentPage.fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessOrderStatus(baseUrl)
                .assertSuccessCreatedOrder(CurrencyType.RUB);

        catalogPage.openAccountOrderPage();

        accountOrderPage.assertCreatedOrderExist(baseUrl)
                .assertBouquetName()
                .assertBouquetPrice(CurrencyType.RUB)
                .assertDeliveryDate()
                .assertRecipientData(recipientName, phone, address)
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB);
    }

    @Test
    void usingPromoCodeTest() throws Exception {
        apiClient.initBouquet(BouquetType.FLORIST_RU, false);

        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .assertDeliveryCity()
                //.setCurrency(CurrencyType.KZT)
                .setRandomBouquet(CurrencyType.RUB, DeliveryDateType.LOW);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalMinPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, recipientName, phone, address)
                .setRandomDeliveryDate(DeliveryDateType.LOW);

        checkoutPage.assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .assertTotalPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .goToPaymentPage();

        paymentPage.assertPaymentStatus(baseUrl)
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .setPromoCode(promo, CurrencyType.RUB, DeliveryDateType.LOW)
                .assertTotalPrice(CurrencyType.RUB)
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessOrderStatus(baseUrl)
                .assertSuccessCreatedOrder(CurrencyType.RUB);
    }

    @Test
    void usingBackAfterCreatedOrderTest() throws Exception {
        apiClient.initBouquet(BouquetType.FLORIST_RU);

        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setRandomBouquet(CurrencyType.RUB, DeliveryDateType.LOW);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalMinPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, recipientName, phone, address)
                .setRandomDeliveryDate(DeliveryDateType.LOW);

        checkoutPage.assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .assertExtrasPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .assertTotalPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .goToPaymentPage();

        paymentPage.assertPaymentStatus(baseUrl)
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .assertExtrasPrice(CurrencyType.RUB, DeliveryDateType.LOW)
                .assertTotalPrice(CurrencyType.RUB)
                .backOnPrevious();

        checkoutPage.assertOrderAndBackToPay();

        paymentPage.fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessOrderStatus(baseUrl)
                .assertSuccessCreatedOrder(CurrencyType.RUB);
    }

    @Test
    void removeFromCardTest() throws InterruptedException {
        apiClient.initBouquet(BouquetType.FLORIST_RU);

        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setRandomBouquet(CurrencyType.RUB, DeliveryDateType.LOW);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalMinPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        checkoutPage.removeFromCard();
    }
}