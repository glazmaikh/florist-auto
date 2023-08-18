package tests;

import fixtures.AssertFixturesPage;
import helpers.ApiClient;

import helpers.BouquetType;
import helpers.CurrencyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.*;

public class CreateOrderTest extends TestBase {
    private final TestData testData = new TestData();
    private CatalogPage catalogPage;
    private BouquetPage bouquetPage;
    private CheckoutPage checkoutPage;
    private PaymentPage paymentPage;
    private SuccessPage successPage;
    private AccountOrderPage accountOrderPage;
    private AssertFixturesPage assertFixturesPage;
    private String yourName, yourEmail, yourPhone, firstName, phone, address, password;

    @BeforeEach
    void setData() {
        yourName = testData.getYourFullName();
        yourEmail = testData.getYourEmail();
        yourPhone = testData.getYourPhone();
        firstName = testData.getFirstName();
        phone = testData.getPhone();
        address = testData.getAddress();
        password = testData.getPassword();

        ApiClient apiClient = new ApiClient();

        assertFixturesPage = new AssertFixturesPage(apiClient);
        catalogPage = new CatalogPage(apiClient);
        bouquetPage = new BouquetPage(apiClient, assertFixturesPage);
        checkoutPage = new CheckoutPage(apiClient, assertFixturesPage);
        paymentPage = new PaymentPage(apiClient, assertFixturesPage);
        successPage = new SuccessPage(apiClient);
        accountOrderPage = new AccountOrderPage(apiClient);
    }

//    @Test
//    @Tag("create_order")
//    void createNewFloristRuOrderTest() {
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                .setRandomBouquet(BouquetType.FLORIST_RU);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .assertBouquetName()
//                .assertVariationsPrices()
//                .setFirstVariation()
//                .assertDeliveryPrice()
//                .assertTotalPrice()
//                .addToCard(baseUrl);
//
//        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
//                .getRandomDeliveryDate()
//                .getRandomDeliveryTime()
//                .assertBouquetName()
//                .assertDeliveryPrice()
//                .assertBouquetPrice()
//                .assertTotalPrice()
//                .goToPaymentPage();
//
//        paymentPage.assertPaymentStatus(baseUrl)
//                .assertBouquetName()
//                .assertDeliveryPrice()
//                .assertBouquetPrice()
//                .assertTotalPrice()
//                .fillCard(cardNumber, expireNumber, cvcNumber)
//                .pay()
//                .confirm();
//
//        successPage.assertSuccessOrderStatus(baseUrl)
//                .assertSuccessCreatedOrder();
//    }
//
//    @Test
//    @Tag("create_order")
//    void createNewOrderWith2BouquetsTest() {
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                .setRandomBouquet(BouquetType.FLORIST_RU);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .assertBouquetName()
//                .assertVariationsPrices()
//                .setFirstVariation()
//                .assertDeliveryPrice()
//                .assertTotalPrice()
//                .addToCard(baseUrl);
//
//        catalogPage.openCatalogPage(baseUrl)
//                .setRandomBouquet(BouquetType.FLORIST_RU);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .assertBouquetName()
//                .assertVariationsPrices()
//                .setFirstVariation()
//                .assertDeliveryPrice()
//                .assertTotalPrice()
//                .addToCard(baseUrl);
//
//        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
//                .getRandomDeliveryDate()
//                .getRandomDeliveryTime()
//                .assertBouquetName()
//                .assertDeliveryPrice()
//                .assertBouquetPrice()
//                .assertTotalPrice()
//                .goToPaymentPage();
//
//        paymentPage.assertPaymentStatus(baseUrl)
//                .assertBouquetName()
//                .assertDeliveryPrice()
//                .assertBouquetPrice()
//                .assertTotalPrice()
//                .fillCard(cardNumber, expireNumber, cvcNumber)
//                .pay()
//                .confirm();
//
//        successPage.assertSuccessOrderStatus(baseUrl)
//                .assertSuccessCreatedOrder();
//    }
//
//    @Test
//    @Tag("create_order")
//    void createNewIFloristOrderTest() {
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                .setRandomBouquet(BouquetType.IFLORIST);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .assertBouquetName()
//                .assertVariationsPrices()
//                .setFirstVariation()
//                .assertDeliveryPrice()
//                .assertTotalPrice()
//                .addToCard(baseUrl);
//
//        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
//                .getRandomDeliveryDate()
//                .getRandomDeliveryTime()
//                .assertBouquetName()
//                .assertDeliveryPrice()
//                .assertBouquetPrice()
//                .assertTotalPrice()
//                .goToPaymentPage();
//
//        paymentPage.assertPaymentStatus(baseUrl)
//                .assertBouquetName()
//                .assertDeliveryPrice()
//                .assertBouquetPrice()
//                .assertTotalPrice()
//                .fillCard(cardNumber, expireNumber, cvcNumber)
//                .pay()
//                .confirm();
//
//        successPage.assertSuccessOrderStatus(baseUrl)
//                .assertSuccessCreatedOrder();
//    }
//
//    @Test
//    @Tag("create_order")
//    void usingBackAfterCreatedOrderTest() {
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                .setRandomBouquet(BouquetType.ALL_BOUQUETS);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .assertBouquetName()
//                .assertVariationsPrices()
//                .setFirstVariation()
//                .assertDeliveryPrice()
//                .assertTotalPrice()
//                .addToCard(baseUrl);
//
//        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
//                .getRandomDeliveryDate()
//                .getRandomDeliveryTime()
//                .assertBouquetName()
//                .assertDeliveryPrice()
//                .assertBouquetPrice()
//                .assertTotalPrice()
//                .goToPaymentPage();
//
//        paymentPage.assertPaymentStatus(baseUrl)
//                .assertBouquetName()
//                .assertDeliveryPrice()
//                .assertBouquetPrice()
//                .assertTotalPrice()
//                .backOnPrevious();
//
//        checkoutPage.assertOrderAndBackToPay();
//
//        paymentPage.fillCard(cardNumber, expireNumber, cvcNumber)
//                .pay()
//                .confirm();
//
//        successPage.assertSuccessOrderStatus(baseUrl)
//                .assertSuccessCreatedOrder();
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
//                .setRandomBouquet(BouquetType.FLORIST_RU);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .assertBouquetName()
//                .assertVariationsPrices()
//                .setFirstVariation()
//                .assertDeliveryPrice()
//                .addToCard(baseUrl);
//
//        checkoutPage.simpleFillForm(firstName, phone, address)
//                .getRandomDeliveryDate()
//                .getRandomDeliveryTime()
//                .assertBouquetName()
//                .assertDeliveryPrice()
//                .assertBouquetPrice()
//                .assertTotalPrice()
//                .goToPaymentPage();
//
//        paymentPage.assertPaymentStatus(baseUrl)
//                .assertBouquetName()
//                .assertDeliveryPrice()
//                .assertBouquetPrice()
//                .assertTotalPrice()
//                .fillCard(cardNumber, expireNumber, cvcNumber)
//                .pay()
//                .confirm();
//
//        successPage.assertSuccessOrderStatus(baseUrl)
//                .assertSuccessCreatedOrder();
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
//                .setRandomBouquet(BouquetType.FLORIST_RU);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .assertBouquetName()
//                .assertVariationsPrices()
//                .setFirstVariation()
//                .setRandomExtras()
//                .assertExtras()
//                .assertDeliveryPrice()
//                .assertTotalPrice()
//                .addToCard(baseUrl);
//
//        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
//                .getRandomDeliveryDate()
//                .getRandomDeliveryTime()
//                .assertBouquetName()
//                .assertDeliveryPrice()
//                .assertBouquetPrice()
//                .assertExtrasPrice()
//                .assertTotalPrice()
//                .goToPaymentPage();
//
//        paymentPage.assertPaymentStatus(baseUrl)
//                .assertBouquetName()
//                .assertDeliveryPrice()
//                .assertBouquetPrice()
//                .assertExtrasPrice()
//                .assertTotalPrice()
//                .fillCard(cardNumber, expireNumber, cvcNumber)
//                .pay()
//                .confirm();
//
//        successPage.assertSuccessOrderStatus(baseUrl)
//                .assertSuccessCreatedOrder();
//    }
//
//    @Test
//    @Tag("create_order")
//    void removeFromCardTest() {
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                .setRandomBouquet(BouquetType.FLORIST_RU);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .assertBouquetName()
//                .assertVariationsPrices()
//                .setFirstVariation()
//                .assertDeliveryPrice()
//                .assertTotalPrice()
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
//                .setCurrency(CurrencyType.USD)
//                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.USD);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .assertBouquetName()
//                .assertVariationsPrices(CurrencyType.USD)
//                .setFirstVariation()
//                .assertDeliveryPrice(CurrencyType.USD)
//                .assertTotalPrice(CurrencyType.USD)
//                .addToCard(baseUrl);
//
//        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
//                .getRandomDeliveryDate()
//                .getRandomDeliveryTime()
//                .assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.USD)
//                .assertBouquetPrice(CurrencyType.USD)
//                .assertTotalPrice(CurrencyType.USD)
//                .goToPaymentPage();
//
//        paymentPage.assertPaymentStatus(baseUrl)
//                .assertBouquetName()
//                .assertDeliveryPrice(CurrencyType.USD)
//                .assertBouquetPrice(CurrencyType.USD)
//                .assertTotalPrice()
//                .setPromoCode(promo, CurrencyType.USD);
//    }

//    @ParameterizedTest(name = "Тест на проверку оформления заказа и оплаты в валюте {0} доступной на сайте")
//    @MethodSource("currencyEnumProvider")
//    @Tag("create_order")
//    void createOrderDifferentCurrencyTest(CurrencyType currency) {
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                .setCurrency(currency)
//                .setRandomBouquet(BouquetType.FLORIST_RU, currency);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .assertBouquetName()
//                .assertVariationsPrices(currency)
//                .setFirstVariation()
//                //.setRandomExtras(currency)
//                //.assertExtras(currency)
//                .assertDeliveryPrice(currency)
//                .assertTotalPrice(currency)
//                .addToCard(baseUrl);
//    }

//    public static Stream<Arguments> currencyEnumProvider() {
//        return Stream.of(CurrencyType.values())
//                .map(Arguments::of);
//    }

    @Test
    @Tag("create_order")
    void test() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                //.setCurrency(CurrencyType.KZT)
                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertBouquetPrice(CurrencyType.RUB)
                .setRandomExtras(CurrencyType.RUB)
                .assertExtrasPrice(CurrencyType.RUB)
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        catalogPage.openCatalogPage(baseUrl)
                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertBouquetPrice(CurrencyType.RUB)
                .setRandomExtras(CurrencyType.RUB)
                .assertExtrasPrice(CurrencyType.RUB)
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
                .getRandomDeliveryDate()
                .getRandomDeliveryTime()
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB)
                .assertExtrasPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .goToPaymentPage();

        paymentPage.assertPaymentStatus(baseUrl)
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB)
                .assertExtrasPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessOrderStatus(baseUrl)
                .assertSuccessCreatedOrder(CurrencyType.RUB);
    }
}