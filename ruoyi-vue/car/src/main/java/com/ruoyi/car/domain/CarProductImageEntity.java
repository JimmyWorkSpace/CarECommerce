package com.ruoyi.car.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

/**
 * 商品圖片實體類
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@Data
@Table(name = "car_product_image")
public class CarProductImageEntity
{
    /** 主鍵ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主鍵ID'")
    private Long id;

    /** 產品ID */
    @Column(name = "productId", nullable = false, columnDefinition = "BIGINT(20) NOT NULL COMMENT '產品ID'")
    private Long productId;

    /** 圖片地址 */
    @Column(name = "imageUrl", nullable = false, columnDefinition = "VARCHAR(1000) NOT NULL COMMENT '圖片地址'")
    private String imageUrl;

    /** 顯示順序 */
    @Column(name = "showOrder", columnDefinition = "INT(11) DEFAULT NULL COMMENT '顯示順序'")
    private Integer showOrder;

    /** 刪除標記 */
    @Column(name = "delFlag", columnDefinition = "INT(11) DEFAULT NULL COMMENT '刪除標記'")
    private Integer delFlag;

    /** 完整圖片地址（臨時字段，不存儲到數據庫） */
    @Transient
    private String fullImageUrl;
}

