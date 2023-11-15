package tests;

import fixtures.AssertFixturesPage;
import helpers.ApiClient;

import helpers.BouquetType;
import helpers.CurrencyType;
import helpers.HelperPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.*;

import java.time.LocalTime;
import java.util.stream.Stream;

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

    @BeforeEach
    void setData() {
        yourName = testData.getYourFullName();
        yourEmail = testData.getYourEmail();
        yourPhone = testData.getYourPhone();
        recipientName = testData.getRecipientName();
        phone = testData.getPhone();
        address = testData.getAddress();
        password = testData.getPassword();

        ApiClient apiClient = new ApiClient();

        AssertFixturesPage assertFixturesPage = new AssertFixturesPage(apiClient);
        catalogPage = new CatalogPage(apiClient);
        bouquetPage = new BouquetPage(apiClient, assertFixturesPage);
        checkoutPage = new CheckoutPage(apiClient, assertFixturesPage);
        paymentPage = new PaymentPage(apiClient, assertFixturesPage);
        successPage = new SuccessPage(apiClient);
        accountOrderPage = new AccountOrderPage(apiClient);
        orderERPPage = new OrderERPPage(apiClient);
    }

    //    @Test
//    @Tag("create_order")
//    void createNewFloristRuOrderTest() {
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .setFirstVariation()
//                .assertBouquetName()
//                .assertBouquetPrice(CurrencyType.RUB);
//        bouquetPage.assertDeliveryPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .addToCard(baseUrl);
//
//        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, recipientName, phone, address)
//                .getRandomDeliveryDate()
//                .getRandomDeliveryTime()
//                .assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.RUB)
//                .assertBouquetPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .goToPaymentPage();
//
//        paymentPage.assertPaymentStatus(baseUrl)
//                .assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.RUB)
//                .assertBouquetPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .fillCard(cardNumber, expireNumber, cvcNumber)
//                .pay()
//                .confirm();
//
//        successPage.assertSuccessOrderStatus(baseUrl)
//                .assertSuccessCreatedOrder(CurrencyType.RUB);
//    }
//
//    @Test
//    @Tag("create_order")
//    void createNewOrderWith2BouquetsTest() {
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .setFirstVariation()
//                .assertBouquetName()
//                .assertBouquetPrice(CurrencyType.RUB);
//        bouquetPage.assertDeliveryPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .addToCard(baseUrl);
//
//        catalogPage.openCatalogPage(baseUrl)
//                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .setFirstVariation()
//                .assertBouquetName()
//                .assertBouquetPrice(CurrencyType.RUB);
//        bouquetPage.assertDeliveryPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .addToCard(baseUrl);
//
//        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, recipientName, phone, address)
//                .getRandomDeliveryDate()
//                .getRandomDeliveryTime()
//                .assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.RUB)
//                .assertBouquetPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .goToPaymentPage();
//
//        paymentPage.assertPaymentStatus(baseUrl)
//                .assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.RUB)
//                .assertBouquetPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .fillCard(cardNumber, expireNumber, cvcNumber)
//                .pay()
//                .confirm();
//
//        successPage.assertSuccessOrderStatus(baseUrl)
//                .assertSuccessCreatedOrder(CurrencyType.RUB);
//    }
//
//    @Test
//    @Tag("create_order")
//    void createNewIFloristOrderTest() {
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                .setRandomBouquet(BouquetType.IFLORIST, CurrencyType.RUB);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .setFirstVariation()
//                .assertBouquetName()
//                .assertBouquetPrice(CurrencyType.RUB);
//        bouquetPage.assertDeliveryPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .addToCard(baseUrl);
//
//        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, recipientName, phone, address)
//                .getRandomDeliveryDate()
//                .getRandomDeliveryTime()
//                .assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.RUB)
//                .assertBouquetPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .goToPaymentPage();
//
//        paymentPage.assertPaymentStatus(baseUrl)
//                .assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.RUB)
//                .assertBouquetPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .fillCard(cardNumber, expireNumber, cvcNumber)
//                .pay()
//                .confirm();
//
//        successPage.assertSuccessOrderStatus(baseUrl)
//                .assertSuccessCreatedOrder(CurrencyType.RUB);
//    }
//
//    @Test
//    @Tag("create_order")
//    void usingBackAfterCreatedOrderTest() {
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                .setRandomBouquet(BouquetType.ALL_BOUQUETS, CurrencyType.RUB);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .setFirstVariation()
//                .assertBouquetName()
//                .assertBouquetPrice(CurrencyType.RUB);
//        bouquetPage.assertDeliveryPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .addToCard(baseUrl);
//
//        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, recipientName, phone, address)
//                .getRandomDeliveryDate()
//                .getRandomDeliveryTime()
//                .assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.RUB)
//                .assertBouquetPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .goToPaymentPage();
//
//        paymentPage.assertPaymentStatus(baseUrl)
//                .assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.RUB)
//                .assertBouquetPrice(CurrencyType.RUB)
//                .assertExtrasPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .backOnPrevious();
//
//        checkoutPage.assertOrderAndBackToPay();
//
//        paymentPage.fillCard(cardNumber, expireNumber, cvcNumber)
//                .pay()
//                .confirm();
//
//        successPage.assertSuccessOrderStatus(baseUrl)
//                .assertSuccessCreatedOrder(CurrencyType.RUB);
//    }
//
//    @Test
//    @Tag("create_order")
//    void createNewOrderAnAuthUserTest() {
//        catalogPage.apiRegisterUser(yourName, yourEmail, yourPhone, password)
//                .openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .openAuthModal()
//                .fillAuthForm(yourEmail, password);
//
//        accountOrderPage.assertAuth(baseUrl, yourName);
//
//        catalogPage.openCatalogPage(baseUrl)
//                .setDeliveryCity()
//                .setRandomBouquet(BouquetType.ALL_BOUQUETS, CurrencyType.RUB);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .setFirstVariation()
//                .assertBouquetName()
//                .assertBouquetPrice(CurrencyType.RUB);
//        bouquetPage.assertDeliveryPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .addToCard(baseUrl);
//
//        checkoutPage.simpleFillForm(recipientName, phone, address)
//                .getRandomDeliveryDate()
//                .getRandomDeliveryTime()
//                .assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.RUB)
//                .assertBouquetPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .goToPaymentPage();
//
//        paymentPage.assertPaymentStatus(baseUrl)
//                .assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.RUB)
//                .assertBouquetPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .fillCard(cardNumber, expireNumber, cvcNumber)
//                .pay()
//                .confirm();
//
//        successPage.assertSuccessOrderStatus(baseUrl)
//                .assertSuccessCreatedOrder(CurrencyType.RUB);
//
//        catalogPage.openAccountOrderPage();
//        accountOrderPage.assertCreatedOrderFromAuthUser(baseUrl, yourName);
//    }
//
//    @Test
//    @Tag("create_order")
//    void createNewOrderWithExtrasTest() {
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .setFirstVariation()
//                .assertBouquetName()
//                .assertBouquetPrice(CurrencyType.RUB);
//        bouquetPage.setRandomExtras(CurrencyType.RUB)
//                .assertExtrasPrice(CurrencyType.RUB)
//                .assertDeliveryPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .addToCard(baseUrl);
//
//        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, recipientName, phone, address)
//                .getRandomDeliveryDate()
//                .getRandomDeliveryTime()
//                .assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.RUB)
//                .assertBouquetPrice(CurrencyType.RUB)
//                .assertExtrasPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .goToPaymentPage();
//
//        paymentPage.assertPaymentStatus(baseUrl)
//                .assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.RUB)
//                .assertBouquetPrice(CurrencyType.RUB)
//                .assertExtrasPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .fillCard(cardNumber, expireNumber, cvcNumber)
//                .pay()
//                .confirm();
//
//        successPage.assertSuccessOrderStatus(baseUrl)
//                .assertSuccessCreatedOrder(CurrencyType.RUB);
//    }
//
//    @Test
//    @Tag("create_order")
//    void removeFromCardTest() {
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .setFirstVariation()
//                .assertBouquetName()
//                .assertBouquetPrice(CurrencyType.RUB);
//        bouquetPage.assertDeliveryPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .addToCard(baseUrl);
//
//        checkoutPage.removeFromCard();
//    }
//
//    @Test
//    @Tag("create_order")
//    void usingPromoСodeTest() {
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .setFirstVariation()
//                .assertBouquetName()
//                .assertBouquetPrice(CurrencyType.RUB);
//        bouquetPage.assertDeliveryPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .addToCard(baseUrl);
//
//        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, recipientName, phone, address)
//                .getRandomDeliveryDate()
//                .getRandomDeliveryTime()
//                .assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.RUB)
//                .assertBouquetPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .goToPaymentPage();
//
//        paymentPage.assertPaymentStatus(baseUrl)
//                .assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.RUB)
//                .assertBouquetPrice(CurrencyType.RUB)
//                .assertExtrasPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .setPromoCode(promo, CurrencyType.RUB)
//                .fillCard(cardNumber, expireNumber, cvcNumber)
//                .pay()
//                .confirm();
//
//        successPage.assertSuccessOrderStatus(baseUrl)
//                .assertSuccessCreatedOrder(CurrencyType.RUB);
//    }
//
    @ParameterizedTest(name = "Тест на проверку оформления заказа и оплаты в валюте {0} доступной на сайте")
    @MethodSource("currencyEnumProvider")
    @Tag("create_order")
    void createOrderDifferentCurrencyTest(CurrencyType currency) throws InterruptedException {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setCurrency(currency)
                .setRandomBouquet(BouquetType.FLORIST_RU, currency);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertBouquetPrice(currency);

        bouquetPage.assertDeliveryPrice(currency)
                .assertTotalPrice(currency)
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, recipientName, phone, address)
                .setRandomDeliveryDate()
                .setRandomDeliveryTime();

        checkoutPage.assertBouquetName()
                .assertDeliveryPrice(currency)
                .assertBouquetPrice(currency)
                .assertTotalPrice(currency)
                .goToPaymentPage();

        paymentPage.assertPaymentStatus(baseUrl)
                .assertBouquetName()
                .assertDeliveryPrice(currency)
                .assertBouquetPrice(currency)
                .assertTotalPrice(currency)
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessOrderStatus(baseUrl)
                .assertSuccessCreatedOrder(currency);
    }

    public static Stream<Arguments> currencyEnumProvider() {
        return Stream.of(CurrencyType.values())
                .map(Arguments::of);
    }

    @Test
    void createNewFloristRuOrderErpTest() throws InterruptedException {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB);

        String bouquetId = String.valueOf(bouquetPage.getBouquetId());
        String bouquetName = bouquetPage.getBouquetName();
        String deliveryPrice = bouquetPage.getDeliveryPrice(CurrencyType.RUB);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName();

        String bouquetPrice = bouquetPage.assertBouquetPrice(CurrencyType.RUB);

        bouquetPage.assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, recipientName, phone, address)
                .setRandomDeliveryDate();

        String deliveryTimeFrom = checkoutPage.setRandomDeliveryTime();
        String deliveryDate = checkoutPage.getDeliveryDate();

        checkoutPage.assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .goToPaymentPage();

        paymentPage.assertPaymentStatus(baseUrl)
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB);

        String orderId = HelperPage.getOrderNumber();
        String totalPrice = paymentPage.getTotalPrice(CurrencyType.RUB);

        paymentPage.assertTotalPrice(CurrencyType.RUB)
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessOrderStatus(baseUrl)
                .assertSuccessCreatedOrder(CurrencyType.RUB);

        orderERPPage.openOrder(baseUrl, orderId)
                .assertBouquetInfo(orderId, bouquetId, bouquetName, bouquetPrice)
                .assertPrices(deliveryPrice, totalPrice, deliveryDate)
                .assertPaymentCompletedChecked()
                .assertRecipientInfo(recipientName, address, phone, deliveryTimeFrom);
    }
}