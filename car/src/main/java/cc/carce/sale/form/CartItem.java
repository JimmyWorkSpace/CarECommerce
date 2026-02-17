package cc.carce.sale.form;

import lombok.Data;

/**
 * 购物车项
 */
@Data
public class CartItem {
    
    /**
     * 购物车项ID
     */
    private Long id;
    
    /**
     * 产品ID
     */
    private Long productId;
    
    /**
     * 产品名称
     */
    private String productName;
    
    /**
     * 产品数量
     */
    private Integer productAmount;
    
    /**
     * 产品价格
     */
    private Double productPrice;

    /**
     * 价格版本ID（关联 car_product_price.id）
     */
    private Long priceId;
    
    /**
     * 小计金额
     */
    private Double subtotal;
}
