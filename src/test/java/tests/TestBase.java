package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.BaseConfig;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.selenide.LogType;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;

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
        new Properties().load(new FileInputStream(propertiesFilePath));

        BaseConfig config = ConfigFactory.create(BaseConfig.class, System.getProperties());
        baseUrl = config.getBaseUrl();
        cardNumber = config.getCardNumber();
        expireNumber = config.getExpireNumber();
        cvcNumber = config.getCvcNumber();
        promo = config.getPromoCode();
        login = config.getLogin();
        password = config.getPassword();

        //System.setProperty("webdriver.chrome.driver", "C://webdrivers/chromedriver119.exe");
        Configuration.baseUrl = System.getProperty("base_url", "https://www." + testEnv + ".florist.local/");
        //Configuration.baseUrl = System.getProperty("base_url", "https://www.test.florist.local/");
        Configuration.browser = System.getProperty("browser", "chrome");
        RestAssured.baseURI = baseUrl;
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
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Samara"));
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