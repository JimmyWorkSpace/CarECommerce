package cc.carce.sale.entity;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import tk.mybatis.mapper.annotation.KeySql;

/**
 * 商品分类表实体
 */
@Data
@Table(name = "car_product_category")
public class CarProductCategoryEntity {

    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id")
    private Long id;

    /**
     * 分类名称
     */
    @Column(name = "categoryName")
    private String categoryName;

    /**
     * 父分类ID
     */
    @Column(name = "parentId")
    private Long parentId;

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


