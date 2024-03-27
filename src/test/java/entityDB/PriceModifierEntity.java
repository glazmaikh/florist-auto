package entityDB;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "partner_price_modifier")
@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class PriceModifierEntity {
    @Id
    private Long id;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "price_modifier")
    private Integer priceModifier;
}
