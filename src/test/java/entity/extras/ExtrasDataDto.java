package entity.extras;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class ExtrasDataDto {
    private int ok;
    private String error;
    public Map<String, ExtrasDataItemDto> data;
    private Object meta;
    private double t;
}
