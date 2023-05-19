package tests;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {

    @BeforeAll
    static void setUp() {
        System.setProperty("webdriver.chrome.driver", "C://webdrivers/chromedriver.exe");
        //Configuration.baseUrl = "https://www.test.florist.local";
        Configuration.holdBrowserOpen = true;
        //RestAssured.baseURI = "https://www.test.florist.local";
    }
}
