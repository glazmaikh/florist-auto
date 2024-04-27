package tests.e2e;

import fixtures.AssertFixturesPage;
import helpers.ApiClient;
import helpers.BouquetType;
import helpers.CurrencyType;
import helpers.DeliveryDateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import pages.*;
import tests.TestBase;

@Tag("e2e_high")
public class HighSeasonTests extends TestBase {
    private CatalogPage catalogPage;
    private BouquetPage bouquetPage;
    private ApiClient apiClient;

    @BeforeEach
    void setData() {
        apiClient = new ApiClient();
        AssertFixturesPage assertFixturesPage = new AssertFixturesPage(apiClient);
        catalogPage = new CatalogPage(apiClient, assertFixturesPage);
        bouquetPage = new BouquetPage(apiClient, assertFixturesPage);
    }

//    @Test
//    void lowPriceOnCatalogAndBouquetPageTest() throws Exception {
//        apiClient.initBouquet(BouquetType.FLORIST_RU);
//
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                .assertDeliveryCity()
//                .setRandomBouquet(CurrencyType.RUB, DeliveryDateType.LOW);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .setFirstVariation()
//                .assertBouquetName()
//                .assertBouquetPrice(CurrencyType.RUB, DeliveryDateType.LOW);
//    }
//
//    @ParameterizedTest
//    @EnumSource(DeliveryDateType.class)
//    void setHighPriceOnCatalogPageTest(DeliveryDateType deliveryDateType) throws Exception {
//        apiClient.initBouquet(BouquetType.FLORIST_RU);
//
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .setDeliveryCity()
//                .assertDeliveryCity();
//
//        String deliveryDate = catalogPage.setRandomDeliveryDate(deliveryDateType);
//
//        catalogPage.assertDeliveryDate(deliveryDate)
//                .setRandomBouquet(CurrencyType.RUB, deliveryDateType);
//
//        bouquetPage.openBouquetPage(baseUrl)
//                .assertDeliveryDate(deliveryDate)
//                .setFirstVariation()
//                .assertBouquetName()
//                .assertBouquetPrice(CurrencyType.RUB, deliveryDateType);
//    }
}