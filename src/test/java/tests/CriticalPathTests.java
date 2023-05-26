package tests;

import org.junit.jupiter.api.Test;
import pages.BouquetPage;
import pages.CatalogPage;

public class CriticalPathTests extends TestBase{
    private final TestData testData = new TestData();
    private final CatalogPage catalogPage = new CatalogPage();
    private final BouquetPage bouquetPage = new BouquetPage();

    @Test
    void criticalPathTest() {
        catalogPage.openMainPage()
                .closeCookiePopUp()
                .setCity(testData.getRandomCityName())
                .setBouquet(testData.getBouquet());

        bouquetPage.openBouquetPage(testData.getCitySlug(), testData.getBouquet())
                .addToCard();
    }
}