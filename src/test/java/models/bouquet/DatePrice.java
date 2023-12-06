package models.bouquet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class DatePrice {
//    private int RUB;
//    private double USD;
//    private double EUR;
//    private int KZT;
    @JsonProperty("1")
    public Price price1;

    @JsonProperty("2")
    public Price price2;
}
