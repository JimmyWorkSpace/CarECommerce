package com.ruoyi.car.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import lombok.Data;

/**
 * 票券方案實體類
 * 停用之票券方案不可再被購買；已售出票券不因方案停用而失效。
 *
 * @author ruoyi
 */
@Data
@Table(name = "car_card")
public class CarCardEntity {

    /** 主鍵ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主鍵ID'")
    @Excel(name = "主鍵ID", cellType = ColumnType.NUMERIC)
    private Long id;

    /** 票券名稱 */
    @Excel(name = "票券名稱")
    @Column(name = "cardName", nullable = false, columnDefinition = "VARCHAR(100) NOT NULL COMMENT '票券名稱'")
    private String cardName;

    /** 售價 */
    @Excel(name = "售價")
    @Column(name = "salePrice", nullable = false, columnDefinition = "DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '售價'")
    private BigDecimal salePrice;

    /** 使用說明 */
    @Excel(name = "使用說明")
    @Column(name = "usageInstruction", columnDefinition = "TEXT COMMENT '使用說明'")
    private String usageInstruction;

    /** 有效期限類型：1=指定日期 2=指定天數 */
    @Excel(name = "有效期限類型", readConverterExp = "1=指定日期,2=指定天數")
    @Column(name = "validityType", nullable = false, columnDefinition = "TINYINT(1) NOT NULL DEFAULT 1 COMMENT '有效期限類型：1=指定日期 2=指定天數'")
    private Integer validityType;

    /** 指定日期時的有效截止日（validity_type=1 時使用） */
    @Excel(name = "有效截止日", dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "validityEndDate", columnDefinition = "DATE DEFAULT NULL COMMENT '指定日期時的有效截止日'")
    private Date validityEndDate;

    /** 指定天數（購買後 N 天內有效，validity_type=2 時使用） */
    @Excel(name = "有效天數")
    @Column(name = "validityDays", columnDefinition = "INT(11) DEFAULT NULL COMMENT '指定天數'")
    private Integer validityDays;

    /** 啟用狀態：1=啟用 0=停用（停用後不可再被購買） */
    @Excel(name = "狀態", readConverterExp = "1=啟用,0=停用")
    @Column(name = "status", nullable = false, columnDefinition = "TINYINT(1) NOT NULL DEFAULT 1 COMMENT '啟用狀態：1=啟用 0=停用'")
    private Integer status;

    /** 建立者 */
    @Column(name = "createBy", columnDefinition = "VARCHAR(64) DEFAULT '' COMMENT '建立者'")
    private String createBy;

    /** 建立時間 */
    @Excel(name = "建立時間", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "createTime", columnDefinition = "DATETIME DEFAULT NULL COMMENT '建立時間'")
    private Date createTime;

    /** 更新者 */
    @Column(name = "updateBy", columnDefinition = "VARCHAR(64) DEFAULT '' COMMENT '更新者'")
    private String updateBy;

    /** 更新時間 */
    @Column(name = "updateTime", columnDefinition = "DATETIME DEFAULT NULL COMMENT '更新時間'")
    private Date updateTime;

    /** 備註 */
    @Excel(name = "備註")
    @Column(name = "remark", columnDefinition = "VARCHAR(500) DEFAULT NULL COMMENT '備註'")
    private String remark;
}
