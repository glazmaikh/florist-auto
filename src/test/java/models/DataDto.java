package models;

import lombok.Data;

import java.util.Map;

public @Data class DataDto {
    public int ok;
    public String error;
    public String meta;
    public double t;
    public Map<String, DataItemDto> data;
}
