package models.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class CartItem {
    private String item_id;
    private String type;
    private String name;
    private Price price;
}
