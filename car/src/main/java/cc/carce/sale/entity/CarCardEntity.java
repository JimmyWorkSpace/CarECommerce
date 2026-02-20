package cc.carce.sale.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票券方案實體（用於讀取 car_card 表，下單與產生明細時使用）
 */
@Data
@Entity
@Table(name = "car_card")
public class CarCardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) COMMENT '主鍵ID'")
    private Long id;

    @Column(name = "cardName", nullable = false, columnDefinition = "VARCHAR(100) COMMENT '票券名稱'")
    private String cardName;

    @Column(name = "salePrice", nullable = false, columnDefinition = "DECIMAL(10,2) COMMENT '售價'")
    private BigDecimal salePrice;

    @Column(name = "usageInstruction", columnDefinition = "TEXT COMMENT '使用說明'")
    private String usageInstruction;

    @Column(name = "validityType", columnDefinition = "TINYINT(1) DEFAULT 1 COMMENT '有效期限類型 1指定日期 2指定天數'")
    private Integer validityType;

    @Column(name = "validityEndDate", columnDefinition = "DATE COMMENT '有效截止日'")
    private Date validityEndDate;

    @Column(name = "validityDays", columnDefinition = "INT(11) COMMENT '有效天數'")
    private Integer validityDays;

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1 COMMENT '啟用狀態 1啟用 0停用'")
    private Integer status;

    @Column(name = "createTime", columnDefinition = "DATETIME COMMENT '建立時間'")
    private Date createTime;
}
