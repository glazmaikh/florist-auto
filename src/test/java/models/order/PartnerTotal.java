package models.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class PartnerTotal {
    private int bouquets;
    private int extras;
    private int delivery;
}
