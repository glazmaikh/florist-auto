package tests;

import models.bouquet.BouquetDataItemDto;
import models.city.CityDataItemDto;

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

    public CityDataItemDto getRandomCity(Map<String, CityDataItemDto> cityMap) {
        Object[] values = cityMap.values().toArray();
        return (CityDataItemDto) values[new Random().nextInt(values.length)];
    }

    public BouquetDataItemDto getRandomBouquet(Map<String, BouquetDataItemDto> bouquetMap) {
        Object[] values = bouquetMap.values().toArray();
        return (BouquetDataItemDto) values[new Random().nextInt(values.length)];
    }
}