package com.ruoyi.car.domain;

import lombok.Data;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 車輛檢舉實體類
 */
@Data
@Entity
@Table(name = "car_reports")
public class CarReportEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) COMMENT '主鍵ID'")
    private Long id;
    
    /**
     * 被檢舉的車輛銷售ID
     */
    @Column(name = "sale_id", nullable = false, columnDefinition = "BIGINT(20) COMMENT '被檢舉的車輛銷售ID'")
    private Long saleId;
    
    /**
     * 檢舉人ID
     */
    @Column(name = "reporter_id", nullable = false, columnDefinition = "BIGINT(20) COMMENT '檢舉人ID'")
    private Long reporterId;
    
    /**
     * 檢舉原因
     * price_mismatch: 價格與現場不符
     * false_info: 資料虛假
     * fraud_suspicion: 詐騙嫌疑
     * other: 其它
     */
    @Column(name = "reason", nullable = false, columnDefinition = "VARCHAR(50) COMMENT '檢舉原因：price_mismatch(價格與現場不符), false_info(資料虛假), fraud_suspicion(詐騙嫌疑), other(其它)'")
    private String reason;
    
    /**
     * 詳細說明
     */
    @Column(name = "description", nullable = false, columnDefinition = "TEXT COMMENT '詳細說明'")
    private String description;
    
    /**
     * 是否匿名檢舉
     */
    @Column(name = "anonymous", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0 COMMENT '是否匿名檢舉：0-否，1-是'")
    private Boolean anonymous;
    
    /**
     * 處理狀態
     * submitted: 已提交
     * processing: 處理中
     * processed: 已處理
     * rejected: 已駁回
     */
    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'submitted' COMMENT '處理狀態：submitted(已提交), processing(處理中), processed(已處理), rejected(已駁回)'")
    private String status;
    
    /**
     * 處理備註
     */
    @Column(name = "process_note", columnDefinition = "TEXT COMMENT '處理備註'")
    private String processNote;
    
    /**
     * 處理人ID
     */
    @Column(name = "processor_id", columnDefinition = "BIGINT(20) COMMENT '處理人ID'")
    private Long processorId;
    
    /**
     * 處理時間
     */
    @Column(name = "processed_at", columnDefinition = "DATETIME COMMENT '處理時間'")
    private LocalDateTime processedAt;
    
    /**
     * 創建時間
     */
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME COMMENT '創建時間'")
    private LocalDateTime createdAt;
    
    /**
     * 更新時間
     */
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME COMMENT '更新時間'")
    private LocalDateTime updatedAt;
}
