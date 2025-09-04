package cc.carce.sale.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单主表实体
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
     * 订单号
     */
    @Column(name = "orderNo", nullable = false, columnDefinition = "VARCHAR(50) COMMENT '订单号'")
    private String orderNo;

    /**
     * 用户ID
     */
    @Column(name = "userId", nullable = false, columnDefinition = "BIGINT(20) COMMENT '用户ID'")
    private Long userId;

    /**
     * 总价格
     */
    @Column(name = "totalPrice", columnDefinition = "DECIMAL(10,2) COMMENT '总价格'")
    private BigDecimal totalPrice;

    /**
     * 删除标记 1 是 0 否
     */
    @Column(name = "delFlag", columnDefinition = "BIT(1) DEFAULT b'0' COMMENT '删除标记 1 是 0 否'")
    private Boolean delFlag;

    /**
     * 创建时间
     */
    @Column(name = "createTime", columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;

    /**
     * 显示顺序
     */
    @Column(name = "showOrder", columnDefinition = "INT(11) COMMENT '显示顺序'")
    private Integer showOrder;

    /**
     * 订单状态：0 未支付 1 支付中 2 已支付 3 已取消 4 支付失败 5 已发货 6 已完成 7 退货中 8 已退货
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
     * 收件人电话
     */
    @Column(name = "receiverMobile", columnDefinition = "VARCHAR(50) COMMENT '收件人电话'")
    private String receiverMobile;

    /**
     * 物流单号
     */
    @Column(name = "logicNumber", columnDefinition = "VARCHAR(100) COMMENT '物流单号'")
    private String logicNumber;

    /**
     * 订单状态枚举
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
            throw new IllegalArgumentException("未知的订单状态码: " + code);
        }
    }
}