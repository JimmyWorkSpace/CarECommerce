package com.ruoyi.car.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "car_user")
public class CarUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phoneNumber", nullable = false, unique = true, columnDefinition = "VARCHAR(20) COMMENT '手機號'")
    private String phoneNumber;

    @Column(name = "nickName", columnDefinition = "VARCHAR(255) COMMENT '暱稱'")
    private String nickName;

    @Column(name = "delFlag", columnDefinition = "BIT(1) DEFAULT b'0' COMMENT '刪除标记 1 是 0 否'")
    private Boolean delFlag;

    @Column(name = "createTime", columnDefinition = "DATETIME COMMENT '建立時間'")
    private Date createTime;

    @Column(name = "lastLoginTime", columnDefinition = "DATETIME COMMENT '上次登入時間'")
    private Date lastLoginTime;
}
