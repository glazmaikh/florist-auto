package tests;

import com.beust.ah.A;
import config.BaseConfig;
import helpers.ApiClient;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreatedOrderTests {
    private static String baseUrl;
    private static String cardNumber;
    private static String expireNumber;
    private static String cvcNumber;
    private String cityName;
    private final TestData testData = new TestData();
    private final ApiClient apiClient = new ApiClient();

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
        //cityName = testData.getCityNameFromList();
    }

    @Test
    public void test() {
        String cityName = apiClient.getRandomCityName();
        System.out.println(cityName);

//        String cityId = apiClient.getRandomCityId();
//        System.out.println(cityId);

//        String bouquetName = apiClient.getRandomBouquetName(cityId);
//        System.out.println(bouquetName);

        String slug = apiClient.getSlug();
        System.out.println(slug);

        apiClient.getDeliveryPrice(slug);

//        String deliveryPrice = apiClient.getDeliveryPrice();
//        System.out.println(deliveryPrice);
    }
}
