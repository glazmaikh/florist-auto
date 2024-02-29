package entity.deliveryDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class DeliveryInfo {
    private int ok;
    private String error;
    private DeliveryDateData data;
    private Map<String, Object> meta;
    private double t;
}
