package models.bouquet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public @Data class MinPrice {
    @JsonProperty("RUB")
    public int rub;
    @JsonProperty("USD")
    public int usd;
    @JsonProperty("EUR")
    public int eur;
    @JsonProperty("KZT")
    public int kzt;
}
