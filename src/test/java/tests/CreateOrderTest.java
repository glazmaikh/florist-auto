package tests;

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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
//    void usingPromoCodeTest() throws InterruptedException {
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                //.setCurrency(CurrencyType.KZT)
//                .setRandomBouquet(BouquetType.ALL_BOUQUETS, CurrencyType.RUB, false);
//
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
//                .setRandomDeliveryDate()
//                .setRandomDeliveryTime();
//        checkoutPage.assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.RUB)
//                .assertBouquetPrice(CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .goToPaymentPage();
//
//        paymentPage.assertPaymentStatus(baseUrl)
//                .assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.RUB)
//                .assertBouquetPrice(CurrencyType.RUB)
//                .setPromoCode(promo, CurrencyType.RUB)
//                .assertTotalPrice(CurrencyType.RUB)
//                .fillCard(cardNumber, expireNumber, cvcNumber)
//                .pay()
//                .confirm();
//
//        successPage.assertSuccessOrderStatus(baseUrl)
//                .assertSuccessCreatedOrder(CurrencyType.RUB);
//    }
//
//    @ParameterizedTest(name = "Тест на проверку оформления заказа и оплаты в валюте {0} доступной на сайте")
//    @MethodSource("currencyEnumProvider")
//    @Tag("create_order")
//    void createOrderDifferentCurrencyTest(CurrencyType currency) throws InterruptedException {
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                .setCurrency(currency)
//                .setRandomBouquet(BouquetType.FLORIST_RU, currency);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .setFirstVariation()
//                .assertBouquetName()
//                .assertBouquetPrice(currency);
//        bouquetPage.assertDeliveryPrice(currency)
//                .assertTotalPrice(currency)
//                .addToCard(baseUrl);
//
//        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, recipientName, phone, address)
//                .setRandomDeliveryDate()
//                .setRandomDeliveryTime();
//        checkoutPage.assertBouquetName()
//                .assertDeliveryPrice(currency)
//                .assertBouquetPrice(currency)
//                .assertTotalPrice(currency)
//                .goToPaymentPage();
//
//        paymentPage.assertPaymentStatus(baseUrl)
//                .assertBouquetName()
//                .assertDeliveryPrice(currency)
//                .assertBouquetPrice(currency)
//                .assertTotalPrice(currency)
//                .fillCard(cardNumber, expireNumber, cvcNumber)
//                .pay()
//                .confirm();
//
//        successPage.assertSuccessOrderStatus(baseUrl)
//                .assertSuccessCreatedOrder(currency);
//    }
//
//    public static Stream<Arguments> currencyEnumProvider() {
//        return Stream.of(CurrencyType.values())
//                .map(Arguments::of);
//    }

    @Test
    void createNewFloristRuOrderErpTest() throws Exception {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                //.setCurrency(CurrencyType.KZT)
                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB);

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
                .assertPrices(CurrencyType.RUB, totalPrice)
                .assertPaymentCompletedChecked()
                .assertRecipientInfo(deliveryTimeFrom);
    }

    @Test
    @Tag("create_order")
    void createNewOrderWith2BouquetsTest() throws Exception {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                //.setCurrency(CurrencyType.RUB)
                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalMinPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        catalogPage.openCatalogPage(baseUrl)
                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB);

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
                .assertPrices(CurrencyType.RUB, totalPrice)
                .assertPaymentCompletedChecked()
                .assertRecipientInfo(deliveryTimeFrom);
    }
}
