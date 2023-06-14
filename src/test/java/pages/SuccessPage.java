package pages;

import com.codeborne.selenide.ElementsCollection;
import helpers.APIClient;
import helpers.HelperPage;
import lombok.SneakyThrows;
import models.order.OrderData;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class SuccessPage {
    private final ElementsCollection orderList = $$x("//ul[@class='_3QUYNQ-9']//strong");
    private final APIClient apiClient = new APIClient();

    @SneakyThrows
    public SuccessPage assertSuccessCreatedOrder() {
        assertTrue(webdriver().driver().url().contains("success"));
        OrderData orderData = apiClient.getOrderData();

        String id = String.valueOf(orderData.getData().getId());
        String deliveryPrice = HelperPage.formatPrice(orderData.getData().getCart().get("1").getPrice().getRUB());
        String totalPrice = HelperPage.formatPrice(orderData.getData().getTotal().getRUB());

        assertAll(
                "Проверка состава заказа на странице успешно оплаченного заказа",
                () -> assertTrue(orderList.stream().anyMatch(e -> e.text().equals(id)), "incorrect order number"),
                () -> assertTrue(orderData.getData().getStatus_text().contains("Оплачен")),
                () -> assertTrue(orderList.stream().anyMatch(e -> e.text().equals(deliveryPrice)), "incorrect delivery price"),
                () -> assertTrue(orderList.stream().anyMatch(e -> e.text().equals(totalPrice)), "incorrect total price")
        );
        return this;
    }
}