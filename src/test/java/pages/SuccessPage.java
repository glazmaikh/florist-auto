package pages;

import com.codeborne.selenide.ElementsCollection;
import helpers.HelperPage;
import lombok.SneakyThrows;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class SuccessPage {
    private final ElementsCollection orderList = $$x("//ul[@class='_3QUYNQ-9']//strong");

    @SneakyThrows
    public SuccessPage assertSuccessCreatedOrder() {
        assertTrue(webdriver().driver().url().contains("success"));
        assertTrue(orderList.stream().anyMatch(e -> e.text().equals(HelperPage.getOrderNumber())),
                "incorrect order number on SuccessPage");

        assertAll(
                //"Проверка оформленного заказа на странице успешной оплаты",
                //() -> assertTrue(orderData.getData().getStatus_text().contains("Оплачен"))

        );
        return this;
    }
}
