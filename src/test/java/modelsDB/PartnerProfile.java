package modelsDB;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "partner_profile")
public class PartnerProfile {
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

    public Long getId() {
        return id;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public String getPhone() {
        return phone;
    }

    public String getUr_name() {
        return ur_name;
    }

    public Long getAccepted_oferta() {
        return accepted_oferta;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getPartnerOfertaDate() {
        return partnerOfertaDate;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getCurrency() {
        return currency;
    }

    public String getLanguage() {
        return language;
    }

    public Long getStatus() {
        return status;
    }

    public Long getPartner_level() {
        return partner_level;
    }

    public Long getAgency_fee() {
        return agency_fee;
    }

    public String getComment() {
        return comment;
    }

    public Long getCountry() {
        return country;
    }

    public Long getWorldCountry() {
        return worldCountry;
    }

    public Long getLocation_id() {
        return location_id;
    }
}
