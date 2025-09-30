package cc.carce.sale.dto;

import java.util.Date;

import lombok.Data;

/**
 * 购物车DTO，包含商品详细信息
 */
@Data
public class CarShoppingCartDto {
    
    /**
     * 购物车项ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 产品ID（商品ID）
     */
    private Long productId;
    
    /**
     * 产品数量
     */
    private Integer productAmount;
    
    /**
     * 产品价格
     */
    private Long productPrice;
    
    /**
     * 产品名称
     */
    private String productName;
    
    /**
     * 商品图片URL
     */
    private String productImage;
    
    /**
     * 商品来源
     */
    private String source;
    
    /**
     * 商品别名
     */
    private String alias;
    
    /**
     * 商品型号
     */
    private String model;
    
    /**
     * 商品市价
     */
    private Long marketPrice;
    
    /**
     * 商品品牌
     */
    private String brand;
    
    /**
     * 商品类别标签
     */
    private String tag;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 计算小计金额
     */
    public Integer getSubtotal() {
        if (productPrice != null && productAmount != null) {
            return productPrice.intValue() * productAmount.intValue();
        }
        return 0;
    }
}
