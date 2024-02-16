package modelsDB;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "user")
public class User {
    @Id
    private Long id;
    @Column(name = "supplier_id")
    private Long supplier_id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "roles")
    private String roles;
    @Column(name = "iflorist_user_id")
    private Long iflorist_user_id;
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @Column(name = "created_at")
    private String created_at;
    @Column(name = "updated_at")
    private String updated_at;
    @Column(name = "deleted_at")
    private String deleted_at;
    @Column(name = "telegram_user_id")
    private Long telegram_user_id;

    public Long getId() {
        return id;
    }

    public Long getSupplier_id() {
        return supplier_id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRoles() {
        return roles;
    }

    public Long getIflorist_user_id() {
        return iflorist_user_id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public Long getTelegram_user_id() {
        return telegram_user_id;
    }
}