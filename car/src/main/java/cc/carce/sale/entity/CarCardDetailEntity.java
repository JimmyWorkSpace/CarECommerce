package cc.carce.sale.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 票券明細實體：關聯訂單，存儲每張票券的唯一碼及核銷狀態
 */
@Data
@Entity
@Table(name = "car_card_detail")
public class CarCardDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) COMMENT '主鍵ID'")
    private Long id;

    @Column(name = "orderId", nullable = false, columnDefinition = "BIGINT(20) COMMENT '訂單ID'")
    private Long orderId;

    @Column(name = "cardId", nullable = false, columnDefinition = "BIGINT(20) COMMENT '票券方案ID'")
    private Long cardId;

    @Column(name = "cardName", nullable = false, columnDefinition = "VARCHAR(100) COMMENT '票券名稱'")
    private String cardName;

    @Column(name = "ticketCode", nullable = false, columnDefinition = "VARCHAR(32) COMMENT '票券唯一碼'")
    private String ticketCode;

    @Column(name = "redeemed", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0 COMMENT '是否核銷 0未核銷 1已核銷'")
    private Integer redeemed = 0;

    @Column(name = "redeemedTime", columnDefinition = "DATETIME COMMENT '核銷時間'")
    private Date redeemedTime;

    @Column(name = "createTime", columnDefinition = "DATETIME COMMENT '建立時間'")
    private Date createTime;
}
