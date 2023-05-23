package tests;

import models.DataItemDto;

import java.util.Map;
import java.util.Random;

public class TestData {

    // добавить вызов апи списка букетов
    public String getBouquet() {
        String[] bouquetArray = {"вечер в Баварии"};
        return getRandomArrayItem(bouquetArray);
    }

    public String getRandomArrayItem(String[] values) {
        return values[new Random().nextInt(values.length)];
    }

    public DataItemDto getRandomCity(Map<String, DataItemDto> cityMap) {
        Object[] values = cityMap.values().toArray();
        return (DataItemDto) values[new Random().nextInt(values.length)];
    }
}