package tests;

import fixtures.AssertFixturesPage;
import helpers.ApiClient;
import helpers.BouquetType;
import helpers.CurrencyType;
import helpers.DeliveryDateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.*;

public class HighSeasonTests extends TestBase {
//    private CatalogPage catalogPage;
//    private BouquetPage bouquetPage;
//    private ApiClient apiClient;
//
//    @BeforeEach
//    void setData() {
//        apiClient = new ApiClient();
//        AssertFixturesPage assertFixturesPage = new AssertFixturesPage(apiClient);
//
//        catalogPage = new CatalogPage(apiClient, assertFixturesPage);
//        bouquetPage = new BouquetPage(apiClient, assertFixturesPage);
//    }
//
//    @Test
//    @Tag("high_season")
//    void lowPriceOnCatalogAndBouquetPageTest() throws Exception{
//        apiClient.initBouquet(BouquetType.FLORIST_RU);
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                .assertDeliveryCity()
//                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB, DeliveryDateType.LOW);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .setFirstVariation()
//                .assertBouquetName()
//                .assertBouquetPrice(CurrencyType.RUB, DeliveryDateType.LOW);
//    }
}
