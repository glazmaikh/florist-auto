package models.disabledDelivery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class DisabledDeliveryDateResponse {
    private int ok;
    private String error;
    private models.disabledDelivery.Data data;
    private Map<String, String> meta;
    private double t;
}
