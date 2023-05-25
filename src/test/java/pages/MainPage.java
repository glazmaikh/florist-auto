package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainPage {
    private final SelenideElement deliveryPopUp = $("#confirm");
    private final SelenideElement deliveryPopUpYes = $x("//span[text()='Да']");
    private final SelenideElement deliveryPopUpCityNo = $x("//span[text()='Другой город']");
    private final SelenideElement deliveryPopUpModal = $x("//span[text()='Укажите город доставки в поле:']");
    private final SelenideElement deliveryPopUpInput = $("#location-select");
    private final SelenideElement cityLoader = $(".css-1gl4k7y");
    private final SelenideElement bouquetLoader = $(".w0pOM9kK");
    private final ElementsCollection droppedCityList = $$("._8PeWF0tD");
    private final SelenideElement selectedDeliveryCity = $(".CUvbyl33");
    private final SelenideElement cookiePopUp = $(".bco1zbf0");
    private final SelenideElement cookiePopUpClose = $(".lkfJru7k");
    private final ElementsCollection bouquetList = $$("._3JY3BA25");
    private final SelenideElement findMoreButton = $("//span[text()='Показать ещё']");

    BouquetPage bouquetPage = new BouquetPage();
    public MainPage openMainPage() {
        open("https://www.stage.florist.local");
        return this;
    }

    public MainPage setDeliveryCity(String city) {
        deliveryPopUp.shouldBe(exist);
        if (city.equals("Москва")) {
            deliveryPopUpYes.click();
        } else {
            deliveryPopUpCityNo.click();
            deliveryPopUpModal.shouldBe(exist);
            deliveryPopUpInput.val(city);

            cityLoader.shouldNotBe(visible, Duration.ofSeconds(10));
            for (SelenideElement se : droppedCityList) {
                if (se.getOwnText().contains(city)) {
                    se.click();
                    break;
                }
            }
        }
        assertEquals(city, selectedDeliveryCity.getText(),
                "На странице товаров отображается не выбранный город доставки");
        return this;
    }

    public BouquetPage setBouquet(String bouquet) {
        bouquetLoader.shouldNotBe(visible, Duration.ofSeconds(10));
        int count = 0;
        for (SelenideElement se : bouquetList) {
            if (se.getOwnText().contains(bouquet)) {
                se.click();
                break;
            } else if (count == bouquetList.size()) {
                findMoreButton.shouldBe(exist, Duration.ofSeconds(10)).click();
            } else {
                count++;
            }
        }
        return bouquetPage;
    }

    public MainPage closeCookiePopUp() {
        cookiePopUp.shouldBe(visible, Duration.ofSeconds(10));
        cookiePopUpClose.click();

        cookiePopUp.shouldNotBe(visible, Duration.ofSeconds(10));
        return this;
    }
}