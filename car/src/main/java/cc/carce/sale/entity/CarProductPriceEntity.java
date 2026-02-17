package cc.carce.sale.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import tk.mybatis.mapper.annotation.KeySql;

/**
 * 商品价格版本实体（一商品多价格，如按颜色：黑色100元、白色120元）
 */
@Data
@Table(name = "car_product_price")
public class CarProductPriceEntity {

    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id")
    private Long id;

    /**
     * 产品ID
     */
    @Column(name = "productId")
    private Long productId;

    /**
     * 价格版本名称（如：黑色、白色）
     */
    @Column(name = "versionName")
    private String versionName;

    /**
     * 供价
     */
    @Column(name = "supplyPrice")
    private BigDecimal supplyPrice;

    /**
     * 售价
     */
    @Column(name = "salePrice")
    private BigDecimal salePrice;

    /**
     * 剩余数量
     */
    @Column(name = "amount")
    private Integer amount;

    /**
     * 是否上架 1 是 0 否
     */
    @Column(name = "onSale")
    private Integer onSale;

    /**
     * 删除标记 1 已删 0 未删
     */
    @Column(name = "delFlag")
    private Integer delFlag;

    /**
     * 特惠价
     */
    @Column(name = "promotionalPrice")
    private BigDecimal promotionalPrice;
}
