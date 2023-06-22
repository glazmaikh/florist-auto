package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.BaseConfig;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;

public class TestBase {
    static String baseUrl;
    static String cardNumber;
    static String expireNumber;
    static String cvcNumber;
    @BeforeAll
    static void setUp() {
        //System.setProperty("webdriver.chrome.driver", "C://webdrivers/chromedriver114.exe");
        Configuration.holdBrowserOpen = true;
        Configuration.remote = "http://10.201.0.139:4444/wd/hub";
        //Configuration.browser = "edge";

        BaseConfig config = ConfigFactory.create(BaseConfig.class, System.getProperties());
        baseUrl = config.getBaseUrl();
        cardNumber = config.getCardNumber();
        expireNumber = config.getExpireNumber();
        cvcNumber = config.getCvcNumber();

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
    }

    @AfterAll
    public static void tearDown() {
        SelenideLogger.removeListener("allure");
    }
}
