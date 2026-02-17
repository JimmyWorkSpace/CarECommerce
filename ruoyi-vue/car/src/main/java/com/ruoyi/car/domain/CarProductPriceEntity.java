package com.ruoyi.car.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * 商品價格版本實體（一商品多價格，如按顏色：黑色100元、白色120元）
 *
 * @author ruoyi
 */
@Data
@Table(name = "car_product_price")
public class CarProductPriceEntity {

    /** 主鍵ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主鍵ID'")
    private Long id;

    /** 產品ID */
    @Column(name = "productId", columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT '產品Id'")
    private Long productId;

    /** 價格版本名稱（如：黑色、白色） */
    @Column(name = "versionName", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '價格版本名稱'")
    private String versionName;

    /** 供價 */
    @Column(name = "supplyPrice", columnDefinition = "DECIMAL(10,2) DEFAULT NULL COMMENT '供價'")
    private BigDecimal supplyPrice;

    /** 售價 */
    @Column(name = "salePrice", columnDefinition = "DECIMAL(10,2) DEFAULT NULL COMMENT '售價'")
    private BigDecimal salePrice;

    /** 剩餘數量 */
    @Column(name = "amount", columnDefinition = "INT(11) NOT NULL DEFAULT '0' COMMENT '剩餘數量'")
    private Integer amount;

    /** 是否上架 1 是 0 否 */
    @Column(name = "onSale", columnDefinition = "INT(11) NOT NULL DEFAULT '0' COMMENT '是否上架 1 是 0 否'")
    private Integer onSale;

    /** 刪除標記 1 已刪 0 未刪 */
    @Column(name = "delFlag", columnDefinition = "INT(11) NOT NULL DEFAULT '0' COMMENT '刪除標記 1 已刪 0 未刪'")
    private Integer delFlag;

    /** 特惠價 */
    @Column(name = "promotionalPrice", columnDefinition = "DECIMAL(10,2) DEFAULT NULL COMMENT '特惠價'")
    private BigDecimal promotionalPrice;
}
