package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import fixtures.AssertFixturesPage;
import helpers.ApiClient;
import helpers.CurrencyType;
import helpers.DeliveryDateType;
import helpers.HelperPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BouquetPage extends AssertFixturesPage {
    private final SelenideElement addToCardButton = $x("//span[text()='Добавить в корзину']");
    private final SelenideElement bouquetSection = $("#bouquet-main");
    private final ElementsCollection variation = $$x("//div[@class='hmJhIXSe']/div/div");
    private final ElementsCollection extrases = $$("._38l21lFz");
    private final SelenideElement deliveryDayButton = $x(".//*[@class='_1KRc46hs']/ancestor::button");
    private final SelenideElement deliverySelectedDay = $x(".//*[@class='_1KRc46hs']/following-sibling::span");
    private ApiClient apiClient;
    private AssertFixturesPage assertFixturesPage;

    public BouquetPage(ApiClient apiClient, AssertFixturesPage assertFixturesPage) {
        super(apiClient);
        this.apiClient = apiClient;
        this.assertFixturesPage = assertFixturesPage;
    }

    public BouquetPage openBouquetPage(String baseUrl) {
        webdriver().shouldHave(url(baseUrl + apiClient.getCitySlug() + "/bouquet-" + apiClient.getBouquetId()));
        return this;
    }

    public BouquetPage assertBouquetName() {
        bouquetSection.shouldHave(text(apiClient.getBouquetName()));
        return this;
    }

    public BouquetPage assertDeliveryPrice(CurrencyType currencyType) {
        assertFixturesPage.performAssertDeliveryPrice(bouquetSection, currencyType);
        return this;
    }

    public BouquetPage assertBouquetPrice(CurrencyType currencyType, DeliveryDateType deliveryDateType) {
        assertFixturesPage.performAssertBouquetPriceList(bouquetSection, currencyType, deliveryDateType);
        return this;
    }

    public BouquetPage setFirstVariation() {
        variation.get(0).click();
        return this;
    }

    public CheckoutPage addToCard(String baseUrl) {
        addToCardButton.shouldBe(Condition.exist, Duration.ofSeconds(15)).click();
        webdriver().shouldHave(url(baseUrl + apiClient.getCitySlug() + "/checkout"), Duration.ofSeconds(10));
        return new CheckoutPage(apiClient, assertFixturesPage);
    }

    public BouquetPage setRandomExtras(CurrencyType currencyType) {
        apiClient.initExtras();
        String extrasName = apiClient.getExtrasName();
        String extrasPrice = apiClient.getPriceExtrasFirstVariation(currencyType);

        for (SelenideElement se : extrases) {
            if (se.getText().contains(extrasName)) {
                assertTrue(se.getText().contains(HelperPage.priceCurrencyFormat(currencyType, extrasPrice)));
                se.$("._1a05w77u ").shouldBe(exist).click();
                break;
            }
        }
        bouquetSection.shouldHave(text(extrasName), Duration.ofSeconds(5));
        return this;
    }

    public BouquetPage assertExtrasPrice(CurrencyType currencyType) {
        String extrasPrice = apiClient.getPriceExtrasFirstVariation(currencyType);
        if (Double.parseDouble(extrasPrice) == 0) {
            bouquetSection.shouldHave(text("бесплатно"));
        } else {
            bouquetSection.shouldHave(text(HelperPage.priceCurrencyFormat(currencyType, extrasPrice)));
        }
        return this;
    }

    public BouquetPage assertTotalMinPrice(CurrencyType currencyType) {
        String bouquetFirstVariationPrice = apiClient.getBouquetMinPrice(currencyType);
        String deliveryPrice = apiClient.getDeliveryPrice(currencyType);
        double totalPrice = Double.parseDouble(bouquetFirstVariationPrice) + Double.parseDouble(deliveryPrice);

        if (apiClient.getExtrasPrice() != null) {
            totalPrice += Double.parseDouble(apiClient.getPriceExtrasFirstVariation(currencyType));
        }
        bouquetSection.shouldHave(text(HelperPage.priceCurrencyFormat(currencyType, String.valueOf(totalPrice))));
        return this;
    }

    public BouquetPage assertDeliveryDate(String deliveryDate) {
        deliveryDayButton.shouldBe(visible);
        assertEquals(HelperPage.formatDateDeliveryDateParseToSite(deliveryDate), deliverySelectedDay.getText());
        return this;
    }
}
