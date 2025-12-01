package com.ruoyi.car.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * 商品目錄分類實體類
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@Data
@Table(name = "car_product_category")
public class CarProductCategoryEntity
{
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;

    /** 主鍵ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主鍵ID'")
    private Long id;

    /** 分類名稱 */
    @Column(name = "categoryName", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL COMMENT '分類名稱'")
    private String categoryName;

    /** 父級ID */
    @Column(name = "parentId", columnDefinition = "BIGINT(20) DEFAULT NULL COMMENT '父級ID'")
    private Long parentId;

    /** 顯示順序 */
    @Column(name = "showOrder", columnDefinition = "INT(11) DEFAULT NULL COMMENT '顯示順序'")
    private Integer showOrder;

    /** 刪除標記 */
    @Column(name = "delFlag", columnDefinition = "INT(11) DEFAULT NULL COMMENT '刪除標記'")
    private Integer delFlag;

    /** 父分類名稱 */
    @Transient
    private String parentName;

    /** 子分類 */
    @Transient
    private List<CarProductCategoryEntity> children = new ArrayList<CarProductCategoryEntity>();
}

