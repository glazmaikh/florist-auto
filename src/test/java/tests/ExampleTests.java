package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;


public class ExampleTests extends TestBase{
    MainPage mainPage = new MainPage();
    TestData testData = new TestData();

    String city = testData.getCity();

    @Test
    void criticalPathTest() {
        mainPage.openMainPage()
                .closeCookiePopUp()
                .setDeliveryCity(city)
                .setBouquet(city);
    }
}