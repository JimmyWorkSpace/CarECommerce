package cc.carce.sale.dto;

import lombok.Data;
import java.util.List;

/**
 * 门店列表DTO
 * 对应绿界API返回的StoreList数组中的单个元素
 */
@Data
public class StoreListDto {
    
    /**
     * 超商类别
     * FAMI: 全家
     * UNIMART: 7-ELEVEN超商(常温)
     * HILIFE: 莱尔富
     * OKMART: OK超商
     * UNIMARTFREEZE: 7-ELEVEN超商(冷链)
     */
    private String CvsType;
    
    /**
     * 门店信息列表
     */
    private List<StoreInfoDto> StoreInfo;
}
