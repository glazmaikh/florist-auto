package tests;

import fixtures.AssertFixturesPage;
import helpers.ApiClient;
import helpers.BouquetType;
import helpers.CurrencyType;
import helpers.DeliveryDateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.BouquetPage;
import pages.CatalogPage;
import pages.CheckoutPage;

public class SetAddressTest extends TestBase {
    private CatalogPage catalogPage;
    private BouquetPage bouquetPage;
    private CheckoutPage checkoutPage;

    @BeforeEach
    void setData() {
        ApiClient apiClient = new ApiClient();
        AssertFixturesPage assertFixturesPage = new AssertFixturesPage(apiClient);
        catalogPage = new CatalogPage(apiClient, assertFixturesPage);
        bouquetPage = new BouquetPage(apiClient, assertFixturesPage);
        checkoutPage = new CheckoutPage(apiClient, assertFixturesPage);
    }


    @ParameterizedTest(name = "Тест на применение адреса на странице CatalogPage и проверке на чекауте {0}")
    @ValueSource(strings = {"г Астрахань, ул Ульянова, д 1", "ул Ульянова, д 1"})
    @Tag("address")
    void setValidAddressOnCatalogPageTest(String address) throws Exception {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity();

        catalogPage.openAddressPopUp()
                .setValidAddress(address)
                .saveAddress()
                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB, DeliveryDateType.LOW);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .addToCard(baseUrl);

        checkoutPage.assertRecipientAddress(address)
                .assertSuccessAddress();
    }

    @ParameterizedTest(name = "Тест на применение короткого адреса на странице CatalogPage и проверке на чекауте {0}")
    @ValueSource(strings = {"ул Ульянова"})
    void addressValidShortAlertTest(String address) throws Exception {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity();

        catalogPage.openAddressPopUp()
                .setValidShortAddress(address)
                .saveAddress()
                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB, DeliveryDateType.LOW);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .addToCard(baseUrl);

        checkoutPage.assertRecipientAddress(address)
                .assertShortAddress();
    }

    @ParameterizedTest(name = "Тест на применение невалидного адреса на странице CatalogPage и проверке на чекауте {0}")
    @ValueSource(strings = {"asdasd"})
    void addressNotFoundInDBTest(String address) throws Exception {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setDeliveryCity();

        catalogPage.openAddressPopUp()
                .setInvalidAddress(address)
                .saveAddress()
                .setRandomBouquet(BouquetType.FLORIST_RU, CurrencyType.RUB, DeliveryDateType.LOW);

        bouquetPage.openBouquetPage(baseUrl)
                .setFirstVariation()
                .addToCard(baseUrl);

        checkoutPage.assertRecipientInvalidAddress(address)
                .assertAlertAddress();
    }
}