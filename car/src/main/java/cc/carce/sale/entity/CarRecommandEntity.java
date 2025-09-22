package cc.carce.sale.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "car_recommand")
public class CarRecommandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recommandType")
    private Integer recommandType;

    @Column(name = "recommandId")
    private Long recommandId;

    @Column(name = "showOrder")
    private Integer showOrder;

    @Column(name = "delFlag")
    private Integer delFlag;

    @Column(name = "createTime")
    private Date createTime;
}
