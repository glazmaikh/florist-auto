package helpers;

import com.codeborne.selenide.SelenideElement;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.*;

public class HelperPage {

    public static int doubleToIntRound(Double value) {
        return (int) Math.round(value);
    }

    public static String priceRegex(SelenideElement element) {
        return element.getText().replaceAll("[\\s₽]", "");
    }

    public static String priceRegexRub(String price) {
        return price.replaceAll("(\\d)(?=(\\d{3})+$)", "$1 ") + " ₽";
    }

    public static String priceRegex(String price) {
        return price.replaceAll("(\\d)(?=(\\d{3})+$)", "$1 ");
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

    public static String formatCreatedDate(String formatDate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter
                .ofPattern("d MMMM yyyy 'г.'", new Locale("ru"));
        LocalDate date = LocalDate.parse(formatDate, inputFormatter);
        return date.format(outputFormatter);
    }

    public static String formatDateDeliveryDateParse(String formatDate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy 'г.'", new Locale("ru"));
        LocalDate date = LocalDate.parse(formatDate, inputFormatter);
        return date.format(outputFormatter);
    }

    public static String getRandomStringFromList(List<String> list) {
        return list.get(new Random().nextInt(list.size()));
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

    public static String getRandomDeliveryDayWithoutDisabled(List<String> disabledDaysList) {
        LocalDate currentDate = LocalDate.now();
        LocalDate afterDate = currentDate.plusMonths(2);

        List<String> dateList = new ArrayList<>();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (currentDate.isBefore(afterDate)) {
            dateList.add(dateFormat.format(currentDate));
            currentDate = currentDate.plusDays(1);
        }

        dateList.removeAll(disabledDaysList);
        return getRandomStringFromList(dateList);
    }

    public static LocalTime doubleToTime(double value) {
        int hours = (int) value;
        int minutes = (int) ((value - hours) * 60);
        return LocalTime.of(hours, minutes);
    }

    public static String getRandomTimeInterval(LocalTime dateFrom, LocalTime dateTo) {
        List<String> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        dates.add(dateFrom.format(formatter));

        LocalTime current = dateFrom;
        while (current.isBefore(dateTo) || current.equals(dateTo)) {
            dates.add(current.format(formatter));
            current = current.plusMinutes(15);
        }
        dates.add(dateTo.format(formatter));
        return getRandomStringFromList(dates);
    }

    public static boolean isOrderListContainsAllFromBouquets(SelenideElement orderList, List<String> names) {
        return names.stream().allMatch(name -> orderList.getText().contains(name));
    }

    public static int sumIntegerList(List<Integer> list) {
        return list.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}