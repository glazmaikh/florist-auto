package models.deliveryDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class DeliveryDateData {
    private boolean delivery_time_enabled;
    private boolean delivery_call_enabled;
    private boolean delivery_call_required;
    private Map<String, DeliveryTimeInterval> delivery_time_intervals;
    private int min_gap;
    private Map<String, Double> surhours_price;
    private DeliveryTime delivery_time;
    private DeliveryTime delivery_surtime;
}