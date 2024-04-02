package entityDB;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product")
@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class PartnerProductEntity {
    @Id
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "deleted_at")
    private String deletedAt;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "hidden")
    private short hidden;

    @Column(name = "tags")
    private String tags;

    @Column(name = "type")
    private Integer type;

    @Column(name = "color")
    private Integer color;
}