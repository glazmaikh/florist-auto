package models.cityAlias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class ResponseData {
    private int ok;
    private String error;
    private models.cityAlias.Data data;
    private Object meta;
    private double t;
}
