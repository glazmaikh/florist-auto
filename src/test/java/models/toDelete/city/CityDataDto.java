package models.toDelete.city;

import lombok.Data;

import java.util.Map;

public @Data class CityDataDto {
    public int ok;
    public String error;
    public String meta;
    public double t;
    public Map<String, CityDataItemDto> data;
}
