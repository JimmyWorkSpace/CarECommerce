package cc.carce.sale.entity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "reservation")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String mobile;

    @Column(nullable = false)
    private String dateTime;  // if you prefer java.util.Date, change to Date

    @Column(nullable = false)
    private String location;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();
}
