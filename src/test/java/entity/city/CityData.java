package entity.city;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class CityData {
    private String short_name;
    private String name;
    private int geoid;
    private String domain;
    private String slug;
    private String url;
    private String dlrem;
    private Geo geo;
}
