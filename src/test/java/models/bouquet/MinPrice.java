package models.bouquet;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.CurrencyType;
import lombok.Data;

public @Data class MinPrice {
    @JsonProperty("RUB")
    public int rub;
    @JsonProperty("USD")
    public double usd;
    @JsonProperty("EUR")
    public double eur;
    @JsonProperty("KZT")
    public int kzt;

    public double getEur() {
        return eur;
    }

    public int getKzt() {
        return kzt;
    }

    public double getUsd() {
        return usd;
    }

    public int getRub() {
        return rub;
    }

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