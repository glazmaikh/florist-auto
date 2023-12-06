package models.bouquet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class BouquetDataItemDto {
    public int id;
    public int account_id;
    public int collection_id;
    public int canonical_city_id;
    public String canonical_city_slug;
    public boolean iflorist;
    public int salon_id;
    public MinPrice min_price;
    public String name;
    public String subDescription;
    public boolean show_sales_hit;
    public String sales_hit_color;
    public String sales_hit_text;
    public boolean is_action;
    public int old_price_converted;
    public Object stamp;
    public boolean express_delivery;
    public String img;
    public String description;
    private Map<String, PriceItemDto> prices;
    @JsonProperty("min_date_price")
    public Map<String, Map<String, Double>> minDatePrice;
    public String salon_name;

//    public List<PriceItemDto> getPrices() {
//        return prices;
//    }
}
