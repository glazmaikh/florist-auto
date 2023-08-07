package config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:base.properties"
})
public interface BaseConfig extends Config {
    @Key("base_url")
    String getBaseUrl();

    @Key("card_number")
    String getCardNumber();

    @Key("expire_number")
    String getExpireNumber();

    @Key("cvc_number")
    String getCvcNumber();

    @Key("promo")
    String getPromo();
}
