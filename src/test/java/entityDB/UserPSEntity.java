package entityDB;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user")
@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class UserPSEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "roles")
    private String roles;

    @Column(name = "iflorist_user_id")
    private int ifloristUserId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "deleted_at")
    private String deletedAt;

    @Column(name = "telegram_user_id")
    private Long telegramUserId;

    @Column(name = "hidden")
    private short hidden;
}
