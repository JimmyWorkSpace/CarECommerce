package com.ruoyi.car.dto;

import lombok.Data;
import java.util.Date;

/**
 * 预约看车DTO
 */
@Data
public class CarAppointmentDto {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 车辆销售ID
     */
    private Long carSaleId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 预约人姓名
     */
    private String appointmentName;

    /**
     * 预约人電話
     */
    private String appointmentPhone;

    /**
     * 预约時間
     */
    private Date appointmentTime;

    /**
     * 预约备注
     */
    private String appointmentNote;

    /**
     * 预约狀態：1-已预约，2-已看车，3-已取消
     */
    private Integer appointmentStatus;

    /**
     * 预约狀態描述
     */
    private String appointmentStatusDesc;

    /**
     * 建立時間
     */
    private Date createTime;

    /**
     * 更新時間
     */
    private Date updateTime;

    // 车辆相关信息
    /**
     * 车辆销售標題
     */
    private String saleTitle;

    /**
     * 车辆標題
     */
    private String carTitle;

    /**
     * 车辆品牌
     */
    private String carBrand;

    /**
     * 车辆型號
     */
    private String carModel;

    /**
     * 车辆价格
     */
    private Integer carPrice;

    /**
     * 车辆封面圖片
     */
    private String carCoverImage;

    /**
     * 車輛網址
     */
    private String carUrl;
}
