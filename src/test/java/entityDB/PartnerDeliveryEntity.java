package entityDB;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "partner_delivery")
@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class PartnerDeliveryEntity {
    @Id
    private Long id;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "location_type")
    private Integer locationType;

    @Column(name = "name")
    private String name;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "currency")
    private String currency;

    @Column(name = "time")
    private Integer time;

    @Column(name = "hidden")
    private int hidden;
}