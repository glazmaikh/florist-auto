package models.bouquet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.CurrencyType;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class MinDatePrice {
    @JsonProperty("RUB")
    private double rub;
    @JsonProperty("USD")
    private double usd;
    @JsonProperty("KZT")
    private double kzt;
    @JsonProperty("EUR")
    private double eur;

    public double getCurrency(CurrencyType currencyType) {
        return switch (currencyType) {
            case RUB -> getRub();
            case USD -> getUsd();
            case KZT -> getKzt();
            case EUR -> getEur();
            default -> throw new IllegalArgumentException("Unsupported currency type: " + currencyType);
        };
    }
}
