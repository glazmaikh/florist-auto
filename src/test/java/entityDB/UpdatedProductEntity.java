package entityDB;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ext_log_entries")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatedProductEntity {
    @Id
    private Long id;

    @Column(name = "action")
    private String action;

    @Column(name = "logged_at")
    private String logged_at;

    @Column(name = "object_id")
    private Long objectId;

    @Column(name = "object_class")
    private String objectClass;

    @Column(name = "version")
    private Long version;

    @Column(name = "data")
    private String data;
}
