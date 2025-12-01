package cc.carce.sale.entity;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import tk.mybatis.mapper.annotation.KeySql;

/**
 * 商品图片表实体
 */
@Data
@Table(name = "car_product_image")
public class CarProductImageEntity {

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
     * 图片URL
     */
    @Column(name = "imageUrl")
    private String imageUrl;

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


