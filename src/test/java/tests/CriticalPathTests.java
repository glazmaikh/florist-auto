package tests;

import helpers.ApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.*;

public class CriticalPathTests extends TestBase {
    private final TestData testData = new TestData();
    private final ApiClient apiClient = new ApiClient();
    private final CatalogPage catalogPage = new CatalogPage(apiClient);
    private final BouquetPage bouquetPage = new BouquetPage(apiClient);
    private final OrderPage orderPage = new OrderPage(apiClient);
    private final PaymentPage paymentPage = new PaymentPage(apiClient);
    private final SuccessPage successPage = new SuccessPage(apiClient);
    private String yourName, yourEmail, yourPhone, name, phone, address;

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
    @Tag("create_order")
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