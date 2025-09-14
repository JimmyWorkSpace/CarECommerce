package cc.carce.sale.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 预约看车实体类
 */
@Data
@Entity
@Table(name = "car_appointment")
public class CarAppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) COMMENT '主键ID'")
    private Long id;

    /**
     * 车辆销售ID
     */
    @Column(name = "carSaleId", nullable = false, columnDefinition = "BIGINT(20) COMMENT '车辆销售ID'")
    private Long carSaleId;

    /**
     * 用户ID
     */
    @Column(name = "userId", nullable = false, columnDefinition = "BIGINT(20) COMMENT '用户ID'")
    private Long userId;

    /**
     * 预约人姓名
     */
    @Column(name = "appointmentName", nullable = false, columnDefinition = "VARCHAR(50) COMMENT '预约人姓名'")
    private String appointmentName;

    /**
     * 预约人电话
     */
    @Column(name = "appointmentPhone", nullable = false, columnDefinition = "VARCHAR(20) COMMENT '预约人电话'")
    private String appointmentPhone;

    /**
     * 预约时间
     */
    @Column(name = "appointmentTime", nullable = false, columnDefinition = "DATETIME COMMENT '预约时间'")
    private Date appointmentTime;

    /**
     * 预约备注
     */
    @Column(name = "appointmentNote", columnDefinition = "TEXT COMMENT '预约备注'")
    private String appointmentNote;

    /**
     * 预约状态：1-已预约，2-已看车，3-已取消
     */
    @Column(name = "appointmentStatus", nullable = false, columnDefinition = "INT(11) DEFAULT 1 COMMENT '预约状态：1-已预约，2-已看车，3-已取消'")
    private Integer appointmentStatus;

    /**
     * 删除标记：0-未删除，1-已删除
     */
    @Column(name = "delFlag", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0 COMMENT '删除标记：0-未删除，1-已删除'")
    private Boolean delFlag;

    /**
     * 创建时间
     */
    @Column(name = "createTime", nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "updateTime", columnDefinition = "DATETIME COMMENT '更新时间'")
    private Date updateTime;

    /**
     * 预约状态枚举
     */
    public enum AppointmentStatus {
        APPOINTED(1, "已预约"),
        VIEWED(2, "已看车"),
        CANCELLED(3, "已取消");

        private final Integer code;
        private final String description;

        AppointmentStatus(Integer code, String description) {
            this.code = code;
            this.description = description;
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static AppointmentStatus fromCode(Integer code) {
            for (AppointmentStatus status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("未知的预约状态码: " + code);
        }
    }
}
