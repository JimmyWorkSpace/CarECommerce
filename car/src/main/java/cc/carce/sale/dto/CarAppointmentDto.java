package cc.carce.sale.dto;

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
     * 预约人电话
     */
    private String appointmentPhone;

    /**
     * 预约时间
     */
    private Date appointmentTime;

    /**
     * 预约备注
     */
    private String appointmentNote;

    /**
     * 预约状态：1-已预约，2-已看车，3-已取消
     */
    private Integer appointmentStatus;

    /**
     * 预约状态描述
     */
    private String appointmentStatusDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    // 车辆相关信息
    /**
     * 车辆标题
     */
    private String carTitle;

    /**
     * 车辆品牌
     */
    private String carBrand;

    /**
     * 车辆型号
     */
    private String carModel;

    /**
     * 车辆价格
     */
    private Integer carPrice;

    /**
     * 车辆封面图片
     */
    private String carCoverImage;
}
