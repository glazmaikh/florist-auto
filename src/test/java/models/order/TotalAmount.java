package models.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class TotalAmount {
    @JsonProperty("RUB")
    public double rUB;
    @JsonProperty("USD")
    public double uSD;
    @JsonProperty("EUR")
    public double eUR;
    @JsonProperty("KZT")
    public double kZT;
}
