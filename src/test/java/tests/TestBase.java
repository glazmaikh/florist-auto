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
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static io.restassured.RestAssured.baseURI;

public class TestBase {
    public static String baseUrl;
    static String cardNumber;
    static String expireNumber;
    static String cvcNumber;
    static String promo;
    static String login;
    static String password;

    @BeforeAll
    static void setUp() throws IOException {
        String testEnv = System.getProperty("TEST_ENV");
        String propertiesFilePath = "src/test/resources/" + testEnv + ".properties";

        Properties properties = new Properties();
        properties.load(new FileInputStream(propertiesFilePath));

        BaseConfig config = ConfigFactory.create(BaseConfig.class, System.getProperties());
        baseUrl = properties.getProperty("base_url", config.getBaseUrl());
        baseURI = baseUrl;
        cardNumber = properties.getProperty("card_number", config.getCardNumber());
        expireNumber = properties.getProperty("expire_number", config.getExpireNumber());
        cvcNumber = properties.getProperty("cvc_number", config.getCvcNumber());
        promo = properties.getProperty("promo_code", config.getPromoCode());
        login = properties.getProperty("login", config.getLogin());
        password = properties.getProperty("password", config.getPassword());

        //System.setProperty("webdriver.chrome.driver", "C://webdrivers/chromedriver119.exe");
        Configuration.browser = System.getProperty("browser", "chrome");
        //Configuration.holdBrowserOpen = true;
        Configuration.remote = "http://10.201.0.139:4444/wd/hub";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.of(
                "enableVNC", true,
                "enableLogs", true,
                "env", List.of("VERBOSE=true")));
        Configuration.browserCapabilities = capabilities;
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void addListener() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
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