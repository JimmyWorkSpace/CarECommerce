package com.ruoyi.car.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

/**
 * 商品實體類
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@Data
@Table(name = "car_product")
public class CarProductEntity
{
    /** 主鍵ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主鍵ID'")
    private Long id;

    /** 標題 */
    @Column(name = "productTitle", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL COMMENT '標題'")
    private String productTitle;

    /** 簡要介紹 */
    @Column(name = "productDespShort", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '簡要介紹'")
    private String productDespShort;

    /** 詳細介紹-富文本 */
    @Column(name = "productDesp", columnDefinition = "LONGTEXT COMMENT '詳細介紹-富文本'")
    private String productDesp;

    /** 標籤 */
    @Column(name = "productTags", columnDefinition = "VARCHAR(1000) DEFAULT NULL COMMENT '標籤'")
    private String productTags;

    /** 供價 */
    @Column(name = "supplyPrice", columnDefinition = "DECIMAL(10,2) DEFAULT NULL COMMENT '供價'")
    private BigDecimal supplyPrice;

    /** 售價 */
    @Column(name = "salePrice", columnDefinition = "DECIMAL(10,2) DEFAULT NULL COMMENT '售價'")
    private BigDecimal salePrice;

    /** 剩餘數量 */
    @Column(name = "amount", nullable = false, columnDefinition = "INT(11) NOT NULL DEFAULT '0' COMMENT '剩餘數量'")
    private Integer amount;

    /** 分類ID */
    @Column(name = "categoryId", columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT '分類ID'")
    private Long categoryId;

    /** 是否上架 1 是 0 否 */
    @Column(name = "onSale", nullable = false, columnDefinition = "INT(11) NOT NULL DEFAULT '0' COMMENT '是否上架 1 是 0 否'")
    private Integer onSale;

    /** 刪除標記 1 已刪 0 未刪 */
    @Column(name = "delFlag", nullable = false, columnDefinition = "INT(11) NOT NULL DEFAULT '0' COMMENT '刪除標記 1 已刪 0 未刪'")
    private Integer delFlag;

    /** 商品圖片列表（臨時字段） */
    @Transient
    private List<CarProductImageEntity> images;

    /** 商品屬性列表（臨時字段） */
    @Transient
    private List<CarProductAttrEntity> attrs;
}

