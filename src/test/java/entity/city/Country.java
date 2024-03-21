package entity.city;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class Country {
    private int id;
    private String name;
    private boolean foreign;
    private String isoCode;
}
