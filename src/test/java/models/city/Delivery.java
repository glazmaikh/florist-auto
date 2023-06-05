package models.city;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class Delivery {
    @JsonProperty("RUB")
    public String rub;
    @JsonProperty("USD")
    public String usd;
    @JsonProperty("EUR")
    public String eur;
    @JsonProperty("KZT")
    public String kzt;
}
