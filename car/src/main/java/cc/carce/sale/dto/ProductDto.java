package cc.carce.sale.dto;

import java.util.Date;
import lombok.Data;

/**
 * 商品详情DTO，用于前端展示
 */
@Data
public class ProductDto {
    
    /**
     * 商品ID
     */
    private Long id;
    
    /**
     * 商品名称
     */
    private String name;
    
    /**
     * 商品别名/简短描述
     */
    private String alias;
    
    /**
     * 型号
     */
    private String model;
    
    /**
     * 售价
     */
    private Long price;
    
    /**
     * 市价
     */
    private Long marketPrice;
    
    /**
     * 品牌
     */
    private String brand;
    
    /**
     * 商品标签
     */
    private String tag;
    
    /**
     * 来源
     */
    private String source;
    
    /**
     * 商品详细描述
     */
    private String memo;
    
    /**
     * 创建时间
     */
    private Date cDt;
    
    /**
     * 图片
     */
    private String image;
    
    /**
     * 分类名称
     */
    private String category;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 商品标签（与tag并存）
     */
    private String productTags;
    
    /**
     * 特惠价
     */
    private Long promotionalPrice;
    
    /**
     * 原价（salePrice），用于显示划线价格
     */
    private Long originalPrice;
    
    /**
     * 库存数量
     */
    private Integer amount;
    
    /**
     * 是否上架
     */
    private Integer onSale;
}
