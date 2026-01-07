package com.ruoyi.car.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Table(name = "car_menu")
public class CarMenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "showOrder")
    private Integer showOrder;

    @Column(name = "delFlag")
    private Integer delFlag;

    @Column(name = "createTime")
    private Date createTime;

    @Column(name = "isShow")
    private Integer isShow;

    @Column(name = "canDel")
    private Integer canDel;

    @Column(name = "linkUrl")
    private String linkUrl;

    @Column(name = "linkType")
    private Integer linkType;

    @Column(name = "content")
    private String content;

    @Column(name = "ogTitle")
    private String ogTitle;

    @Column(name = "ogImage")
    private String ogImage;

    @Column(name = "ogDesp")
    private String ogDesp;

    @Column(name = "parentId")
    private Long parentId;

    /** 子菜单 */
    @Transient
    private List<CarMenuEntity> children = new ArrayList<CarMenuEntity>();
}
