package helpers;

import com.codeborne.selenide.SelenideElement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelperPage {
    public static int totalPrice(int bouquetPrice, int deliveryPrice) {
        return bouquetPrice + deliveryPrice;
    }

    public static void assertBouquetPrice(int bouquetPrice, SelenideElement element) {
        assertEquals(String.valueOf(bouquetPrice), priceRegex(element));
    }

    public static void assertDeliveryPrice(int deliveryPrice, SelenideElement element) {
        assertEquals(String.valueOf(deliveryPrice), priceRegex(element));
    }
    public static String priceRegex(SelenideElement element) {
        return element.getText().replaceAll("[\\s₽]", "");
    }
}