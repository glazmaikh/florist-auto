package models.deliveryDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class DeliveryTime {
    private double from;
    private double to;
}
