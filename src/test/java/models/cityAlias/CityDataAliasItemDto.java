package models.cityAlias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @lombok.Data class CityDataAliasItemDto {
    private int id;
    private String slug;
    private String name;
    private String fullName;
    private String declension;
    private Country country;
    private Region region;
    private Office office;
    private Map<String, Double> delivery;
    private Map<String, Double> surcharge_delivery;
    private DeliveryTime delivery_time;
    private DeliveryTime surcharge_delivery_time;
    private String timezone;
    @JsonProperty("nofollow_noindex_filter_level")
    private int noFollowNoIndexFilterLevel;
    @JsonProperty("nofollow")
    private int noFollow;
    @JsonProperty("noindex")
    private int noIndex;
}
