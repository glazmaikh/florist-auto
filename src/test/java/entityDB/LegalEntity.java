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
    private Long account_id;

    @Column(name = "legalname")
    private String legalname;

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
    private String head_fullname;

    @Column(name = "head_position")
    private String head_position;

    @Column(name = "document_title")
    private String document_title;

    @Column(name = "legal_address")
    private String legal_address;

    @Column(name = "real_address")
    private String real_address;

    @Column(name = "post_address")
    private String post_address;

    @Column(name = "created_at")
    private String created_at;
}
