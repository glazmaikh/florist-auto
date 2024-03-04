package entityDB;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;


@Entity
@Table(name = "partner_worktime")
@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class WorkTimeEntity {
    @Id
    private Long id;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "from_time")
    private BigDecimal fromTime;

    @Column(name = "to_time")
    private BigDecimal toTime;

    @Column(name = "workday")
    private int workday;

    @Column(name = "weekday")
    private int weekday;

    @Column(name = "around_the_clock")
    private int aroundTheClock;

    @Column(name = "test_to")
    private BigDecimal testTo;
}
