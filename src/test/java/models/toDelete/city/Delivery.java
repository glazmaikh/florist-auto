package models.toDelete.city;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class Delivery {
    @JsonProperty("RUB")
    public int rub;
    @JsonProperty("USD")
    public int usd;
    @JsonProperty("EUR")
    public int eur;
    @JsonProperty("KZT")
    public int kzt;
}
