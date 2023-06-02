package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.BaseConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import models.bouquet.BouquetDataDto;
import models.bouquet.BouquetDataItemDto;
import models.bouquet.PriceItemDto;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pages.BouquetPage;
import pages.CatalogPage;
import pages.OrderPage;
import pages.PaymentPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CriticalPathTests extends TestBase {
    private final TestData testData = new TestData();
    private final CatalogPage catalogPage = new CatalogPage();
    private final BouquetPage bouquetPage = new BouquetPage();
    private final OrderPage orderPage = new OrderPage();
    private final PaymentPage paymentPage = new PaymentPage();
    private static String baseUrl;
    private static String cardNumber;
    private static String expireNumber;
    private static String cvcNumber;
    BouquetDataItemDto bouquet;

    @BeforeAll
    static void setConfig() {
        BaseConfig config = ConfigFactory.create(BaseConfig.class, System.getProperties());
        baseUrl = config.getBaseUrl();
        cardNumber = config.getCardNumber();
        expireNumber = config.getExpireNumber();
        cvcNumber = config.getCvcNumber();
    }

    @Test
    void criticalPathTest() throws InterruptedException {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .setCity(testData.getRandomCityName())
                .setBouquet(testData.getBouquet());

        bouquetPage.openBouquetPage(baseUrl, testData.getCitySlug(), testData.getBouquet())
                .addToCard();

        orderPage.simpleFillForm(testData.yourName, testData.yourEmail, testData.yourPhone,
                testData.name, testData.phone, testData.address);

        paymentPage.fillCard(cardNumber, expireNumber, cvcNumber)
                .pay()
                .confirm();
    }

    @Test
    void test() throws JsonProcessingException {
        RequestSpecification httpRequest = RestAssured.given();
        ObjectMapper objectMapper = new ObjectMapper();
        Response responseBouquet = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", 1)
                .param("showPrices", 1)
                .param("includeIflorist", 1)
                .get("https://www.test.florist.local/api/bouquet");
        ResponseBody bodyBouquet = responseBouquet.getBody();

        BouquetDataDto bouquetData = objectMapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);
        bouquet = getRandomBouquet(bouquetData.getData());

        Map<String, PriceItemDto> prices = bouquet.getPrices();
        prices.forEach((key, value) -> System.out.println("[Key]: " + key + "[Value]: " + value));
    }

    public BouquetDataItemDto getRandomBouquet(Map<String, BouquetDataItemDto> bouquetMap) {
        Object[] values = bouquetMap.values().toArray();
        return (BouquetDataItemDto) values[new Random().nextInt(values.length)];
    }

    public PriceItemDto getRandomPrice(Map<String, PriceItemDto> prices) {
        List<PriceItemDto> priceItemDtoList = new ArrayList<>(prices.values());
        return priceItemDtoList.get(new Random().nextInt(priceItemDtoList.size()));
    }
}