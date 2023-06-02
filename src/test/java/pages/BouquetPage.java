package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import models.bouquet.BouquetDataItemDto;
import models.bouquet.PriceItemDto;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BouquetPage {
    private final SelenideElement addToCardButton = $x("//span[text()='Добавить в корзину']");
    private final SelenideElement bouquetSection = $("#bouquet-main");
    private final SelenideElement priceSection = bouquetSection.$(".no-wrap");
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

    public PriceItemDto getRandomPrice(Map<String, PriceItemDto> prices) {
        List<PriceItemDto> priceItemDtoList = new ArrayList<>(prices.values());
        int randomIndex = new Random().nextInt(priceItemDtoList.size());
        return priceItemDtoList.get(randomIndex);
    }

    /*
    ElementsCollection elements = $$(".your-elements-selector");

// Создаем список ожидаемых значений
List<String> expectedValues = Arrays.asList("Value 1", "Value 2", "Value 3");

// Проверяем, что количество элементов в списке соответствует ожидаемому количеству
Assertions.assertEquals(expectedValues.size(), elements.size());

// Проверяем значения каждого элемента в списке
for (int i = 0; i < elements.size(); i++) {
    String actualValue = elements.get(i).text();
    String expectedValue = expectedValues.get(i);
    Assertions.assertEquals(expectedValue, actualValue);
}
     */
}