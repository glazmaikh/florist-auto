package config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:stage.properties"
})
public interface BaseConfig extends Config {
    @Key("base.url")
    String getBaseUrl();

    @Key("card.number")
    String getCardNumber();

    @Key("expire.number")
    String getExpireNumber();

    @Key("cvc.number")
    String getCvcNumber();

    @Key("promo.code")
    String getPromoCode();
}
