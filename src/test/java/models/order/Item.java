package models.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class Item {
    private String name;
    private String variation;
    private int count;
    private String type;
    private boolean is_extras;
    private TotalAmount price;
    private String id;
    private int item_id;
    private int price_id;
    private String url;
    private String img;
}
