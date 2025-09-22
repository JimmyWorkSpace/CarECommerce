package com.ruoyi.car.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 问答模块实体类
 */
@Data
@Table(name = "car_question_answer")
public class CarQuestionAnswerEntity {
    
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) COMMENT '主键ID'")
    private Long id;
    
    /**
     * 频道ID
     */
    @Column(name = "channelId", nullable = false, columnDefinition = "BIGINT(20) COMMENT '频道ID'")
    private Long channelId;
    
    /**
     * 问题
     */
    @Column(name = "question", columnDefinition = "VARCHAR(2000) COMMENT '问题'")
    private String question;
    
    /**
     * 回答
     */
    @Column(name = "answer", columnDefinition = "LONGTEXT COMMENT '回答'")
    private String answer;
    
    /**
     * 排序
     */
    @Column(name = "showOrder", columnDefinition = "INT(11) COMMENT '排序'")
    private Integer showOrder;
    
    /**
     * 删除标记 1 是 0 否
     */
    @Column(name = "delFlag", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0 COMMENT '删除标记 1 是 0 否'")
    private Boolean delFlag;
    
    /**
     * 创建时间
     */
    @Column(name = "createTime", columnDefinition = "DATETIME COMMENT '创建时间'")
    private LocalDateTime createTime;
}
