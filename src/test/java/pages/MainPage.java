package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import pages.components.DeliveryComponent;

import java.util.Random;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {
    private final SelenideElement deliveryPopUp = $("#confirm");
    private final SelenideElement deliveryPopUpYes = $x("//span[text()='Да']");
    private final SelenideElement deliveryPopUpCityNo = $x("//span[text()='Другой город']");
    private final SelenideElement deliveryPopUpModal = $x("//span[text()='Укажите город доставки в поле:']");
    private final SelenideElement deliveryPopUpInput = $("#location-select");
    private final SelenideElement deliveryPopUpFindCityItem = $(".css-oboqqt-menu");
    private final ElementsCollection dropDownCityList = $$("._187JQJYl");
    DeliveryComponent deliveryComponent = new DeliveryComponent();

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
            //todo найти локатор первого выпадающего элемента
        }
        return this;
    }

    public MainPage setRandomDeliveryCity() {
        deliveryPopUp.shouldBe(exist);
        deliveryPopUpCityNo.click();

        deliveryPopUpModal.shouldBe(exist);
        getRandomArrayItem(dropDownCityList).hover().click();
        return this;
    }

    public SelenideElement getRandomArrayItem(ElementsCollection values) {
        int index = random.nextInt(values.size());
        return values.get(index);
    }
}
