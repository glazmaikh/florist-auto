package entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class AuthDto {
    private int ok;
    private String error;
    private User user;
    private Object meta;
    private double t;
}
