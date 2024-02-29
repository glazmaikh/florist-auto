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
    private Long accountId;

    @Column(name = "phone")
    private String phone;

    @Column(name = "ur_name")
    private String urName;

    @Column(name = "accepted_oferta")
    private Long acceptedOferta;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

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
    private Long partnerLevel;

    @Column(name = "agency_fee")
    private Long agencyFee;

    @Column(name = "comment")
    private String comment;

    @Column(name = "country")
    private Long country;

    @Column(name = "worldCountry")
    private Long worldCountry;

    @Column(name = "location_id")
    private Long locationId;
}
