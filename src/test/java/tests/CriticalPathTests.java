package tests;

import config.BaseConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pages.BouquetPage;
import pages.CatalogPage;
import pages.OrderPage;
import pages.PaymentPage;

public class CriticalPathTests extends TestBase {
    private final TestData testData = new TestData();
    private final CatalogPage catalogPage = new CatalogPage();
    private final BouquetPage bouquetPage = new BouquetPage();
    private final OrderPage orderPage = new OrderPage();
    private final PaymentPage paymentPage = new PaymentPage();
    private static String baseUrl;
    private static String cardNumber;
    private static String expireNumber;
    private static String cvcNumber;

    @BeforeAll
    static void setConfig() {
        BaseConfig config = ConfigFactory.create(BaseConfig.class, System.getProperties());
        baseUrl = config.getBaseUrl();
        cardNumber = config.getCardNumber();
        expireNumber = config.getExpireNumber();
        cvcNumber = config.getCvcNumber();
    }

    @Test
    void criticalPathTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setCity(testData.getRandomCityName())
                .setBouquet(testData.getBouquet());

        bouquetPage.openBouquetPage(baseUrl, testData.getCitySlug(), testData.getBouquetId())
                .assertBouquetName(testData.getBouquetName())
                .assertVariationsPrices(testData.getBouquet())
                .assertDeliveryPrice(testData.getDeliveryPrice())
                .getFirstVariation()
                .addToCard();

        orderPage.simpleFillForm(testData.yourName, testData.yourEmail, testData.yourPhone,
                        testData.name, testData.phone, testData.address)
                .assertOrderList(testData.getBouquetName(), testData.getBouquetPrice(), testData.getDeliveryPrice());

        paymentPage.assertOrderList(testData.getBouquetName(), testData.getBouquetPrice(), testData.getDeliveryPrice())
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();
    }
}