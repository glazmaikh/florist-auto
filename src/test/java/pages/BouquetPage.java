package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import models.bouquet.BouquetDataItemDto;
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
    private final ElementsCollection variation = $$x("//div[@class='hmJhIXSe']/div/div");
    private final OrderPage orderPage = new OrderPage();

    public BouquetPage openBouquetPage(String baseUrl, String citySlug, BouquetDataItemDto bouquet) {
        webdriver().shouldHave(url(baseUrl + citySlug + "/bouquet-" + bouquet.getId()));
        bouquetSection.shouldHave(text(bouquet.getName()));

        List<PriceItemDto> priceList = bouquet.getPriceList();
        assertEquals(variation.size(), priceList.size(), "where is your variation?");

        for (int i = 0; i < priceList.size(); i++) {
            assertEquals(Integer.parseInt(variation.get(i).getText().replaceAll("[\\s₽]", "")),
                    priceList.get(i).getPrice().get("RUB").intValue(), "Variations price is not equals");
        }
        return this;
    }

    public OrderPage addToCard() {
        addToCardButton.shouldBe(Condition.exist, Duration.ofSeconds(3)).click();
        return orderPage;
    }
}