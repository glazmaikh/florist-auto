package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class SuccessPage {

    private final SelenideElement thanksFor = $x("//h1[text() ='Спасибо за заказ']");
}
