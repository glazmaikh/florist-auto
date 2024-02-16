package models.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class DeliveryData {
    private int cityId;
    private int countryId;
    private int worldCountryId;
    private String deliveryDate;
}

