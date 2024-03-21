package entity.deliveryDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class DeliveryTimeInterval {
    private double from;
    private double to;
    private boolean is_surhours;
}