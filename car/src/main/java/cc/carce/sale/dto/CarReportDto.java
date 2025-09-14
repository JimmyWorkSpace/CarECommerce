package cc.carce.sale.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 車輛檢舉DTO
 */
@Data
public class CarReportDto {
    
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
     * 檢舉人姓名（如果不是匿名）
     */
    private String reporterName;
    
    /**
     * 檢舉原因
     */
    private String reason;
    
    /**
     * 檢舉原因顯示名稱
     */
    private String reasonDisplayName;
    
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
     */
    private String status;
    
    /**
     * 處理狀態顯示名稱
     */
    private String statusDisplayName;
    
    /**
     * 處理備註
     */
    private String processNote;
    
    /**
     * 處理人ID
     */
    private Long processorId;
    
    /**
     * 處理人姓名
     */
    private String processorName;
    
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
    
    /**
     * 車輛信息
     */
    private String carTitle;
    private String carBrand;
    private String carModel;
    private Integer carYear;
}
