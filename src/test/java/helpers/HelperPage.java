package helpers;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelperPage {
    public static int totalPrice(int bouquetPrice, int deliveryPrice) {
        return bouquetPrice + deliveryPrice;
    }

    public static void assertBouquetPrice(int bouquetPrice, SelenideElement element) {
        assertEquals(String.valueOf(bouquetPrice), element.getText().replaceAll("[\\s₽]", ""));
    }

    public static void assertDeliveryPrice(int deliveryPrice, SelenideElement element) {
        assertEquals(String.valueOf(deliveryPrice), element.getText().replaceAll("[\\s₽]", ""));
    }

    public static SelenideElement getRandomDeliveryDay(ElementsCollection collection) {
        return collection.get(new Random().nextInt(collection.size()));
    }
}
