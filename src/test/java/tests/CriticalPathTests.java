package tests;

import com.beust.ah.A;
import config.BaseConfig;
import helpers.APIClient;
import helpers.HelperPage;
import io.restassured.path.json.JsonPath;
import models.bouquet.BouquetDataItemDto;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.*;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CriticalPathTests extends TestBase {
    private final TestData testData = new TestData();
    private final CatalogPage catalogPage = new CatalogPage();
    private final BouquetPage bouquetPage = new BouquetPage();
    private final OrderPage orderPage = new OrderPage();
    private final PaymentPage paymentPage = new PaymentPage();
    private final SuccessPage successPage = new SuccessPage();
    private static String baseUrl;
    private static String cardNumber;
    private static String expireNumber;
    private static String cvcNumber;
    private String cityName;
    private BouquetDataItemDto bouquet;
    private String citySlug;
    private int bouquetId;
    private String bouquetName;
    private int deliveryPrice;
    private String yourName, yourEmail, yourPhone, name, phone, address;
    private int bouquetPrice;

    @BeforeAll
    static void setConfig() {
        BaseConfig config = ConfigFactory.create(BaseConfig.class, System.getProperties());
        baseUrl = config.getBaseUrl();
        cardNumber = config.getCardNumber();
        expireNumber = config.getExpireNumber();
        cvcNumber = config.getCvcNumber();
    }

    @BeforeEach
    void setData() {
        cityName = testData.getRandomCityName();
        bouquet = testData.getBouquet();
        citySlug = testData.getCitySlug();
        bouquetId = testData.getBouquetId();
        bouquetName = testData.getBouquetName();
        deliveryPrice = testData.getDeliveryPrice();
        yourName = testData.getYourName();
        yourEmail = testData.getYourEmail();
        yourPhone = testData.getYourPhone();
        name = testData.getName();
        phone = testData.getPhone();
        address = testData.getAddress();
        bouquetPrice = testData.getBouquetPrice();
    }

    @Test
    void criticalPathTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setCity(cityName)
                .setBouquet(bouquet);

        bouquetPage.openBouquetPage(baseUrl, citySlug, bouquetId)
                .assertBouquetName(bouquetName)
                .assertVariationsPrices(bouquet)
                .assertDeliveryPrice(deliveryPrice)
                .getFirstVariation()
                .addToCard();

        orderPage.simpleFillForm(yourName, yourEmail, yourPhone, name, phone, address)
                .getRandomDeliveryDay()
                .assertOrderList(bouquetName, bouquetPrice, deliveryPrice)
                .pressPayButton();

        paymentPage.assertOrderList()
                .fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();

        successPage.assertSuccessCreatedOrder();
    }

//    @Test
//    void test() {
//        APIClient apiClient = new APIClient();
//        HelperPage helperPage = new HelperPage();
//
//        String randomRussianCityName = helperPage.getRandomRussianCityName();
//        System.out.println(randomRussianCityName);
//
//        //String randomBouquet = helperPage.getRandomBouquetName(randomCityName);
//    }
}