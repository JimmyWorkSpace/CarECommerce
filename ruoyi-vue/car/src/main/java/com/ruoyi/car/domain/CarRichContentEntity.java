package com.ruoyi.car.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 富文本内容实体类
 */
@Data
@Table(name = "car_rich_content")
public class CarRichContentEntity {
    
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) COMMENT '主键ID'")
    private Long id;
    
    /**
     * 標題
     */
    @Column(name = "title", columnDefinition = "VARCHAR(255) COMMENT '標題'")
    private String title;
    
    /**
     * 内容
     */
    @Column(name = "content", columnDefinition = "LONGTEXT COMMENT '内容'")
    private String content;
    
    /**
     * 類型 1 关于 2 頻道
     */
    @Column(name = "contentType", columnDefinition = "INT(11) COMMENT '類型 1 关于 2 頻道'")
    private Integer contentType;
    
    /**
     * 排序
     */
    @Column(name = "showOrder", columnDefinition = "INT(11) COMMENT '排序'")
    private Integer showOrder;
    
    /**
     * 刪除标记 1 是 0 否
     */
    @Column(name = "delFlag", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0 COMMENT '刪除标记 1 是 0 否'")
    private Boolean delFlag;
    
    /**
     * 建立時間
     */
    @Column(name = "createTime", columnDefinition = "DATETIME COMMENT '建立時間'")
    private LocalDateTime createTime;
    
    /**
     * 内容類型枚举
     */
    public enum ContentType {
        ABOUT(1, "关于"),
        CHANNEL(2, "頻道");
        
        private final Integer code;
        private final String description;
        
        ContentType(Integer code, String description) {
            this.code = code;
            this.description = description;
        }
        
        public Integer getCode() {
            return code;
        }
        
        public String getDescription() {
            return description;
        }
        
        public static ContentType fromCode(Integer code) {
            for (ContentType type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("未知的内容類型码: " + code);
        }
    }
}
