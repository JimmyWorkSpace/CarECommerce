package cc.carce.sale.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 車輛檢舉實體類
 */
@Data
public class CarReportEntity {
    
    private Long id;
    
    /**
     * 被檢舉的車輛銷售ID
     */
    private Long saleId;
    
    /**
     * 檢舉人ID
     */
    private Long reporterId;
    
    /**
     * 檢舉原因
     * price_mismatch: 價格與現場不符
     * false_info: 資料虛假
     * fraud_suspicion: 詐騙嫌疑
     * other: 其它
     */
    private String reason;
    
    /**
     * 詳細說明
     */
    private String description;
    
    /**
     * 是否匿名檢舉
     */
    private Boolean anonymous;
    
    /**
     * 處理狀態
     * submitted: 已提交
     * processing: 處理中
     * processed: 已處理
     * rejected: 已駁回
     */
    private String status;
    
    /**
     * 處理備註
     */
    private String processNote;
    
    /**
     * 處理人ID
     */
    private Long processorId;
    
    /**
     * 處理時間
     */
    private LocalDateTime processedAt;
    
    /**
     * 創建時間
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新時間
     */
    private LocalDateTime updatedAt;
}
