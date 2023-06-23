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
    private final CreatingOrderPage creatingOrderPage = new CreatingOrderPage(apiClient);
    private final PaymentPage paymentPage = new PaymentPage(apiClient);
    private final SuccessPage successPage = new SuccessPage(apiClient);
    private final OrderPage orderPage = new OrderPage(apiClient);
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
                .addToCard(baseUrl);

        creatingOrderPage.simpleFillForm(yourName, yourEmail, yourPhone, firstName, phone, address)
                .getRandomDeliveryDay()
                .assertOrderList()
                .pressPayButton();

        paymentPage.assertOrderList()
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessCreatedOrder();
    }

    @Test
    @Tag("register")
    void registerTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openRegisterModal()
                .fillRegisterForm(yourName, phone, yourEmail, password)
                .makeCaptcha();
    }

    @Test
    @Tag("auth")
    void authTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openAuthModal()
                .fillAuthForm("test123123@test.ru", "123123");

        orderPage.assertAuth();
    }
}