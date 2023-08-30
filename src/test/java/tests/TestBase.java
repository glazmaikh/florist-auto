package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.BaseConfig;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class TestBase {
    public static String baseUrl;
    static String cardNumber;
    static String expireNumber;
    static String cvcNumber;
    static String promo;

    @BeforeAll
    static void setUp() throws IOException {
        String testEnv = System.getProperty("TEST_ENV");
        String propertiesFilePath = "src/test/resources/" + testEnv + ".properties";
        new Properties().load(new FileInputStream(propertiesFilePath));

        BaseConfig config = ConfigFactory.create(BaseConfig.class, System.getProperties());
        baseUrl = config.getBaseUrl();
        cardNumber = config.getCardNumber();
        expireNumber = config.getExpireNumber();
        cvcNumber = config.getCvcNumber();
        promo = config.getPromoCode();

        //System.setProperty("webdriver.chrome.driver", "C://webdrivers/chromedriver116.exe");
        Configuration.baseUrl = System.getProperty("base_url", "https://www.stage.florist.local/");
        Configuration.browser = System.getProperty("browser", "chrome");
        RestAssured.baseURI = baseUrl;
        //Configuration.holdBrowserOpen = true;
        Configuration.remote = "http://10.201.0.139:4444/wd/hub";

//        SelenideLogger.addListener("allure", new AllureSelenide());
//        DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setCapability("enableVNC", true);
//        capabilities.setCapability("enableVideo", true);
//        Configuration.browserCapabilities = capabilities;
    }

    @BeforeEach
    void addListener() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();

        Selenide.closeWebDriver();
    }

    @AfterAll
    public static void tearDown() {
        SelenideLogger.removeListener("allure");
    }
}