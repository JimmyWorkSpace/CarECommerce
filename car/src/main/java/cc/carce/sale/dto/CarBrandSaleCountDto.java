package cc.carce.sale.dto;

import lombok.Data;

@Data
public class CarBrandSaleCountDto {
    
    /**
     * 品牌名称
     */
    private String brand;
    
    /**
     * 品牌ID
     */
    private Long id;
    
    /**
     * 在售数量
     */
    private Long saleCount;
}
