package com.ruoyi.car.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "car_config")
public class CarConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID'")
    private Long id;

    @Column(name = "code", nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置代码'")
    private String code;

    @Column(name = "value", nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置值'")
    private String value;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名称'")
    private String name;

    @Column(name = "showOrder", columnDefinition = "INT(11) DEFAULT 0 COMMENT '显示顺序'")
    private Integer showOrder;

    @Column(name = "delFlag", columnDefinition = "INT(11) DEFAULT 0 COMMENT '删除标记 1 是 0 否'")
    private Integer delFlag;

    @Column(name = "createTime", columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;
}
