package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import helpers.BouquetType;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tests.TestBase.baseUrl;

public class CatalogPage {
    private final ApiClient apiClient;
    private final SelenideElement cookiePopUp = $("._3bcT6MiV");
    private final SelenideElement cookiePopUpClose = $(".lkfJru7k");
    private final SelenideElement deliveryCityModal = $x("//span[text()='Укажите город доставки в поле:']");
    private final SelenideElement cityPopUpInput = $("#location-select");
    private final SelenideElement cityLoader = $(".css-1gl4k7y");
    private final ElementsCollection droppedCityList = $$("._8PeWF0tD");
    private final SelenideElement bouquetLoader = $(".w0pOM9kK");
    private final ElementsCollection bouquetList = $$("._3fIsQ45s");
    private final SelenideElement authRegisterButton = $("button[aria-label='Войти на сайт']");
    private final SelenideElement createAccountTab = $x("//ul/span[text()='Создать аккаунт']");
    private final SelenideElement nameInput = $(byName("name"));
    private final SelenideElement phoneInput = $(byName("phone"));
    private final SelenideElement emailInput = $(byName("email"));
    private final SelenideElement passwordInput = $(byName("password"));
    private final SelenideElement repeatPasswordInput = $(byName("repeatPassword"));
    private final SelenideElement loginInput = $(byName("login"));
    private final SelenideElement privacyPolicyInput = $x("//div[@class='boxes_item']//label");
    private final SelenideElement privacyAlert = $x("//span[text()='Вы должны согласиться с условиями']");
    private final SelenideElement emptyFieldAlert = $x("//span[text()='Поле обязательно для заполнения']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement deliveryCity = $(".CUvbyl33");
    private final SelenideElement accountOrdersButton = $("button[aria-label='Перейти в личный кабинет']");
    private final SelenideElement registerNewAccountButton = $x("//span[text()='Создать аккаунт']/parent::button");
    private final SelenideElement userNotFoundSpan = $x("//span[text()='User not found']");
    private final ElementsCollection incorrectPassword = $$x("//span[text()='Invalid password']");
    private final ElementsCollection minimumPasswordError = $$x("//span[text()='Минимум 6 символов']");
    private final ElementsCollection emptyFieldsErrors = $$x("//span[text()='Поле обязательно для заполнения']");
    private final SelenideElement alertIncorrectPhoneInput = $x("//span[text()='Введите корректный номер телефона']");
    private final SelenideElement alertIncorrectEmailInput = $x("//span[text()='Введите корректный email адрес']");

    public CatalogPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public CatalogPage openCatalogPage(String baseUrl) {
        open(baseUrl);
        webdriver().shouldHave(url(baseUrl));
        return this;
    }

    public AccountOrderPage openAccountOrderPage() {
        accountOrdersButton.shouldBe(exist).click();
        return new AccountOrderPage(apiClient);
    }

    public CatalogPage setDeliveryCity() {
        deliveryCity.shouldBe(exist).click();
        deliveryCityModal.shouldBe(exist);

        String cityName = apiClient.getCityName();
        cityPopUpInput.val(cityName);

        cityLoader.shouldNotBe(visible, Duration.ofSeconds(10));
        for (SelenideElement se : droppedCityList) {
            if (se.getOwnText().contains(cityName)) {
                se.click();
                break;
            }
        }
        return this;
    }

    public BouquetPage setRandomBouquet(BouquetType bouquetType) {
        apiClient.initBouquet(bouquetType);
        bouquetLoader.shouldNotBe(visible, Duration.ofSeconds(30));
        String bouquetName = apiClient.getBouquetName();
        String bouquetPrice = String.valueOf(apiClient.getBouquetPrice());
        int page = 1;

        boolean foundBouquet = false;
        while (!foundBouquet) {
            for (SelenideElement se : bouquetList) {
                if (se.getText().contains(bouquetName)) {
                    assertEquals(bouquetPrice, se.$("._1KvrG3Aq").getText().replaceAll("\\D", ""),
                            "Incorrect price " + bouquetName);
                    se.click();
                    foundBouquet = true;
                    break;
                }
            }
            if (!foundBouquet) {
                String nextPageUrl = baseUrl + "?page=" + (page + 1);
                open(nextPageUrl);
                page++;
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
        authRegisterButton.shouldBe(exist).click();
        createAccountTab.shouldBe(exist).click();
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

        registerNewAccountButton.shouldBe(exist).click();
        return this;
    }

    public CatalogPage openAuthModal() {
        authRegisterButton.shouldBe(exist).click();
        return this;
    }

    public AccountOrderPage fillAuthForm(String email, String password) {
        loginInput.sendKeys(email);
        emptyFieldAlert.shouldNotBe(exist);

        passwordInput.sendKeys(password);
        emptyFieldAlert.shouldNotBe(exist);

        submitButton.shouldBe(exist).click();
        return new AccountOrderPage(apiClient);
    }

    public CatalogPage fillUnAuthForm(String email, String password) {
        loginInput.sendKeys(email);
        passwordInput.sendKeys(password);
        loginInput.click();
        return this;
    }

    public CatalogPage apiRegisterUser(String name, String email, String phone, String password) {
        apiClient.apiRegisterUser(name, email, phone, password);
        return this;
    }

    public CatalogPage assertUnAuth() {
        webdriver().shouldHave(url(baseUrl));
        userNotFoundSpan.shouldBe(visible);
        return this;
    }

    public CatalogPage assertAuthIncorrectPass() {
        webdriver().shouldHave(url(baseUrl));
        incorrectPassword.shouldHave(size(2));
        for (SelenideElement se : incorrectPassword) {
            assertEquals("Invalid password", se.getText());
        }
        return this;
    }

    public CatalogPage assertInvalidPasswords() {
        webdriver().shouldHave(url(baseUrl));
        minimumPasswordError.shouldHave(size(2));
        for (SelenideElement se : minimumPasswordError) {
            assertEquals("Минимум 6 символов", se.getText());
        }
        return this;
    }

    public CatalogPage assertEmptyRegisterFields() {
        webdriver().shouldHave(url(baseUrl));
        emptyFieldsErrors.shouldHave(size(5));
        for (SelenideElement se : emptyFieldsErrors) {
            assertEquals("Поле обязательно для заполнения", se.getText());
        }
        return this;
    }

    public CatalogPage assertEmptyAuthFields() {
        webdriver().shouldHave(url(baseUrl));
        emptyFieldsErrors.shouldHave(size(2));
        for (SelenideElement se : emptyFieldsErrors) {
            assertEquals("Поле обязательно для заполнения", se.getText());
        }
        return this;
    }

    public CatalogPage assertAddedIncorrectRegisterPhone(String phone) {
        phoneInput.shouldBe(exist).click();
        nameInput.click();
        phoneInput.click();
        assertEquals("Введите корректный номер телефона", alertIncorrectPhoneInput.getText());

        phoneInput.sendKeys(phone);
        assertEquals("Введите корректный номер телефона", alertIncorrectPhoneInput.getText());
        return this;
    }

    public CatalogPage assertAddedIncorrectRegisterEmail(String email) {
        emailInput.shouldBe(exist).sendKeys(email);
        nameInput.click();
        assertEquals("Введите корректный email адрес", alertIncorrectEmailInput.getText());
        return this;
    }
}