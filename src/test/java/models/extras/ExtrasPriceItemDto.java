package models.extras;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class ExtrasPriceItemDto {
    private String id;
    private String name;
    private Map<String, Double> price;
    @JsonProperty("date_price")
    private Map<String, ExtrasPrice> datePrice;
}