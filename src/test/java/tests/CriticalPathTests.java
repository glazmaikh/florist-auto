package tests;

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

    @Test
    void criticalPathTest() {
        catalogPage.openMainPage()
                .closeCookiePopUp()
                .setCity(testData.getRandomCityName())
                .setBouquet(testData.getBouquet());

        bouquetPage.openBouquetPage(testData.getCitySlug(), testData.getBouquet())
                .addToCard();

        orderPage.simpleFillForm(testData.yourName, testData.yourEmail, testData.yourPhone,
                testData.name, testData.phone, testData.address);

        paymentPage.fillCard()
                .pay()
                .confirm();
    }
}