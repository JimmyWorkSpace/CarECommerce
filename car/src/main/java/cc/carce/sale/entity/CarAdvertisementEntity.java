package cc.carce.sale.entity;

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

    @Column(name = "showOrder")
    private Integer showOrder;

    @Column(name = "delFlag")
    private Integer delFlag;

    @Column(name = "createTime")
    private Date createTime;
} 