package models.disabledDelivery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @lombok.Data class Data {
    private Map<String, String> disabled_dates;
}
