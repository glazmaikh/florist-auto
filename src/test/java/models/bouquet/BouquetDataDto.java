package models.bouquet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class BouquetDataDto {
    public int ok;
    public String error;
    public Map<String, BouquetDataItemDto> data;
    public double t;
}
