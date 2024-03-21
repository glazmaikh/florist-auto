package entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class User {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String city;
    private boolean inform;
    private boolean push;
    private int discount;
    private String birthday;
    private String prefix;
    private boolean encryptedKey;
    private boolean sharedKey;
}