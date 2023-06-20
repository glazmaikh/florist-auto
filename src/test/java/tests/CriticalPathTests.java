package tests;

import config.BaseConfig;
import helpers.ApiClient;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.*;

public class CriticalPathTests extends TestBase {
    private final TestData testData = new TestData();
    private final ApiClient apiClient = new ApiClient();
    private final CatalogPage catalogPage = new CatalogPage(apiClient);
    private final BouquetPage bouquetPage = new BouquetPage(apiClient);
    private final OrderPage orderPage = new OrderPage(apiClient);
    private final PaymentPage paymentPage = new PaymentPage();
    private final SuccessPage successPage = new SuccessPage();
    private static String baseUrl;
    private static String cardNumber;
    private static String expireNumber;
    private static String cvcNumber;
    private String yourName, yourEmail, yourPhone, name, phone, address;

    @BeforeAll
    static void setConfig() {
        BaseConfig config = ConfigFactory.create(BaseConfig.class, System.getProperties());
        baseUrl = config.getBaseUrl();
        cardNumber = config.getCardNumber();
        expireNumber = config.getExpireNumber();
        cvcNumber = config.getCvcNumber();
    }

    @BeforeEach
    void setData() {
        yourName = testData.getYourName();
        yourEmail = testData.getYourEmail();
        yourPhone = testData.getYourPhone();
        name = testData.getName();
        phone = testData.getPhone();
        address = testData.getAddress();
    }

    @Test
    void createNewOrderTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setRandomCity()
                .setRandomBouquet();

        bouquetPage.openBouquetPage(baseUrl)
                .assertBouquetName()
                .assertVariationsPrices()
                .assertDeliveryPrice()
                .getFirstVariation()
                .addToCard();

        orderPage.simpleFillForm(yourName, yourEmail, yourPhone, name, phone, address)
                .getRandomDeliveryDay()
                .assertOrderList()
                .pressPayButton();

        paymentPage.assertOrderList()
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessCreatedOrder();
    }
}