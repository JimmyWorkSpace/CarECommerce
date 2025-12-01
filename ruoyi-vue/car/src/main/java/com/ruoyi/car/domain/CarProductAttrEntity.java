package com.ruoyi.car.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * 商品屬性實體類
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@Data
@Table(name = "car_product_attr")
public class CarProductAttrEntity
{
    /** 主鍵ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主鍵ID'")
    private Long id;

    /** 商品ID */
    @Column(name = "productId", nullable = false, columnDefinition = "BIGINT(20) NOT NULL COMMENT '商品ID'")
    private Long productId;

    /** 屬性名 */
    @Column(name = "attrName", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '屬性名'")
    private String attrName;

    /** 屬性值 */
    @Column(name = "attrValue", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '屬性值'")
    private String attrValue;

    /** 顯示順序 */
    @Column(name = "showOrder", columnDefinition = "INT(11) DEFAULT NULL COMMENT '顯示順序'")
    private Integer showOrder;

    /** 刪除標記 */
    @Column(name = "delFlag", columnDefinition = "INT(11) DEFAULT NULL COMMENT '刪除標記'")
    private Integer delFlag;
}

