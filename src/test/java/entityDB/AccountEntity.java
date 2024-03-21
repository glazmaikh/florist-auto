package entityDB;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class AccountEntity {
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "c_phones")
    private String phones;

    @Column(name = "c_email")
    private String email;

    @Column(name = "partner_login")
    private String partnerLogin;

    @Column(name = "use_productsstorage")
    private int useProductsstorage;
}