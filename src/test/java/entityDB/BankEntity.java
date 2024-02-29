package entityDB;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "partner_bank")
@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class BankEntity {
    @Id
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_address")
    private String bankAddress;

    @Column(name = "bik")
    private String bik;

    @Column(name = "rs")
    private String rs;

    @Column(name = "ks")
    private String ks;

    @Column(name = "created_at")
    private String createdAt;
}