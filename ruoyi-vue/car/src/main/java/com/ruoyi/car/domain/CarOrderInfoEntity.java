package com.ruoyi.car.domain;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 訂單主表实体
 */
@Data
@Entity
@Table(name = "car_order_info")
public class CarOrderInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) COMMENT '主键ID'")
    private Long id;

    /**
     * 訂單號
     */
    @Column(name = "orderNo", nullable = false, columnDefinition = "VARCHAR(50) COMMENT '訂單號'")
    private String orderNo;

    /**
     * 用户ID
     */
    @Column(name = "userId", nullable = false, columnDefinition = "BIGINT(20) COMMENT '用户ID'")
    private Long userId;

    /**
     * 總價格
     */
    @Column(name = "totalPrice", columnDefinition = "INT(11) COMMENT '總價格'")
    private Integer totalPrice;

    /**
     * 刪除标记 1 是 0 否
     */
    @Column(name = "delFlag", columnDefinition = "BIT(1) DEFAULT b'0' COMMENT '刪除标记 1 是 0 否'")
    private Boolean delFlag;

    /**
     * 建立時間
     */
    @Column(name = "createTime", columnDefinition = "DATETIME COMMENT '建立時間'")
    private Date createTime;

    /**
     * 显示顺序
     */
    @Column(name = "showOrder", columnDefinition = "INT(11) COMMENT '显示顺序'")
    private Integer showOrder;

    /**
     * 訂單狀態：0 未支付 1 支付中 2 已支付 3 已取消 4 支付失败 5 已发货 6 已完成 7 退货中 8 已退货
     */
    @Column(name = "orderStatus", columnDefinition = "INT(11) COMMENT '0 未支付 1 支付中 2 已支付 3 已取消 4 支付失败 5 已发货 6 已完成 7 退货中 8 已退货'")
    private Integer orderStatus;

    /**
     * 收件人地址
     */
    @Column(name = "receiverAddress", columnDefinition = "VARCHAR(1000) COMMENT '收件人地址'")
    private String receiverAddress;

    /**
     * 收件人
     */
    @Column(name = "receiverName", columnDefinition = "VARCHAR(255) COMMENT '收件人'")
    private String receiverName;

    /**
     * 收件人電話
     */
    @Column(name = "receiverMobile", columnDefinition = "VARCHAR(50) COMMENT '收件人電話'")
    private String receiverMobile;

    /**
     * 物流單號
     */
    @Column(name = "logicNumber", columnDefinition = "VARCHAR(100) COMMENT '物流單號'")
    private String logicNumber;

    /**
     * 價格版本ID（所選商品價格版本，對應 car_product_price.id）
     */
    @Column(name = "priceId", columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT '價格版本'")
    private Long priceId;

    /**
     * 訂單類型 1 宅配到府 2 超商取貨；另作價格類型時：1 普通商品訂單 2 卡券訂單，默認 1
     */
    @Column(name = "orderType", columnDefinition = "INT(11) DEFAULT 1 COMMENT '訂單類型 1 宅配到府 2 超商取貨；價格類型 1 普通商品訂單 2 卡券訂單'")
    private Integer orderType;

    /**
     * 超商店舖編號
     */
    @Column(name = "CVSStoreID", columnDefinition = "VARCHAR(50) COMMENT '超商店舖編號'")
    private String cvsStoreID;

    /**
     * 超商店舖名稱
     */
    @Column(name = "CVSStoreName", columnDefinition = "VARCHAR(100) COMMENT '超商店舖名稱'")
    private String cvsStoreName;

    /**
     * 超商店舖地址
     */
    @Column(name = "CVSAddress", columnDefinition = "VARCHAR(255) COMMENT '超商店舖地址'")
    private String cvsAddress;

    /**
     * 超商店舖電話
     */
    @Column(name = "CVSTelephone", columnDefinition = "VARCHAR(50) COMMENT '超商店舖電話'")
    private String cvsTelephone;

    /**
     * 使用者選擇的超商店舖是否為離島店鋪.0：本島,1：離島
     */
    @Column(name = "CVSOutSide", columnDefinition = "INT(11) COMMENT '使用者選擇的超商店舖是否為離島店鋪.0：本島,1：離島'")
    private Integer cvsOutSide;

    /**
     * 訂單類型枚举
     */
    public enum OrderType {
        HOME_DELIVERY(1, "宅配到府"),
        CONVENIENCE_STORE(2, "超商取貨");

        private final Integer code;
        private final String description;

        OrderType(Integer code, String description) {
            this.code = code;
            this.description = description;
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static OrderType fromCode(Integer code) {
            for (OrderType type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("未知的訂單類型码: " + code);
        }
    }

    /**
     * 訂單狀態枚举
     */
    public enum OrderStatus {
        UNPAID(0, "未支付"),
        PAYING(1, "支付中"),
        PAID(2, "已支付"),
        CANCELLED(3, "已取消"),
        PAYMENT_FAILED(4, "支付失败"),
        SHIPPED(5, "已发货"),
        COMPLETED(6, "已完成"),
        RETURNING(7, "退货中"),
        RETURNED(8, "已退货");

        private final Integer code;
        private final String description;

        OrderStatus(Integer code, String description) {
            this.code = code;
            this.description = description;
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static OrderStatus fromCode(Integer code) {
            for (OrderStatus status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("未知的訂單狀態码: " + code);
        }
    }
}