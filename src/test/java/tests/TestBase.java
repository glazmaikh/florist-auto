package tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {
    @BeforeAll
    static void setUp() {
        //System.setProperty("webdriver.chrome.driver", "C://webdrivers/chromedriver114.exe");
        Configuration.holdBrowserOpen = true;
        Configuration.remote = "https://login:password@selenoid.autotests.cloud/wd/hub";
    }
}
