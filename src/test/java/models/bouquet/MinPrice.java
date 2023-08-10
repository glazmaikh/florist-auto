package models.bouquet;

import com.fasterxml.jackson.annotation.JsonProperty;
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
}
