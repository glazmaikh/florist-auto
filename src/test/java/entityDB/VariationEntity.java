package entityDB;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "variation")
@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class VariationEntity {
    @Id
    private Long id;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "title")
    private String title;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "price")
    private String price;

    @Column(name = "currency")
    private String currency;

    @Column(name = "hidden")
    private short hidden;

    @Column(name = "images")
    private String images;

    @Column(name = "compositions")
    private String compositions;

    @Column(name = "dimensions")
    private String dimensions;

    @Column(name = "isDefault")
    private boolean isDefault;

    @Column(name = "formationTime")
    private Integer formationTime;
}
