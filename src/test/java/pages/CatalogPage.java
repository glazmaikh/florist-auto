package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CatalogPage {
    private ApiClient apiClient;
    private final SelenideElement cookiePopUp = $("._3bcT6MiV");
    private final SelenideElement cookiePopUpClose = $(".lkfJru7k");
    private final SelenideElement cityPopUp = $("#confirm");
    private final SelenideElement cityPopUpYes = $x("//span[text()='Да']");
    private final SelenideElement cityPopUpCityNo = $x("//span[text()='Другой город']");
    private final SelenideElement cityPopUpModal = $x("//span[text()='Укажите город доставки в поле:']");
    private final SelenideElement cityPopUpInput = $("#location-select");
    private final SelenideElement cityLoader = $(".css-1gl4k7y");
    private final ElementsCollection droppedCityList = $$("._8PeWF0tD");
    private final SelenideElement bouquetLoader = $(".w0pOM9kK");
    private final ElementsCollection bouquetList = $$("._3fIsQ45s");
    private final SelenideElement findMoreButton = $("//span[text()='Показать ещё']");
    private final SelenideElement authRegisterButton = $("button[aria-label='Войти на сайт']");
    private final SelenideElement createAccountTab = $x("//ul/span[text()='Создать аккаунт']");
    private final SelenideElement nameInput = $(byName("name"));
    private final SelenideElement phoneInput = $(byName("phone"));
    private final SelenideElement emailInput = $(byName("email"));
    private final SelenideElement passwordInput = $(byName("password"));
    private final SelenideElement repeatPasswordInput = $(byName("repeatPassword"));
    private final SelenideElement subscribeInput = $("._1zXA0QMS ");
    private final SelenideElement captchaInput = $("#recaptcha-anchor-label");
    private final SelenideElement privacyPolicyInput = $x("//div[@class='boxes_item']//label");
    private final SelenideElement privacyAlert = $x("//span[text()='Вы должны согласиться с условиями']");

    public CatalogPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public CatalogPage openCatalogPage(String baseUrl) {
        open(baseUrl);
        webdriver().shouldHave(url(baseUrl));
        return this;
    }

    public CatalogPage setRandomCity() {
        cityPopUp.shouldBe(exist);
        String cityName = apiClient.getRandomCityName();
        if (cityName.equals("Москва")) {
            cityPopUpYes.click();
        } else {
            cityPopUpCityNo.click();
            cityPopUpModal.shouldBe(exist);
            cityPopUpInput.val(cityName);

            cityLoader.shouldNotBe(visible, Duration.ofSeconds(10));
            for (SelenideElement se : droppedCityList) {
                if (se.getOwnText().contains(cityName)) {
                    se.click();
                    break;
                }
            }
        }
        return this;
    }

    public BouquetPage setRandomBouquet() {
        bouquetLoader.shouldNotBe(visible, Duration.ofSeconds(30));
        String bouquetName = apiClient.getBouquetName();

        int count = 0;
        for (SelenideElement se : bouquetList) {
            if (se.getText().contains(bouquetName)) {
                assertEquals(String.valueOf(apiClient.getBouquetPrice()),
                        se.$("._1KvrG3Aq").getText().replaceAll("\\D", ""),
                        "Incorrect price " + bouquetName);
                se.click();
                break;
            } else if (count == bouquetList.size()) {
                findMoreButton.shouldBe(exist, Duration.ofSeconds(30)).click();
            } else {
                count++;
            }
        }
        return new BouquetPage(apiClient);
    }

    public CatalogPage closeCookiePopUp() {
        cookiePopUp.shouldBe(visible, Duration.ofSeconds(15));
        cookiePopUpClose.shouldBe(visible, Duration.ofSeconds(15)).click();
        cookiePopUp.shouldNotBe(visible, Duration.ofSeconds(10));
        return this;
    }

    public CatalogPage openRegisterModal() {
        authRegisterButton.shouldHave(exist).click();
        createAccountTab.shouldHave(exist).click();
        return this;
    }

    public CatalogPage fillRegisterForm(String name, String phone, String email, String password) {
        nameInput.shouldBe(exist).sendKeys(name);
        phoneInput.sendKeys(phone);
        emailInput.sendKeys(email);
        phoneInput.sendKeys(email);
        passwordInput.sendKeys(password);
        repeatPasswordInput.sendKeys(password);
        privacyPolicyInput.click();
        privacyAlert.shouldNotBe(exist);
        captchaInput.shouldBe(exist).click();
        return this;
    }
}