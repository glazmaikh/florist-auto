package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import helpers.HelperPage;
import models.bouquet.PriceItemDto;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BouquetPage {
    private final SelenideElement addToCardButton = $x("//span[text()='Добавить в корзину']");
    private final SelenideElement bouquetSection = $("#bouquet-main");
    private final SelenideElement deliveryPriceSection = $(".UFVGkjKP");
    private final ElementsCollection variation = $$x("//div[@class='hmJhIXSe']/div/div");
    private final ApiClient apiClient;

    public BouquetPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

//    public BouquetPage openBouquetPage(String baseUrl) {
//        webdriver().shouldHave(url(baseUrl + apiClient.getCitySlug() + "/bouquet-" + apiClient.getBouquetId()));
//        return this;
//    }
//
//    public BouquetPage assertBouquetName() {
//        bouquetSection.shouldHave(text(apiClient.getBouquetName()));
//        return this;
//    }
//    // не сработает для акции, вариации не по порядку
//    public BouquetPage assertVariationsPrices() {
//        List<PriceItemDto> priceList = apiClient.getPriceList();
//        assertEquals(variation.size(), priceList.size(), "where is your variations?");
//
//        for (int i = 0; i < priceList.size(); i++) {
//            assertEquals(Integer.parseInt(variation.get(i).getText().replaceAll("[\\s₽]", "")),
//                    HelperPage.doubleToIntRound(priceList.get(i).getPrice().get("RUB")), "Variations price is not equals");
//        }
//        return this;
//    }

//    public BouquetPage assertDeliveryPrice() {
//        // сделать тест на бесплатную/платную доставку
//        int deliveryPrice = HelperPage.doubleToIntRound(apiClient.getDeliveryPrice());
//        if (deliveryPrice > 100) {
//            assertEquals(HelperPage.priceRegex(deliveryPriceSection.$(".no-wrap")),
//                    String.valueOf(deliveryPrice), "Delivery price on Bouquet Page is not equals");
//        } else {
//            deliveryPriceSection.shouldBe(text("бесплатно"));
//        }
//        return this;
//    }

    public BouquetPage getFirstVariation() {
        variation.get(0).click();
        return this;
    }

    public CreatingOrderPage addToCard(String baseUrl) {
        addToCardButton.shouldBe(Condition.exist, Duration.ofSeconds(5)).click();
        //webdriver().shouldHave(url(baseUrl + apiClient.getCitySlug() + "/checkout"), Duration.ofSeconds(10));
        return new CreatingOrderPage(apiClient);
    }
}