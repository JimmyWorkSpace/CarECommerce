package cc.carce.sale.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 短信发送记录实体类
 */
@Data
@Entity
@Table(name = "car_sms_log")
public class CarSmsLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) COMMENT '主键ID'")
    private Long id;

    /**
     * 手机号码
     */
    @Column(name = "phoneNumber", columnDefinition = "VARCHAR(50) COMMENT '手机号码'")
    private String phoneNumber;

    /**
     * 短信内容
     */
    @Column(name = "smsContent", columnDefinition = "VARCHAR(1000) COMMENT '短信内容'")
    private String smsContent;

    /**
     * 发送时间
     */
    @Column(name = "sendTime", columnDefinition = "DATETIME COMMENT '发送时间'")
    private Date sendTime;
}
