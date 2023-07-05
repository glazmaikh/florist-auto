package models.toDelete.city;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class CityDataItemDto {
    public String id;
    public String name;
    public String alternative;
    public String slug;
    public Country country;
    public Region region;
    public Delivery delivery;
    public boolean express_delivery;
}
