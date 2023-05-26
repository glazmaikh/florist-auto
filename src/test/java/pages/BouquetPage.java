package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import models.bouquet.BouquetDataItemDto;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BouquetPage {
    private final SelenideElement addToCardButton = $x("//span[text()='Добавить в корзину']");
    private final SelenideElement bouquetSection = $("#bouquet-main");
    private final SelenideElement priceSection = bouquetSection.$(".no-wrap");
    private final OrderPage orderPage = new OrderPage();

    public BouquetPage openBouquetPage(String citySlug, BouquetDataItemDto bouquet) {
        webdriver().shouldHave(url("https://www.stage.florist.local/" + citySlug + "/bouquet-" + bouquet.getId()));

        bouquetSection.shouldHave(Condition.text(bouquet.getName()));

        String price = priceSection.getText().replaceAll(" ", "");
        assertEquals(String.valueOf(bouquet.getMin_price().getRub()), price.substring(0, price.length() - 1), "Incorrect price");
        return this;
    }

    public OrderPage addToCard() {
        addToCardButton.shouldBe(Condition.exist, Duration.ofSeconds(3)).click();
        return orderPage;
    }
}