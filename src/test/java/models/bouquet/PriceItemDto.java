package models.bouquet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class PriceItemDto {
    public String name;
    public Price price;
}
