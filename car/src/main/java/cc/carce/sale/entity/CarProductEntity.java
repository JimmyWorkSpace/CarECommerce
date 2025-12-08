package cc.carce.sale.entity;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import tk.mybatis.mapper.annotation.KeySql;

/**
 * 商品表实体
 */
@Data
@Table(name = "car_product")
public class CarProductEntity {

    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id")
    private Long id;

    /**
     * 商品标题
     */
    @Column(name = "productTitle")
    private String productTitle;

    /**
     * 商品简短描述
     */
    @Column(name = "productDespShort")
    private String productDespShort;

    /**
     * 商品详细描述
     */
    @Column(name = "productDesp")
    private String productDesp;

    /**
     * 商品标签
     */
    @Column(name = "productTags")
    private String productTags;

    /**
     * 供应价格
     */
    @Column(name = "supplyPrice")
    private java.math.BigDecimal supplyPrice;

    /**
     * 销售价格
     */
    @Column(name = "salePrice")
    private java.math.BigDecimal salePrice;

    /**
     * 库存数量
     */
    @Column(name = "amount")
    private Integer amount;

    /**
     * 分类ID
     */
    @Column(name = "categoryId")
    private Long categoryId;

    /**
     * 是否上架 0 否 / 1 是
     */
    @Column(name = "onSale")
    private Integer onSale;

    /**
     * 删除标记 0 否 / 1 是
     */
    @Column(name = "delFlag")
    private Integer delFlag;

    /**
     * 特惠价
     */
    @Column(name = "promotionalPrice")
    private java.math.BigDecimal promotionalPrice;
}


