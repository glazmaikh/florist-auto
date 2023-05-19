package tests;

import java.util.Random;

public class TestData {
    static Random random = new Random();

    // добавить вызов апи списка всех городов
    public String getCity() {
        String[] cityArray = {"Москва", "Санкт-Петербург", "Астрахань", "Барнаул", "Владивосток"};
        return getRandomArrayItem(cityArray);
    }

    // добавить вызов апи списка букетов
    //, "Небесный сапфир", "Любезный Пич", "51 инстаграм", "Хризантемы осенью", "Марио"
    public String getBouquet() {
        String[] bouquetArray = {"вечер в Баварии"};
        return getRandomArrayItem(bouquetArray);
    }

    public String getRandomArrayItem(String[] values) {
        return values[random.nextInt(values.length)];
    }
}
