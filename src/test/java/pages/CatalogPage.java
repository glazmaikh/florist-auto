package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import models.bouquet.BouquetDataItemDto;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CatalogPage {
    private final SelenideElement cookiePopUp = $("._3bcT6MiV");
    private final SelenideElement cookiePopUpClose = $(".lkfJru7k");
    private final SelenideElement cityPopUp = $("#confirm");
    private final SelenideElement cityPopUpYes = $x("//span[text()='Да']");
    private final SelenideElement cityPopUpCityNo = $x("//span[text()='Другой город']");
    private final SelenideElement cityPopUpModal = $x("//span[text()='Укажите город доставки в поле:']");
    private final SelenideElement cityPopUpInput = $("#location-select");
    private final SelenideElement selectedCity = $(".CUvbyl33");
    private final SelenideElement cityLoader = $(".css-1gl4k7y");
    private final ElementsCollection droppedCityList = $$("._8PeWF0tD");
    private final SelenideElement bouquetLoader = $(".w0pOM9kK");
    private final ElementsCollection bouquetList = $$("._3fIsQ45s");
    private final SelenideElement findMoreButton = $("//span[text()='Показать ещё']");
    private final BouquetPage bouquetPage = new BouquetPage();
    private final ApiClient apiClient = new ApiClient();

    public CatalogPage openCatalogPage(String baseUrl) {
        open(baseUrl);
        webdriver().shouldHave(url(baseUrl));
        return this;
    }

    public CatalogPage setCity(String city) {
        cityPopUp.shouldBe(exist);
        if (city.equals("Москва")) {
            cityPopUpYes.click();
        } else {
            cityPopUpCityNo.click();
            cityPopUpModal.shouldBe(exist);
            cityPopUpInput.val(city);

            cityLoader.shouldNotBe(visible, Duration.ofSeconds(10));
            for (SelenideElement se : droppedCityList) {
                if (se.getOwnText().contains(city)) {
                    se.click();
                    break;
                }
            }
        }
        assertEquals(city, selectedCity.getText(),
                "На странице товаров не отображается выбранный город доставки");
        return this;
    }

    public CatalogPage setRandomCity() {
        apiClient.getRandomCityId();
        return this;
    }

    public BouquetPage setBouquet(BouquetDataItemDto bouquet) {
        bouquetLoader.shouldNotBe(visible, Duration.ofSeconds(30));
        int count = 0;
        for (SelenideElement se : bouquetList) {
            if (se.getText().contains(bouquet.getName())) {
                assertEquals(String.valueOf(bouquet.getMin_price().getRub()),
                        se.$("._1KvrG3Aq").getText().replaceAll("\\D", ""),
                        "Incorrect price " + bouquet.getName());
                se.click();
                break;
            } else if (count == bouquetList.size()) {
                findMoreButton.shouldBe(exist, Duration.ofSeconds(30)).click();
            } else {
                count++;
            }
        }
        return bouquetPage;
    }

    public CatalogPage closeCookiePopUp() {
        cookiePopUp.shouldBe(visible, Duration.ofSeconds(10));
        cookiePopUpClose.shouldBe(visible).click();
        cookiePopUp.shouldNotBe(visible, Duration.ofSeconds(10));
        return this;
    }
}