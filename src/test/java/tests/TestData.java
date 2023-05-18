package tests;

import java.util.Random;

public class TestData {
    static Random random = new Random();

    public String getCity() {
        String[] genderArray = {"Москва", "Санкт-Петербург", "Астрахань", "Барнаул", "Владивосток"};
        return getRandomArrayItem(genderArray);
    }

    public String getRandomArrayItem(String[] values) {
        return values[random.nextInt(values.length)];
    }
}
