package cc.carce.sale.dto;

import lombok.Data;
import java.util.List;

/**
 * 绿界门店查询API响应DTO
 * 对应绿界API返回的完整响应结构
 */
@Data
public class ECPayStoreResponseDto {
    
    /**
     * 回应代码
     * 1 代表 API 执行成功，其余代码均为失败
     */
    private Integer RtnCode;
    
    /**
     * 回应消息
     */
    private String RtnMsg;
    
    /**
     * 商店清单
     */
    private List<StoreListDto> StoreList;
}
