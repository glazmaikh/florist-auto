package helpers;

import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.SelenideElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelperPage {

    public static int doubleToIntRound(Double value) {
        return (int) Math.round(value);
    }
    public static int totalPrice(int bouquetPrice, int deliveryPrice) {
        return bouquetPrice + deliveryPrice;
    }

    public static void assertPrice(int bouquetPrice, SelenideElement element) {
        assertEquals(String.valueOf(bouquetPrice), priceRegex(element));
    }
    public static String priceRegex(SelenideElement element) {
        return element.getText().replaceAll("[\\s₽]", "");
    }

    public static String getOrderNumber() {
        String path = webdriver().driver().url();
        Pattern pattern = Pattern.compile("/order/payment/(\\d+)/");
        Matcher matcher = pattern.matcher(path);
        if (!matcher.find()) {
            return null;
        } return matcher.group(1);
    }
    public static String getOrderAccessKey() {
        String url = webdriver().driver().url();
        Pattern pattern = Pattern.compile(".*/([^/]+)$");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            return null;
        } return matcher.group(1);
    }

    public static String formatPriceRub(String formatPrice) {
        return formatPrice.replaceAll("[ ,.\"\\n]", "").trim() + "₽";
    }

    public static String formatCreatedDate(String formatDate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter
                .ofPattern("d MMMM yyyy 'г.'", new Locale("ru"));
        LocalDate date = LocalDate.parse(formatDate, inputFormatter);
        return date.format(outputFormatter);
    }
}