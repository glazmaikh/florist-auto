package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.ApiClient;

import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import models.disabledDelivery.DisabledDeliveryDateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.*;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class CriticalPathTests extends TestBase {
    private final TestData testData = new TestData();
    private CatalogPage catalogPage;
    private BouquetPage bouquetPage;
    private CreatingOrderPage creatingOrderPage;
    private PaymentPage paymentPage;
    private SuccessPage successPage;
    private OrderPage orderPage;
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

        ApiClient apiClient = new ApiClient();

        catalogPage = new CatalogPage(apiClient);
        bouquetPage = new BouquetPage(apiClient);
        creatingOrderPage = new CreatingOrderPage(apiClient);
        paymentPage = new PaymentPage(apiClient);
        successPage = new SuccessPage(apiClient);
        orderPage = new OrderPage(apiClient);
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

        successPage.assertSuccessCreatedOrder(baseUrl);
    }

    @Disabled("Before remade captcha")
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
        catalogPage.apiRegisterUser(yourName, yourEmail, yourPhone, password)
                .openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openAuthModal()
                .fillAuthForm(yourEmail, password);

        orderPage.assertAuth(baseUrl, yourName);
    }

    @Test
    void test() {
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
                .getDeliveryDateWithoutDisabled();
    }
}