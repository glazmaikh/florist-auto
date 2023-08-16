package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import helpers.CurrencyType;
import helpers.HelperPage;
import models.bouquet.PriceItemDto;
import models.extras.ExtrasDataItemDto;
import models.extras.ExtrasPrice;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BouquetPage {
    private final SelenideElement addToCardButton = $x("//span[text()='Добавить в корзину']");
    private final SelenideElement bouquetSection = $("#bouquet-main");
    private final SelenideElement deliveryPriceSection = $(".UFVGkjKP");
    private final ElementsCollection variation = $$x("//div[@class='hmJhIXSe']/div/div");
    private final ElementsCollection extrases = $$("._38l21lFz");
    private final ApiClient apiClient;
    private static final int extrasPrice = 0;

    public BouquetPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public BouquetPage openBouquetPage(String baseUrl) {
        webdriver().shouldHave(url(baseUrl + apiClient.getCitySlug() + "/bouquet-" + apiClient.getBouquetId()));
        return this;
    }

    public BouquetPage assertBouquetName() {
        bouquetSection.shouldHave(text(apiClient.getBouquetName()));
        return this;
    }
//    // не сработает для акции, вариации не по порядку

    public BouquetPage assertVariationsPrices(CurrencyType currencyType) {
        List<PriceItemDto> priceList = apiClient.getPriceList();
        assertEquals(variation.size(), priceList.size(), "no variations");

        for (int i = 0; i < priceList.size(); i++) {
            assertEquals(Double.parseDouble(variation.get(i).getText().replaceAll("[^\\d.]+", "")),
                    priceList.get(i).getPrice().get(currencyType.name()), "Variations price is not equals");
        }
        return this;
    }

    public BouquetPage assertDeliveryPrice(CurrencyType currencyType) {
        String deliveryPrice = apiClient.getDeliveryPrice(currencyType);

        if (Double.parseDouble(deliveryPrice) > 1) {
            bouquetSection.shouldHave(text(HelperPage.priceRegex(deliveryPrice)));
        } else {
            deliveryPriceSection.shouldBe(text("бесплатно"));
        }
        return this;
    }

    public BouquetPage setFirstVariation() {
        variation.get(0).click();
        return this;
    }

    public CheckoutPage addToCard(String baseUrl) {
        addToCardButton.shouldBe(Condition.exist, Duration.ofSeconds(5)).click();
        webdriver().shouldHave(url(baseUrl + apiClient.getCitySlug() + "/checkout"), Duration.ofSeconds(10));
        return new CheckoutPage(apiClient);
    }

    public BouquetPage setRandomExtras(CurrencyType currencyType) {
        apiClient.initExtras();
        String extrasName = apiClient.getExtrasName();
        String extrasPrice = apiClient.getPriceExtrasFirstVariation(currencyType);
        for (SelenideElement se : extrases) {
            if (se.getText().contains(extrasName)) {
                assertTrue(HelperPage.priceRegex(se).contains(String.valueOf(extrasPrice)));
                se.$("._1a05w77u ").shouldBe(exist).click();
                break;
            }
        }
        return this;
    }

    public BouquetPage assertExtras(CurrencyType currencyType) {
        String extrasPrice = apiClient.getPriceExtrasFirstVariation(currencyType);
        bouquetSection.shouldHave(text(apiClient.getExtrasName()));
        bouquetSection.shouldHave(text(apiClient.getExtrasVariationName()));
        if (Double.parseDouble(extrasPrice) == 0) {
            bouquetSection.shouldHave(text("бесплатно"));
        } else {
            bouquetSection.shouldHave(text(HelperPage.priceRegex(extrasPrice)));
        }

        return this;
    }

    public BouquetPage assertTotalPrice(CurrencyType currencyType) {
        String bouquetFirstVariationPrice = apiClient.getBouquetPriceList(currencyType).get(apiClient.getBouquetPriceList(currencyType).size() - 1);
        String deliveryPrice = apiClient.getDeliveryPrice(currencyType);
        double totalPrice = Double.parseDouble(bouquetFirstVariationPrice) + Double.parseDouble(deliveryPrice);

        if (apiClient.getPriceExtrasFirstVariation(currencyType) != null) {
            totalPrice += Double.parseDouble(apiClient.getPriceExtrasFirstVariation(currencyType));
        }

        if (HelperPage.containsDecimalNumber(bouquetFirstVariationPrice)) {
            bouquetSection.shouldHave(text(HelperPage.formatToCents(totalPrice)));
        } else {
            bouquetSection.shouldHave(text(HelperPage.priceRegex(String.valueOf(totalPrice).replaceAll("\\.(\\d+)", ""))));
        }
        return this;
    }

//    public BouquetPage assertTotalPriceWithExtras(CurrencyType currencyType) {
//        String bouquetFirstVariationPrice = apiClient.getBouquetPriceList(currencyType).get(apiClient.getBouquetPriceList(currencyType).size() - 1);
//        String deliveryPrice = apiClient.getDeliveryPrice(currencyType);
//        String extrasPrice = apiClient.getPriceExtrasFirstVariation(currencyType);
//        double totalPrice = Double.parseDouble(bouquetFirstVariationPrice) +  Double.parseDouble(extrasPrice) + Double.parseDouble(deliveryPrice);
//
//        if (!extrasPrice.isEmpty()) {
//            totalPrice += Double.parseDouble(extrasPrice);
//        }
//
//        if (HelperPage.containsDecimalNumber(bouquetFirstVariationPrice)) {
//            bouquetSection.shouldHave(text(HelperPage.formatToCents(totalPrice)));
//        } else {
//            bouquetSection.shouldHave(text(HelperPage.priceRegex(String.valueOf(totalPrice).replaceAll("\\.(\\d+)", ""))));
//        }
//        return this;
//    }
}