package models.bouquet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class DatePrice {
    @JsonProperty("RUB")
    private int rub;
    @JsonProperty("USD")
    private double usd;
    @JsonProperty("EUR")
    private double eur;
    @JsonProperty("KZT")
    private int kzt;
}
