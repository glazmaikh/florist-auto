package helpers;

import com.codeborne.selenide.SelenideElement;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.*;

public class HelperPage {
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

    public static String getRandomStringFromList(List<LocalDate> list) {
        if (list.isEmpty()) {
            throw new RuntimeException("Нет доступных дат");
        }
        return list.get(new Random().nextInt(list.size())).toString();
    }

    public static LocalTime getRandomStringFromListTEST(List<LocalTime> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    public static String regexMaxPaidDate(String date) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");

        LocalDateTime dateTime = LocalDateTime.parse(date, inputFormatter);
        return dateTime.format(outputFormatter);
    }

    public static String getRandomLowDeliveryDay(List<LocalDate> disabledDaysList) throws Exception {
        LocalDate currentDate = LocalDate.now();
        LocalDate afterDate = currentDate.plusMonths(2);
        List<LocalDate> dateList = new ArrayList<>();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (currentDate.isBefore(afterDate)) {
            boolean isLeapYear = Year.of(currentDate.getYear()).isLeap();

            boolean lowDate = !(currentDate.getMonthValue() == 2 && currentDate.getDayOfMonth() >= 10 &&
                    currentDate.getDayOfMonth() <= 14) &&
                    !(currentDate.getMonthValue() == 3 && currentDate.getDayOfMonth() <= 10) &&
                    !(isLeapYear && currentDate.getMonthValue() == 2 && currentDate.getDayOfMonth() == 29);

            if (lowDate) {
                dateList.add(LocalDate.parse(dateFormat.format(currentDate)));
            }
            currentDate = currentDate.plusDays(1);
        }
        dateList.removeAll(disabledDaysList);
        return getRandomStringFromList(dateList);
    }

    public static String getRandomHighFebruaryDeliveryDay(List<LocalDate> disabledDaysList) {
        LocalDate currentDate = LocalDate.now();
        LocalDate afterDate = currentDate.plusMonths(2);
        List<LocalDate> dateList = new ArrayList<>();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (currentDate.isBefore(afterDate)) {
            boolean highFebruaryDate = currentDate.getMonthValue() == 2 &&
                    currentDate.getDayOfMonth() >= 10 && currentDate.getDayOfMonth() <= 14;

            if (highFebruaryDate) {
                dateList.add(LocalDate.parse(dateFormat.format(currentDate)));
            }
            currentDate = currentDate.plusDays(1);
        }
        dateList.removeAll(disabledDaysList);
        return getRandomStringFromList(dateList);
    }

    public static String getRandomHighMarchDeliveryDay(List<LocalDate> disabledDaysList) {
        LocalDate currentDate = LocalDate.now();
        LocalDate afterDate = currentDate.plusMonths(2);
        List<LocalDate> dateList = new ArrayList<>();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (currentDate.isBefore(afterDate)) {
            boolean highMarchDate = currentDate.getMonthValue() == 3 && currentDate.getDayOfMonth() <= 10;

            if (highMarchDate) {
                dateList.add(LocalDate.parse(dateFormat.format(currentDate)));
            }
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

    public static LocalTime getRandomTimeInterval(LocalTime dateFrom, LocalTime dateTo) {
        List<LocalTime> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        dates.add(LocalTime.parse(dateFrom.format(formatter)));

        LocalTime current = dateFrom;
        while (current.isBefore(dateTo) || current.equals(dateTo)) {
            dates.add(LocalTime.parse(current.format(formatter)));
            current = current.plusMinutes(15);
        }
        dates.add(LocalTime.parse(dateTo.format(formatter)));
        return getRandomStringFromListTEST(dates);
    }

    public static boolean isOrderSectionContainsAllFromBouquets(SelenideElement orderList, List<String> prices) {
        return prices.stream().allMatch(name -> orderList.getText().contains(name));
    }

    public static String priceCurrencyFormat(CurrencyType currencyType, String price) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String cleanedPrice = price.replaceAll("[^\\d.,]", "");

        return switch (currencyType) {
            case EUR, USD -> cleanedPrice.equals("0.00") || cleanedPrice.equals("0.0") ? "0.00" :
                    currencyType.getSymbol() +
                            decimalFormat.format(Double.parseDouble(cleanedPrice.replaceAll("(\\d)(?=(\\d{3})+$)", "$1")))
                                    .replace(",", ".");
            case KZT, RUB -> {
                int intValue = Integer.parseInt(cleanedPrice.split("\\.")[0]);
                yield String.valueOf(intValue).replaceAll("(\\d)(?=(\\d{3})+$)", "$1 ") + " " + currencyType.getSymbol();
            }
        };
    }

    public static boolean containsDecimalNumber(String input) {
        String pattern = "\\d+\\.\\d{2}$";
        return input.matches(pattern);
    }

    public static String formatCurrencySum(double sum, CurrencyType currencyType) {
        String pattern = null;
        switch (currencyType) {
            case RUB, KZT -> pattern = "#,###";
            case EUR, USD -> pattern = "#,##0.00";
        }
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(sum).replaceAll(",", ".");
    }

    public static String formatDateDeliveryDateParseToSite(String formatDate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("d MMMM", new Locale("ru"));
        LocalDate date = LocalDate.parse(formatDate, inputFormatter);
        return date.format(outputFormatter);
    }

    public static String formatFromApi(Object fromApi) {
        if (fromApi == null) {
            return null;
        } else {
            double value = ((Number) fromApi).doubleValue();
            return String.format(Locale.US,"%.2f", value);
        }
    }
}