package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import pages.components.DeliveryComponent;

import java.util.Random;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainPage {
    private final SelenideElement deliveryPopUp = $("#confirm");
    private final SelenideElement deliveryPopUpYes = $x("//span[text()='Да']");
    private final SelenideElement deliveryPopUpCityNo = $x("//span[text()='Другой город']");
    private final SelenideElement deliveryPopUpModal = $x("//span[text()='Укажите город доставки в поле:']");
    private final SelenideElement deliveryPopUpInput = $("#location-select");
    private final SelenideElement deliveryPopUpFindCityItem = $(".css-oboqqt-menu");
    private final ElementsCollection dropDownCityList = $$("._8PeWF0tD");
    private final ElementsCollection droppedCityList = $$("._8PeWF0tD");
    SelenideElement selectedDeliveryCity = $(".CUvbyl33");
    private final SelenideElement cookiePopUp = $(".bco1zbf0");
    private final SelenideElement cookiePopUpClose = $(".lkfJru7k");

    private final ElementsCollection bouquetList = $$("._3_gf3f0b");
    DeliveryComponent deliveryComponent = new DeliveryComponent();
    BouquetPage bouquetPage = new BouquetPage();
    static Random random = new Random();

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
            deliveryPopUpFindCityItem.shouldBe(exist);

            for (SelenideElement se : droppedCityList) {
                if (se.getOwnText().contains(city)) {
                    se.click();
                    break;
                }
            }
        }
        assertEquals(city, selectedDeliveryCity.getText(), "На странице товаров отображается не выбранный город доставки");
        return this;
    }

// добавить вызов апи списка всех городов

    public BouquetPage openRandomBouquetPage() {
        bouquetList.shouldHave(size(60));
        bouquetList.get(getRandomArrayItem(bouquetList)).click();
        return bouquetPage;
    }

    public MainPage closeCookiePopUp() {
        if (cookiePopUp.exists()) {
            cookiePopUpClose.click();
        }
        cookiePopUp.shouldNotBe(exist);
        return this;
    }

    public int getRandomArrayItem(ElementsCollection values) {
        return random.nextInt(values.size());
    }
}
