package models.bouquet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class Price {
    @JsonProperty("USD")
    public double uSD;
    @JsonProperty("EUR")
    public double eUR;
    @JsonProperty("KZT")
    public int kZT;
    @JsonProperty("RUB")
    public int rUB;
}
