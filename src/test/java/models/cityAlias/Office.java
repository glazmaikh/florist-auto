package models.cityAlias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class Office {
    private String phone;
    private String workingFrom;
    private String workingTo;
    private String address;
}