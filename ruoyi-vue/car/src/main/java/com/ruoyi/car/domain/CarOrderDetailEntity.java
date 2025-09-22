package com.ruoyi.car.domain;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单详情表实体
 */
@Data
@Entity
@Table(name = "car_order_detail")
public class CarOrderDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) COMMENT '主键ID'")
    private Long id;

    /**
     * 订单ID
     */
    @Column(name = "orderId", nullable = false, columnDefinition = "BIGINT(20) COMMENT '订单ID'")
    private Long orderId;

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
     * 产品ID
     */
    @Column(name = "productId", columnDefinition = "BIGINT(20) COMMENT '产品ID'")
    private Long productId;

    /**
     * 产品名称
     */
    @Column(name = "productName", columnDefinition = "VARCHAR(255) COMMENT '产品名称'")
    private String productName;

    /**
     * 产品数量
     */
    @Column(name = "productAmount", nullable = false, columnDefinition = "INT(11) COMMENT '产品数量'")
    private Integer productAmount;

    /**
     * 单价
     */
    @Column(name = "productPrice", columnDefinition = "INT(11) COMMENT '单价'")
    private Integer productPrice;

    /**
     * 总价。冗余，单价乘以数量
     */
    @Column(name = "totalPrice", columnDefinition = "INT(11) COMMENT '总价。冗余，单价乘以数量'")
    private Integer totalPrice;
}