package models.bouquet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class Prices {
    @JsonProperty("0")
    public String _0;
    public Map<Object, PriceItemDto> priceItemDtoMap;
}
