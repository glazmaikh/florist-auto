package entityDB;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "partner_legal")
@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class LegalEntity {
    @Id
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "legalname")
    private String legalName;

    @Column(name = "inn")
    private String inn;

    @Column(name = "kpp")
    private String kpp;

    @Column(name = "okpo")
    private String okpo;

    @Column(name = "ogrn")
    private String ogrn;

    @Column(name = "egrip")
    private String egrip;

    @Column(name = "head_fullname")
    private String headFullName;

    @Column(name = "head_position")
    private String headPosition;

    @Column(name = "document_title")
    private String documentTitle;

    @Column(name = "legal_address")
    private String legalAddress;

    @Column(name = "real_address")
    private String realAddress;

    @Column(name = "post_address")
    private String postAddress;

    @Column(name = "created_at")
    private String createdAt;
}
