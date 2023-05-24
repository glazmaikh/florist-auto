package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import models.city.CityDataItemDto;

import java.time.Duration;
import java.util.Map;
import java.util.Random;

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

    private final ElementsCollection droppedCityList = $$("._8PeWF0tD");
    SelenideElement selectedDeliveryCity = $(".CUvbyl33");
    private final SelenideElement cookiePopUp = $(".bco1zbf0");
    private final SelenideElement cookiePopUpClose = $(".lkfJru7k");
    private final ElementsCollection bouquetList = $$("._3_gf3f0b");

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

//    public List<SelenideElement> isDisplayed(ElementsCollection collection) {
//        List<Boolean> list = new ArrayList<>();
//        for (SelenideElement se : collection) {
//            list.add(se.isDisplayed());
//        }
//        return list;
//    }

    public BouquetPage setBouquet(String city) {
        // прорабатываю логику в классе WithApiExampleTest, чтобы ничего не мешало

        //bouquetList.get(getRandomArrayItem(bouquetList)).click();
        return bouquetPage;
    }

    public MainPage closeCookiePopUp() {
        cookiePopUp.shouldBe(exist, Duration.ofSeconds(10));
        cookiePopUp.shouldBe(visible, Duration.ofSeconds(10));
        if (cookiePopUp.exists()) {
            cookiePopUpClose.click();
        }
        cookiePopUp.shouldNotBe(visible, Duration.ofSeconds(10));


        return this;
    }

    public int getRandomArrayItem(ElementsCollection values) {
        return random.nextInt(values.size());
    }

    public CityDataItemDto getRandomCity(Map<String, CityDataItemDto> cityMap) {
        Object[] values = cityMap.values().toArray();
        return (CityDataItemDto) values[new Random().nextInt(values.length)];
    }
}