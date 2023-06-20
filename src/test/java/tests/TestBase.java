package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {
    @BeforeAll
    public static void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        System.setProperty("webdriver.chrome.driver", "C://webdrivers/chromedriver114.exe");
        Configuration.holdBrowserOpen = true;
        //Configuration.remote = "https://login:password@selenoid.autotests.cloud/wd/hub";
    }

    @AfterAll
    public static void tearDown() {
        SelenideLogger.removeListener("allure");
    }
}
