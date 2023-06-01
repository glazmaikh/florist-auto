package config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:${env}.properties"
})
public interface WebConfig extends Config {
    @Key("base_url")
    String getBaseUrl();

    @Key("card_number")
    String getCurdNumber();

    @Key("expire_number")
    String getExpireNumber();

    @Key("cvc_number")
    String getCvcNumber();
}
