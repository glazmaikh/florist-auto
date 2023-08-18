package tests;

import fixtures.AssertFixturesPage;
import helpers.ApiClient;

import helpers.BouquetType;
import helpers.CurrencyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.*;

import java.util.stream.Stream;

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

    @Test
    @Tag("create_order")
    void createNewFloristRuOrderTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertBouquetPrice(CurrencyType.RUB)
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
                .getRandomDeliveryDate()
                .getRandomDeliveryTime()
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .goToPaymentPage();

        paymentPage.assertPaymentStatus(baseUrl)
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessOrderStatus(baseUrl)
                .assertSuccessCreatedOrder(CurrencyType.RUB);
    }

    @Test
    @Tag("create_order")
    void createNewOrderWith2BouquetsTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertBouquetPrice(CurrencyType.RUB)
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        catalogPage.openCatalogPage(baseUrl)
                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertBouquetPrice(CurrencyType.RUB)
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
                .getRandomDeliveryDate()
                .getRandomDeliveryTime()
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .goToPaymentPage();

        paymentPage.assertPaymentStatus(baseUrl)
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessOrderStatus(baseUrl)
                .assertSuccessCreatedOrder(CurrencyType.RUB);
    }

    @Test
    @Tag("create_order")
    void createNewIFloristOrderTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setRandomBouquet(BouquetType.IFLORIST, CurrencyType.RUB);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertBouquetPrice(CurrencyType.RUB)
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
                .getRandomDeliveryDate()
                .getRandomDeliveryTime()
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .goToPaymentPage();

        paymentPage.assertPaymentStatus(baseUrl)
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessOrderStatus(baseUrl)
                .assertSuccessCreatedOrder(CurrencyType.RUB);
    }

    @Test
    @Tag("create_order")
    void usingBackAfterCreatedOrderTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setRandomBouquet(BouquetType.ALL_BOUQUETS, CurrencyType.RUB);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertBouquetPrice(CurrencyType.RUB)
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
                .getRandomDeliveryDate()
                .getRandomDeliveryTime()
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .goToPaymentPage();

        paymentPage.assertPaymentStatus(baseUrl)
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB)
                .assertExtrasPrice(CurrencyType.RUB)
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
    @Tag("create_order")
    void createNewOrderAnAuthUserTest() {
        catalogPage.apiRegisterUser(yourName, yourEmail, yourPhone, password)
                .openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openAuthModal()
                .fillAuthForm(yourEmail, password);

        accountOrderPage.assertAuth(baseUrl, yourName);

        catalogPage.openCatalogPage(baseUrl)
                .setDeliveryCity()
                .setRandomBouquet(BouquetType.ALL_BOUQUETS, CurrencyType.RUB);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertBouquetPrice(CurrencyType.RUB)
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(firstName, phone, address)
                .getRandomDeliveryDate()
                .getRandomDeliveryTime()
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .goToPaymentPage();

        paymentPage.assertPaymentStatus(baseUrl)
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessOrderStatus(baseUrl)
                .assertSuccessCreatedOrder(CurrencyType.RUB);

        catalogPage.openAccountOrderPage();
        accountOrderPage.assertCreatedOrderFromAuthUser(baseUrl, yourName);
    }

    @Test
    @Tag("create_order")
    void createNewOrderWithExtrasTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
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

    @Test
    @Tag("create_order")
    void removeFromCardTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertBouquetPrice(CurrencyType.RUB)
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        checkoutPage.removeFromCard();
    }

    @Test
    @Tag("create_order")
    void usingPromoСodeTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertBouquetPrice(CurrencyType.RUB)
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
                .getRandomDeliveryDate()
                .getRandomDeliveryTime()
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .goToPaymentPage();

        paymentPage.assertPaymentStatus(baseUrl)
                .assertBouquetName()
                .assertDeliveryPrice(CurrencyType.RUB)
                .assertBouquetPrice(CurrencyType.RUB)
                .assertExtrasPrice(CurrencyType.RUB)
                .assertTotalPrice(CurrencyType.RUB)
                .setPromoCode(promo, CurrencyType.RUB)
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessOrderStatus(baseUrl)
                .assertSuccessCreatedOrder(CurrencyType.RUB);
    }

    @ParameterizedTest(name = "Тест на проверку оформления заказа и оплаты в валюте {0} доступной на сайте")
    @MethodSource("currencyEnumProvider")
    @Tag("create_order")
    void createOrderDifferentCurrencyTest(CurrencyType currency) {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setCurrency(currency)
                .setRandomBouquet(BouquetType.FLORIST_RU, currency);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .assertBouquetName()
                .assertBouquetPrice(currency)
                .assertDeliveryPrice(currency)
                .assertTotalPrice(currency)
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
                .getRandomDeliveryDate()
                .getRandomDeliveryTime()
                .assertBouquetName()
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
}