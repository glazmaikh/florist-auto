package tests;

import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ExampleTests extends TestBase{

    @Test
    void testExample1() {
        open("/bouquet-333005534");
        $("._1PuI3GVJ").shouldBe(exist);
        $(byText("Добавить в корзину")).click();
    }
}
