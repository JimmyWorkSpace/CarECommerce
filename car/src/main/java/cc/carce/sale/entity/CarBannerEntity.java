package cc.carce.sale.entity;

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
    private Boolean isLink;

    @Column(name = "showOrder")
    private Integer showOrder;

    @Column(name = "delFlag")
    private Boolean delFlag;

    @Column(name = "createTime")
    private Date createTime;

    /** 轮播图类型 1 首页轮播 2 商品页轮播 */
    @Column(name = "bannerType")
    private Integer bannerType;
} 