package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import models.DataDto;
import models.DataItemDto;
import org.junit.jupiter.api.Timeout;
import pages.components.DeliveryComponent;

import java.time.Duration;
import java.util.Map;
import java.util.Random;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainPage {
    private final SelenideElement deliveryPopUp = $("#confirm");
    private final SelenideElement deliveryPopUpYes = $x("//span[text()='Да']");
    private final SelenideElement deliveryPopUpCityNo = $x("//span[text()='Другой город']");
    private final SelenideElement deliveryPopUpModal = $x("//span[text()='Укажите город доставки в поле:']");
    private final SelenideElement deliveryPopUpInput = $("#location-select");
    private final SelenideElement deliveryPopUpFindCityItem = $(".css-11unzgr");
    private final ElementsCollection dropDownCityList = $$("._8PeWF0tD");
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
            deliveryPopUpFindCityItem.shouldHave(exist);

            // придумать ожидание
            droppedCityList.shouldHave(sizeGreaterThan(5));
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

    public BouquetPage setBouquet(String city) {
        // прорабатываю логику в классе WithApiExampleTest, чтобы ничего не мешало

        //bouquetList.get(getRandomArrayItem(bouquetList)).click();
        return bouquetPage;
    }

    public MainPage closeCookiePopUp() {
        cookiePopUp.shouldBe(exist);
        if (cookiePopUp.exists()) {
            cookiePopUpClose.click();
        }
        cookiePopUp.shouldNotBe(exist);
        return this;
    }

    public int getRandomArrayItem(ElementsCollection values) {
        return random.nextInt(values.size());
    }

    public DataItemDto getRandomCity(Map<String, DataItemDto> cityMap) {
        Object[] values = cityMap.values().toArray();
        return (DataItemDto) values[new Random().nextInt(values.length)];
    }
}