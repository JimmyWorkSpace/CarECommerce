package cc.carce.sale.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 购物车DTO，包含车辆详细信息
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
     * 产品ID（车辆ID）
     */
    private Long productId;
    
    /**
     * 产品数量
     */
    private Integer productAmount;
    
    /**
     * 产品价格
     */
    private BigDecimal productPrice;
    
    /**
     * 产品名称
     */
    private String productName;
    
    /**
     * 车辆图片URL
     */
    private String productImage;
    
    /**
     * 车牌号
     */
    private String licensePlate;
    
    /**
     * 品牌名称
     */
    private String brandName;
    
    /**
     * 型号名称
     */
    private String modelName;
    
    /**
     * 出厂年份
     */
    private Integer manufactureYear;
    
    /**
     * 车色
     */
    private String color;
    
    /**
     * 里程数
     */
    private Integer mileage;
    
    /**
     * 变速系统
     */
    private String transmission;
    
    /**
     * 燃料系统
     */
    private String fuelSystem;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 小计金额
     */
    private BigDecimal subtotal;
    
    /**
     * 计算小计金额
     */
    public BigDecimal getSubtotal() {
        if (productPrice != null && productAmount != null) {
            return productPrice.multiply(new BigDecimal(productAmount));
        }
        return BigDecimal.ZERO;
    }
}
