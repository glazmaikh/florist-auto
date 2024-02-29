package entity.extras;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class ExtrasDataItemDto {
    private int collection_id;
    private String description;
    private boolean express_delivery;
    private Map<String, Integer> groups;
    private String id;
    private String img;
    private int is_action;
    //private MinPrice min_price;
    private Map<String, Double> min_price;
    private String name;
    private String preview;
    private Map<String, ExtrasPriceItemDto> prices;
}