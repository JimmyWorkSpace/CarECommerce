package com.ruoyi.car.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "car_advertisement")
public class CarAdvertisementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titleType", columnDefinition = "INT(11) COMMENT '標題類型 0 圖片 1 富文本'")
    private Integer titleType;

    @Column(name = "titleHtml", columnDefinition = "LONGTEXT COMMENT '当標題類型为1富文本时，富文本内容'")
    private String titleHtml;


    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "linkUrl")
    private String linkUrl;

    @Column(name = "isLink")
    private Integer isLink;

    @Column(name = "content")
    private String content;

    @Column(name = "title")
    private String title;
    
    @Column(name = "advType")
    private Integer advType;

    @Column(name = "showOrder")
    private Integer showOrder;

    @Column(name = "delFlag")
    private Integer delFlag;

    @Column(name = "createTime")
    private Date createTime;
} 