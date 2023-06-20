package models.cityAlias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class Country {
    private int id;
    private String name;
    private String slug;
    private String declension;
    private String isoCode;
}
