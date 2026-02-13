package com.ruoyi.car.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "car_banner")
public class CarBannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "linkUrl")
    private String linkUrl;

    @Column(name = "isLink")
    private Integer isLink;

    @Column(name = "showOrder")
    private Integer showOrder;

    @Column(name = "delFlag")
    private Integer delFlag;

    @Column(name = "createTime")
    private Date createTime;

    /** 輪播圖類型 1 首頁輪播 2 商品頁輪播 */
    @Column(name = "bannerType")
    private Integer bannerType;
} 