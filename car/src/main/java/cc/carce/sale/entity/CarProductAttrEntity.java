package cc.carce.sale.entity;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import tk.mybatis.mapper.annotation.KeySql;

/**
 * 商品属性表实体
 */
@Data
@Table(name = "car_product_attr")
public class CarProductAttrEntity {

    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id")
    private Long id;

    /**
     * 商品ID
     */
    @Column(name = "productId")
    private Long productId;

    /**
     * 属性名称
     */
    @Column(name = "attrName")
    private String attrName;

    /**
     * 属性值
     */
    @Column(name = "attrValue")
    private String attrValue;

    /**
     * 显示顺序
     */
    @Column(name = "showOrder")
    private Integer showOrder;

    /**
     * 删除标记 0 否 / 1 是
     */
    @Column(name = "delFlag")
    private Integer delFlag;
}


