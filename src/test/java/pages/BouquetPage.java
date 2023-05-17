package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class BouquetPage {

    private final SelenideElement addToCardButton = $x("//span[text()='Добавить в корзину']");

    OrderPage orderPage = new OrderPage();

    public OrderPage clickAddtoCardButton() {
        addToCardButton.shouldBe(Condition.exist).click();
        return orderPage;
    }

//    public OrderPage clickAddtoCardButton() {
//        addToCardButton.shouldBe(Condition.exist).click();
//        return orderPage;
//    }
}
