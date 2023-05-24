package models.bouquet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class MinDatePrice {
    @JsonProperty("1")
    public _1 _1;
    @JsonProperty("2")
    public _2 _2;
}
