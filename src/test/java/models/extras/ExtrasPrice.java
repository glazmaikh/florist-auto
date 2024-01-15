package models.extras;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class ExtrasPrice {
    private String id;
    private String name;
    private String img;
    private Map<String, Double> price;
    private Map<String, Map<String, Double>> date_price;
    @JsonProperty("default")
    private boolean defaults;
    private boolean express_delivery;
}
