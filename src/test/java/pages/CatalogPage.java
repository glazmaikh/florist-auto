package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import fixtures.AssertFixturesPage;
import helpers.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tests.TestBase.baseUrl;

public class CatalogPage {
    private final ApiClient apiClient;
    private AssertFixturesPage assertFixturesPage;
    private final SelenideElement cookiePopUpCross = $x("(.//*[normalize-space(text()) and normalize-space(.)='Флорист.ру — международная доставка цветов и букетов'])[1]/following::*[name()='svg'][3]");
    private final SelenideElement cookiePopUp = $(".bco1zbf0");
    private final SelenideElement cookiePopUpCrossArea = $(".lkfJru7k");
    private final SelenideElement deliveryCityModal = $x("//span[text()='Укажите город доставки в поле:']");
    private final SelenideElement cityPopUpInput = $("#location-select");
    private final SelenideElement cityLoader = $(".css-1gl4k7y");
    private final ElementsCollection droppedCityList = $$("._8PeWF0tD");
    private final SelenideElement bouquetLoader = $(".w0pOM9kK");
    private ElementsCollection bouquetList = $$("._3fIsQ45s");
    private final SelenideElement authRegisterButton = $("button[aria-label='Войти на сайт']");
    private final SelenideElement createAccountTab = $x("//ul/span[text()='Создать аккаунт']");
    private final SelenideElement nameInput = $(byName("name"));
    private final SelenideElement phoneInput = $(byName("phone"));
    private final SelenideElement emailInput = $(byName("email"));
    private final SelenideElement passwordInput = $(byName("password"));
    private final SelenideElement repeatPasswordInput = $(byName("repeatPassword"));
    private final SelenideElement loginInput = $(byName("login"));
    private final SelenideElement privacyPolicyInput = $x("//div[@class='boxes_item']//label");
    private final SelenideElement privacyAlert = $(byText("Вы должны согласиться с условиями"));
    private final SelenideElement emptyFieldAlert = $x("//span[text()='Поле обязательно для заполнения']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement deliveryCity = $("._3APPs71Z");
    private final SelenideElement accountOrdersButton = $("button[aria-label='Перейти в личный кабинет']");
    private final SelenideElement registerNewAccountButton = $x("//span[text()='Создать аккаунт']/parent::button");
    private final SelenideElement userNotFoundSpan = $x("//span[text()='User not found']");
    private final ElementsCollection incorrectPassword = $$x("//span[text()='Invalid password']");
    private final ElementsCollection minimumPasswordError = $$x("//span[text()='Минимум 6 символов']");
    private final ElementsCollection emptyFieldsErrors = $$x("//span[text()='Поле обязательно для заполнения']");
    private final SelenideElement alertIncorrectPhoneInput = $x("//span[text()='Введите корректный номер телефона']");
    private final SelenideElement alertIncorrectEmailInput = $x("//span[text()='Введите корректный email адрес']");
    private final SelenideElement userAlreadyExistsError = $(byText("User with this email already exists"));
    private final SelenideElement currencyDropper = $(byText("₽ Руб."));
    private final SelenideElement setKztPrice = $(byText("₸ Казахстанский тенге"));
    private final SelenideElement setRubPrice = $(byText("₽ Российский рубль"));
    private final SelenideElement setEurPrice = $(byText("€ Евро"));
    private final SelenideElement setUsdPrice = $(byText("$ Доллар США"));
    private final SelenideElement addressButton = $(".UfnjhKsH");
    private final SelenideElement addressInput = $(byName("recipientAddressSource"));
    private final ElementsCollection addressDroppedItems = $$("._3e9oSM9z");
    private final SelenideElement saveAddressButton = $x(".//button[@type='submit']");
    private final SelenideElement addressAlert = $(".pfxmD-Os");
    private final SelenideElement addressSuccessItem = $(".fr_DHwvM");
    private final SelenideElement deliveryDayButton = $x(".//*[@class='_1KRc46hs']/ancestor::button");
    private final SelenideElement deliverySelectedDay = $x(".//*[@class='_1KRc46hs']/following-sibling::span");
    private ElementsCollection deliveryAllDays = $$x("//button[contains(@class, 'react-calendar__tile') and not(@disabled)]/abbr");
    private final SelenideElement nextMonthButton = $(".react-calendar__navigation__next-button");

    private String deliveryDate;

    public CatalogPage(ApiClient apiClient, AssertFixturesPage assertFixturesPage) {
        this.apiClient = apiClient;
        this.assertFixturesPage = assertFixturesPage;
    }

    public CatalogPage openCatalogPage(String baseUrl) {
        open(baseUrl);
        return this;
    }

    public AccountOrderPage openAccountOrderPage() {
        accountOrdersButton.shouldBe(exist).click();
        return new AccountOrderPage(apiClient);
    }

    public CatalogPage setDeliveryCity() throws InterruptedException {
        deliveryCity.shouldBe(visible).click();
        deliveryCityModal.shouldBe(visible);

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

    public BouquetPage setRandomBouquet(CurrencyType currencyType, DeliveryDateType deliveryDateType) {
        bouquetLoader.shouldNotBe(visible, Duration.ofSeconds(30));
        String bouquetName = apiClient.getBouquetName();
        String bouquetPrice = apiClient.getBouquetPriceList(currencyType, deliveryDateType).toString();
        int page = 1;

        boolean foundBouquet = false;
        while (!foundBouquet) {
            bouquetList.shouldHave(sizeGreaterThanOrEqual(apiClient.getBouquetListReminder()));
            for (SelenideElement se : bouquetList) {
                if (se.getText().contains(bouquetName)) {
                    assertTrue(se.$("._1KvrG3Aq").getText().contains(HelperPage.priceCurrencyFormat(currencyType, bouquetPrice)),
                            "Incorrect bouquet price " + bouquetName);
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
        return new BouquetPage(apiClient, new AssertFixturesPage(apiClient));
    }

    public BouquetPage setRandomBouquetTest(CurrencyType currencyType, DeliveryDateType deliveryDateType) {
        bouquetLoader.shouldNotBe(visible, Duration.ofSeconds(30));
        String bouquetName = apiClient.getBouquetName();
        String bouquetPrice = apiClient.getBouquetPrice(currencyType, deliveryDateType);
        int page = 1;

        boolean foundBouquet = false;
        while (!foundBouquet) {
            bouquetList.shouldHave(sizeGreaterThanOrEqual(apiClient.getBouquetListReminder()));
            for (SelenideElement se : bouquetList) {
                if (se.getText().contains(bouquetName)) {
                    assertTrue(se.$("._1KvrG3Aq").getText().contains(HelperPage.priceCurrencyFormat(currencyType, bouquetPrice)),
                            "Incorrect bouquet price " + bouquetName);
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
        return new BouquetPage(apiClient, new AssertFixturesPage(apiClient));
    }

    public CatalogPage closeCookiePopUp() {
        cookiePopUpCross.shouldBe(visible, Duration.ofSeconds(15));
        cookiePopUp.shouldBe(visible, Duration.ofSeconds(15));

        boolean cookiePopUpClosed = false;
        while (!cookiePopUpClosed) {
            cookiePopUpCrossArea.shouldBe(visible, Duration.ofSeconds(15)).click();
            if (cookiePopUp.is(visible)) {
                System.out.println("Попап кук не закрылся с 1 раза");
                cookiePopUpCrossArea.shouldBe(visible, Duration.ofSeconds(15)).click();
            } else {
                cookiePopUpClosed = true;
            }
        }
        cookiePopUp.shouldNotBe(visible, Duration.ofSeconds(15));
        return this;
    }

    public CatalogPage assertDeliveryCity() {
        String cityName = apiClient.getCityName();
        deliveryCity.shouldBe(visible);
        assertEquals(deliveryCity.getText(), cityName, "deliveryCity not equals on CatalogPage");
        return this;
    }

    public String setRandomDeliveryDate(DeliveryDateType deliveryDateType) throws Exception {
        List<LocalDate> disabledDaysList = apiClient.getDisabledDeliveryDaysList();

        switch (deliveryDateType) {
            case LOW -> deliveryDate = HelperPage.getRandomLowDeliveryDay(disabledDaysList);
            case HiGH_FEBRUARY -> deliveryDate = HelperPage.getRandomHighFebruaryDeliveryDay(disabledDaysList);
            case HIGH_MARCH -> deliveryDate = HelperPage.getRandomHighMarchDeliveryDay(disabledDaysList);
        }

        deliveryDayButton.shouldBe(exist).click();
        boolean foundDate = false;
        while (!foundDate) {
            for (SelenideElement se : deliveryAllDays) {
                if (Objects.requireNonNull(se.getAttribute("aria-label")).contains(HelperPage.formatDateDeliveryDateParse(deliveryDate))) {
                    se.shouldBe(exist).click();
                    foundDate = true;
                    break;
                }
            }
            if (!foundDate) {
                nextMonthButton.shouldBe(exist).click();
                deliveryAllDays = $$x("//button[contains(@class, 'react-calendar__tile') and not(@disabled)]/abbr");
            }
        }
        return deliveryDate;
    }

    public CatalogPage assertDeliveryDate(String deliveryDate) {
        deliveryDayButton.shouldBe(visible);
        assertEquals(HelperPage.formatDateDeliveryDateParseToSite(deliveryDate), deliverySelectedDay.getText());
        return this;
    }

    public CatalogPage openRegisterModal() {
        authRegisterButton.shouldBe(exist).click();
        createAccountTab.shouldBe(exist).click();
        return this;
    }

    public CatalogPage fillRegisterForm(String name, String phone, String email, String password) {
        nameInput.shouldBe(exist).sendKeys(name);
        phoneInput.shouldBe(exist).sendKeys(phone);
        emailInput.shouldBe(exist).sendKeys(email);
        phoneInput.shouldBe(exist).sendKeys(email);
        passwordInput.shouldBe(exist).sendKeys(password);
        repeatPasswordInput.shouldBe(exist).sendKeys(password);
        privacyPolicyInput.shouldBe(exist).click();
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

    public CatalogPage assertNotRegisterWithoutAcceptPolicy(String name, String phone, String email, String password) {
        nameInput.shouldBe(exist).sendKeys(name);
        phoneInput.sendKeys(phone);
        emailInput.sendKeys(email);
        phoneInput.sendKeys(email);
        passwordInput.sendKeys(password);
        repeatPasswordInput.sendKeys(password);

        registerNewAccountButton.scrollTo().shouldBe(exist).click();
        privacyAlert.shouldBe(exist);
        webdriver().shouldHave(url(baseUrl));
        return this;
    }

    public CatalogPage assertAlreadyExistsEmailWhenRegister() {
        userAlreadyExistsError.shouldBe(exist);
        return this;
    }

    public CatalogPage setCurrency(CurrencyType currencyType) {
        currencyDropper.shouldBe(exist).click();
        switch (currencyType) {
            case EUR -> setEurPrice.shouldBe(exist).click();
            case KZT -> setKztPrice.shouldBe(exist).click();
            case USD -> setUsdPrice.shouldBe(exist).click();
            //case RUB -> setRubPrice.shouldBe(exist).click();
        }
        return this;
    }

    public CatalogPage openAddressPopUp() {
        addressButton.shouldBe(exist).click();
        return this;
    }

    public CatalogPage setInputAddress(String address) {
        addressInput.shouldBe(exist).val(address);

        boolean wait = true;
        while (wait) {
            if (addressDroppedItems.size() == 3) {
                for (SelenideElement se : addressDroppedItems) {
                    if (se.getText().contains(address)) {
                        se.shouldBe(exist).click();
                        break;
                    }
                }
                wait = false;
            }
        }
        return this;
    }

    public CatalogPage setValidAddress(String address) {
        setInputAddress(address);
        addressSuccessItem.shouldBe(exist);
        return this;
    }

    public CatalogPage setValidShortAddress(String address) {
        setInputAddress(address);
        addressAlert.shouldBe(exist);
        assertEquals("Укажите, пожалуйста, адрес с точностью до дома",
                addressAlert.getText());
        return this;
    }

    public CatalogPage setInvalidAddress(String address) {
        addressInput.shouldBe(exist).val(address);

        addressDroppedItems.shouldHave(size(1));
        for (SelenideElement se : addressDroppedItems) {
            if (se.getText().equals(address)) {
                se.shouldBe(exist).click();
            }
        }
        addressAlert.shouldBe(exist);
        assertEquals("Выбранный адрес не найден в базе. Если вы уверены в его правильности, продолжите оформление.",
                addressAlert.getText());
        return this;
    }

    public CatalogPage saveAddress() {
        saveAddressButton.shouldBe(exist).click();
        return this;
    }
}