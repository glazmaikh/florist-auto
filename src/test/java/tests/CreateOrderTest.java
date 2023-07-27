package tests;

import helpers.ApiClient;

import helpers.BouquetType;
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

        catalogPage = new CatalogPage(apiClient);
        bouquetPage = new BouquetPage(apiClient);
        checkoutPage = new CheckoutPage(apiClient);
        paymentPage = new PaymentPage(apiClient);
        successPage = new SuccessPage(apiClient);
        accountOrderPage = new AccountOrderPage(apiClient);
    }

    @Test
    @Tag("create_order")
    void createNewFloristRuOrderTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setRandomBouquet(BouquetType.FLORIST_RU);

        bouquetPage.openBouquetPage(baseUrl)
                .assertBouquetName()
                .assertVariationsPrices()
                .assertDeliveryPrice()
                .setFirstVariation()
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
                .getRandomDeliveryDate()
                .getRandomDeliveryTime()
     //           .assertOrderList()
                .pressPayButton();

        paymentPage
                //.assertOrderList()
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessCreatedOrder(baseUrl);
    }

    @Test
    @Tag("create_order")
    void createNewOrderWith2BouquetsTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setRandomBouquet(BouquetType.FLORIST_RU);

        bouquetPage.openBouquetPage(baseUrl)
                .assertBouquetName()
                .assertVariationsPrices()
                .assertDeliveryPrice()
                .setFirstVariation()
                .addToCard(baseUrl);

        catalogPage.openCatalogPage(baseUrl)
                .setRandomBouquet(BouquetType.FLORIST_RU);

        bouquetPage.openBouquetPage(baseUrl)
                .assertBouquetName()
                .assertVariationsPrices()
                .assertDeliveryPrice()
                .setFirstVariation()
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
                .getRandomDeliveryDate()
                .getRandomDeliveryTime()
   //             .assertOrderList()
                .pressPayButton();

        paymentPage
                //.assertOrderList()
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessCreatedOrder(baseUrl);
    }

    @Test
    @Tag("create_order")
    void createNewIFloristOrderTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setRandomBouquet(BouquetType.FLORIST_RU);

        bouquetPage.openBouquetPage(baseUrl)
                .assertBouquetName()
                .assertVariationsPrices()
                .assertDeliveryPrice()
                .setFirstVariation()
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
                .getRandomDeliveryDate()
                .getRandomDeliveryTime()
 //               .assertOrderList()
                .pressPayButton();

        paymentPage
                //.assertOrderList()
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessCreatedOrder(baseUrl);
    }

    @Test
    @Tag("create_order")
    void usingBackAfterCreatedOrderTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setRandomBouquet(BouquetType.ALL_BOUQUETS);

        bouquetPage.openBouquetPage(baseUrl)
                .assertBouquetName()
                .assertVariationsPrices()
                .assertDeliveryPrice()
                .setFirstVariation()
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
                .getRandomDeliveryDate()
                .getRandomDeliveryTime()
 //               .assertOrderList()
                .pressPayButton();

        paymentPage
                //.assertOrderList()
                .backOnPrevious();

        checkoutPage.assertOrderAndBackToPay();

        paymentPage.fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessCreatedOrder(baseUrl);
    }

    @Test
    @Tag("create_order")
    void creatingNewOrderAnAuthUserTest() {
        catalogPage.apiRegisterUser(yourName, yourEmail, yourPhone, password)
                .openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openAuthModal()
                .fillAuthForm(yourEmail, password);

        accountOrderPage.assertAuth(baseUrl, yourName);

        catalogPage.openCatalogPage(baseUrl)
                .setDeliveryCity()
                .setRandomBouquet(BouquetType.ALL_BOUQUETS);

        bouquetPage.openBouquetPage(baseUrl)
                .assertBouquetName()
                .assertVariationsPrices()
                .assertDeliveryPrice()
                .setFirstVariation()
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(firstName, phone, address)
                .getRandomDeliveryDate()
                .getRandomDeliveryTime()
 //               .assertOrderList()
                .pressPayButton();

        paymentPage
                //.assertOrderList()
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessCreatedOrder(baseUrl);
        catalogPage.openAccountOrderPage();
        accountOrderPage.assertCreatedOrderFromAuthUser(baseUrl, yourName);
    }

    @Test
    void test() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity()
                .setRandomBouquet(BouquetType.FLORIST_RU);

        bouquetPage.openBouquetPage(baseUrl)
                .assertBouquetName()
                .assertVariationsPrices()
                .setFirstVariation()
                .setRandomExtras()
                .assertExtras()
                .assertDeliveryPrice()
                .assertTotalPrice()
                .addToCard(baseUrl);

        checkoutPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
                .getRandomDeliveryDate()
                .getRandomDeliveryTime()
                .assertBouquetName()
                .assertDeliveryPrice()
                .assertBouquetPrice()
                .assertExtrasPrice()
                .assertTotalPrice()
                .pressPayButton();

        paymentPage.assertPaymentStatus()
                .assertBouquetName()
                .assertDeliveryPrice()
                .assertBouquetPrice()
                .assertExtrasPrice()
                .assertTotalPrice()
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessCreatedOrder(baseUrl);
    }
}