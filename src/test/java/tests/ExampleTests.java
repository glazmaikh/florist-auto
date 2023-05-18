package tests;

import org.junit.jupiter.api.Test;
import pages.BouquetPage;
import pages.MainPage;
import pages.OrderPage;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ExampleTests extends TestBase{
    MainPage mainPage = new MainPage();
    BouquetPage bouquetPage = new BouquetPage();
    OrderPage orderPage = new OrderPage();
    TestData testData = new TestData();

    String city = testData.getCity();

    @Test
    void testExample1() {
        open("/bouquet-333005534");
        $("._1PuI3GVJ").shouldBe(exist);
        $(byText("Добавить в корзину")).click();
        $("._3s4lh2_t").shouldHave(text("Яркая корзиночка в осенних тонах"));
    }

    @Test
    void criticalPathTest() {
        mainPage.openMainPage()
                .closeCookiePopUp()
                .setDeliveryCity(city)
                .openRandomBouquetPage();

        bouquetPage.clickAddtoCardButton();
        //orderPage.
    }
}
