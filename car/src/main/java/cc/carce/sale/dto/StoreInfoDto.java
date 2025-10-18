package cc.carce.sale.dto;

import lombok.Data;

/**
 * 门店信息DTO
 * 对应绿界API返回的StoreInfo字段
 */
@Data
public class StoreInfoDto {
    
    /**
     * 门店代码
     */
    private Long StoreId;
    
    /**
     * 门店名称
     */
    private String StoreName;
    
    /**
     * 门店地址
     */
    private String StoreAddr;
    
    /**
     * 门店电话
     */
    private String StorePhone;
}
