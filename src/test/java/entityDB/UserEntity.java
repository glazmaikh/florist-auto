package entityDB;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "partner_profile")
@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class UserEntity {
    @Id
    private Long id;

    @Column(name = "account_id")
    private Long account_id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "ur_name")
    private String ur_name;

    @Column(name = "accepted_oferta")
    private Long accepted_oferta;

    @Column(name = "created_at")
    private String created_at;

    @Column(name = "updated_at")
    private String updated_at;

    @Column(name = "partnerOfertaDate")
    private String partnerOfertaDate;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "currency")
    private String currency;

    @Column(name = "language")
    private String language;

    @Column(name = "status")
    private Long status;

    @Column(name = "partner_level")
    private Long partner_level;

    @Column(name = "agency_fee")
    private Long agency_fee;

    @Column(name = "comment")
    private String comment;

    @Column(name = "country")
    private Long country;

    @Column(name = "worldCountry")
    private Long worldCountry;

    @Column(name = "location_id")
    private Long location_id;
}
