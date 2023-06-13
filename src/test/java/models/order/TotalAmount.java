package models.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class TotalAmount {
    private double RUB;
    private double USD;
    private double EUR;
    private double KZT;
}
