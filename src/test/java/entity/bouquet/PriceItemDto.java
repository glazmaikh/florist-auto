package entity.bouquet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class PriceItemDto {
    public String id;
    public String name;
    public Map<String, Double> price;
    @JsonProperty("date_price")
    private Map<String, DatePrice> datePrice;
}
