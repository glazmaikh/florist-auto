package models.city;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class City {
    private String name;
    private String declension_name;
    private int id;
}
