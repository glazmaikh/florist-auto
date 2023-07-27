package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import helpers.HelperPage;
import models.bouquet.PriceItemDto;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
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
    public BouquetPage assertVariationsPrices() {
        List<PriceItemDto> priceList = apiClient.getPriceList();
        assertEquals(variation.size(), priceList.size(), "where is your variations?");

        for (int i = 0; i < priceList.size(); i++) {
            assertEquals(Integer.parseInt(variation.get(i).getText().replaceAll("[\\s₽]", "")),
                    HelperPage.doubleToIntRound(priceList.get(i).getPrice().get("RUB")), "Variations price is not equals");
        }
        return this;
    }

    public BouquetPage assertDeliveryPrice() {
        int deliveryPrice = HelperPage.doubleToIntRound(apiClient.getDeliveryPrice());
        if (deliveryPrice > 100) {
            bouquetSection.shouldHave(text(HelperPage.priceRegexRub(String.valueOf(deliveryPrice))));
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

    public BouquetPage setRandomExtras() {
        String extrasName = apiClient.getExtrasName();
        int extrasPrice = HelperPage.doubleToIntRound(apiClient.getPriceExtrasFirstVariationRub());
        for (SelenideElement se : extrases) {
            if (se.getText().contains(extrasName)) {
                assertTrue(HelperPage.priceRegex(se).contains(String.valueOf(extrasPrice)));
                se.$("._1a05w77u ").shouldBe(exist).click();
                break;
            }
        }
        return this;
    }

    public BouquetPage assertExtras() {
        int extrasPrice = HelperPage.doubleToIntRound(apiClient.getPriceExtrasFirstVariationRub());
        bouquetSection.shouldHave(text(apiClient.getExtrasName()));
        bouquetSection.shouldHave(text(apiClient.getExtrasVariationName()));
        if (extrasPrice == 0) {
            bouquetSection.shouldHave(text("бесплатно"));
        } else {
            bouquetSection.shouldHave(text(HelperPage.priceRegexRub(String.valueOf(extrasPrice))));
        }

        return this;
    }

    public BouquetPage assertTotalPrice() {
        int bouquetFirstVariationPrice = apiClient.getBouquetPriceRubList().get(0);
        int extrasPrice = HelperPage.doubleToIntRound(apiClient.getPriceExtrasFirstVariationRub());
        int deliveryPrice = HelperPage.doubleToIntRound(apiClient.getDeliveryPrice());
        int totalPrice = bouquetFirstVariationPrice + extrasPrice + deliveryPrice;

        bouquetSection.shouldHave(text(HelperPage.priceRegexRub(String.valueOf(totalPrice))));
        return this;
    }
}