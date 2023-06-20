package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import helpers.HelperPage;
import lombok.SneakyThrows;
import models.order.OrderData;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class SuccessPage {
    private final ElementsCollection orderList = $$x("//li");
    private final SelenideElement totalPrice = $(".no-wrap");
    private final ApiClient apiClient;

    public SuccessPage(ApiClient apiClient) {
    this.apiClient = apiClient;
    }

    @SneakyThrows
    public SuccessPage assertSuccessCreatedOrder() {
        assertTrue(webdriver().driver().url().contains("success"));
        OrderData orderData = apiClient.getOrderData();

        String id = String.valueOf(orderData.getData().getId());
        String orderCreatedDate = HelperPage.formatCreatedDate(orderData.getData().getCreated_at());
        String totalDataPrice = HelperPage.formatPrice(orderData.getData().getTotal().getRUB());

        assertAll(
                "Проверка состава заказа на странице успешно оплаченного заказа",
                () -> assertTrue(orderList.stream().anyMatch(e -> e.getOwnText().replaceAll("\\s+", "").equals(id)),
                        "incorrect order number"),
                () -> assertTrue(orderList.stream().anyMatch(e -> e.getOwnText().trim().equals(orderCreatedDate))),
                () -> assertTrue(orderData.getData().getStatus_text().contains("Оплачен")),
                () -> assertEquals(totalPrice.getText(), totalDataPrice, "incorrect total price")
        );
        return this;
    }
}