package helpers;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.*;
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
        }
        return matcher.group(1);
    }

    public static String getOrderAccessKey() {
        String url = webdriver().driver().url();
        Pattern pattern = Pattern.compile(".*/([^/]+)$");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            return null;
        }
        return matcher.group(1);
    }

    public static String formatPriceRub(String formatPrice) {
        return formatPrice.replaceAll("[\\s,.\u20BD\"\n]", "") + "\u20BD";
    }

    public static String formatCreatedDate(String formatDate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter
                .ofPattern("d MMMM yyyy 'г.'", new Locale("ru"));
        LocalDate date = LocalDate.parse(formatDate, inputFormatter);
        return date.format(outputFormatter);
    }

    public static List<String> convertDates(List<String> dates) {
        List<String> convertedDates = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");

        for (String date : dates) {
            Matcher matcher = pattern.matcher(date);

            if (matcher.matches()) {
                int day = Integer.parseInt(matcher.group(3));
                int month = Integer.parseInt(matcher.group(2));
                int year = Integer.parseInt(matcher.group(1));

                String formattedDate = String.format("%d %s %d г.", day, getMonthName(month), year);
                convertedDates.add(formattedDate);
            } else {
                convertedDates.add("Invalid date format");
            }
        }

        return convertedDates;
    }

    private static String getMonthName(int month) {
        String[] monthNames = {
                "января", "февраля", "марта", "апреля",
                "мая", "июня", "июля", "августа",
                "сентября", "октября", "ноября", "декабря"
        };
        return monthNames[month - 1];
    }

    public static List<String> getListFromAriaLabelAttribute(ElementsCollection collection) {
        List<String> textList = new ArrayList<>();

        for (SelenideElement element : collection) {
            String ariaLabel = element.getAttribute("aria-label");
            if (ariaLabel != null && !ariaLabel.isEmpty()) {
                textList.add(ariaLabel);
            }
        }
        return textList;
    }

    public static String getRandomStringFromList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(new Random().nextInt(list.size()));
    }

    public static String deliveryDataRegex(String date) {
        String[] parts = date.split(" ");
        return parts[parts.length - 1];
    }

    public static String regexMaxPaidDate(String date) {
        String[] parts = date.split(" ");
        String datePart = parts[0];
        String timePart = parts[1];

        String[] dateParts = datePart.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        String[] timeParts = timePart.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);
        int seconds = Integer.parseInt(timeParts[2]);

        hours += 3;
        if (hours >= 24) {
            hours -= 24;
            LocalDate originalDate = LocalDate.of(year, month, day);
            LocalDate nextDay = originalDate.plusDays(1);
            year = nextDay.getYear();
            month = nextDay.getMonthValue();
            day = nextDay.getDayOfMonth();
        }

        String formattedDate = String.format("%02d.%02d.%04d", day, month, year);
        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return formattedDate + ", " + formattedTime;
    }
}