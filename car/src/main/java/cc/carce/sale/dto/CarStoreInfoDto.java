package cc.carce.sale.dto;

import cc.carce.sale.entity.CarStoreInfoEntity;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarStoreInfoDto {
    
    /**
     * 便利店类型
     */
    private String cvsType;

    private String csvName;
    public String getCsvName() {
        if(StrUtil.isBlank(cvsType)){
            return "未知";
        }
        switch (cvsType){
            case "FAMI":
                return "全家";
            case "UNIMART":
                return "7-ELEVEN超商(常溫)";
            case "HILIFE":
                return "萊爾富";
            case "OKMART":
                return "OK超商";
            case "UNIMARTFREEZE":
                return "7-ELEVEN超商(冷鏈)";
            default:
                return "未知";
        }
    }
    
    /**
     * 门店ID
     */
    private Long storeId;
    
    /**
     * 门店名称
     */
    private String storeName;
    
    /**
     * 门店地址
     */
    private String storeAddr;
    
    /**
     * 门店电话
     */
    private String storePhone;

    public CarStoreInfoDto(CarStoreInfoEntity entity) {
        this.cvsType = entity.getCvsType();
        this.storeId = entity.getStoreId();
        this.storeName = entity.getStoreName();
        this.storeAddr = entity.getStoreAddr();
        this.storePhone = entity.getStorePhone();
    }
}
