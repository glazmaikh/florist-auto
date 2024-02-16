package models.cityAlias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class Delivery {
    private double RUB;
    private int USD;
    private double EUR;
    private int KZT;
}

