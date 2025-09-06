package cc.carce.sale.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 支付订单实体
 */
@Data
@Table(name = "car_payment_order")
public class CarPaymentOrderEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) COMMENT '主键ID'")
    private Long id;
    
    /**
     * 商户订单号
     */
    @Column(name = "merchant_trade_no", nullable = false, unique = true, 
            columnDefinition = "VARCHAR(50) COMMENT '商户订单号'")
    private String merchantTradeNo;
    
    /**
     * 绿界支付交易号
     */
    @Column(name = "ecpay_trade_no", columnDefinition = "VARCHAR(50) COMMENT '绿界支付交易号'")
    private String ecpayTradeNo;
    
    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false, columnDefinition = "BIGINT(20) COMMENT '用户ID'")
    private Long userId;
    
    /**
     * 订单金额
     */
    @Column(name = "total_amount", nullable = false, columnDefinition = "INT(11) COMMENT '订单金额'")
    private Integer totalAmount;
    
    /**
     * 商品名称
     */
    @Column(name = "item_name", columnDefinition = "VARCHAR(255) COMMENT '商品名称'")
    private String itemName;
    
    /**
     * 订单描述
     */
    @Column(name = "trade_desc", columnDefinition = "TEXT COMMENT '订单描述'")
    private String tradeDesc;
    
    /**
     * 支付方式
     */
    @Column(name = "payment_type", columnDefinition = "VARCHAR(50) COMMENT '支付方式'")
    private String paymentType;
    
    /**
     * 支付状态：0-待支付，1-支付成功，2-支付失败，3-已取消
     */
    @Column(name = "payment_status", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0 COMMENT '支付状态'")
    private Integer paymentStatus;
    
    /**
     * 支付时间
     */
    @Column(name = "payment_time", columnDefinition = "DATETIME COMMENT '支付时间'")
    private Date paymentTime;
    
    /**
     * 绿界支付状态
     */
    @Column(name = "ecpay_status", columnDefinition = "VARCHAR(50) COMMENT '绿界支付状态'")
    private String ecpayStatus;
    
    /**
     * 绿界支付状态描述
     */
    @Column(name = "ecpay_status_desc", columnDefinition = "VARCHAR(255) COMMENT '绿界支付状态描述'")
    private String ecpayStatusDesc;
    
    /**
     * 错误信息
     */
    @Column(name = "error_message", columnDefinition = "TEXT COMMENT '错误信息'")
    private String errorMessage;
    
    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    private Date createTime;
    
    /**
     * 更新时间
     */
    @Column(name = "update_time", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'")
    private Date updateTime;
    
    /**
     * 删除标记
     */
    @Column(name = "del_flag", columnDefinition = "BIT(1) DEFAULT b'0' COMMENT '删除标记'")
    private Boolean delFlag;
    
    /**
     * 支付状态枚举
     */
    public enum PaymentStatus {
        PENDING(0, "待支付"),
        SUCCESS(1, "支付成功"),
        FAILED(2, "支付失败"),
        CANCELLED(3, "已取消");
        
        private final Integer code;
        private final String description;
        
        PaymentStatus(Integer code, String description) {
            this.code = code;
            this.description = description;
        }
        
        public Integer getCode() {
            return code;
        }
        
        public String getDescription() {
            return description;
        }
        
        public static PaymentStatus fromCode(Integer code) {
            for (PaymentStatus status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("未知的支付状态码: " + code);
        }
    }
}
