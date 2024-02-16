package models.cityAlias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public @lombok.Data class CityDataAliasDto {
    private int ok;
    private String error;
    private Data data;
    private Object meta;
    private double t;
}

