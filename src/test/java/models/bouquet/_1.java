package models.bouquet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class _1 {
    @JsonProperty("RUB")
    public int rub;
    @JsonProperty("USD")
    public double usd;
    @JsonProperty("EUR")
    public double eur;
    @JsonProperty("KZT")
    public int kzt;
    public int id;
    public String name;
    public String slug;
    public boolean active;
    public String icon;
    public String type;
    public List list;
}
