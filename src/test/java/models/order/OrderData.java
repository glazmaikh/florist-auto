package models.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class OrderData {
    private int ok;
    private String error;
    private OrderDetails data;
    private Map<String, Object> meta;
    private double t;
}