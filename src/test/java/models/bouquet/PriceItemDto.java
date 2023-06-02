package models.bouquet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class PriceItemDto {
    public String name;
    public Map<String, Double> price;
}
