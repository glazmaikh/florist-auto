package tests;

import org.junit.jupiter.api.Test;
import pages.BouquetPage;
import pages.MainPage;

public class CriticalPathTests extends TestBase{
    private final TestData testData = new TestData();
    private final MainPage mainPage = new MainPage();
    private final BouquetPage bouquetPage = new BouquetPage();

    @Test
    void criticalPathTest() {
        mainPage.openMainPage()
                .closeCookiePopUp()
                .setCity(testData.getRandomCityName())
                .setBouquet(testData.getBouquet());

        bouquetPage.openBouquetPage(testData.getCitySlug(), testData.getBouquet())
                .addToCard();
    }
}