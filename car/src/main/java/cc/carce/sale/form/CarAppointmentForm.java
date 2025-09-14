package cc.carce.sale.form;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 预约看车表单
 */
@Data
public class CarAppointmentForm {

    /**
     * 车辆销售ID
     */
    @NotNull(message = "车辆销售ID不能为空")
    private Long carSaleId;

    /**
     * 预约人姓名
     */
    @NotBlank(message = "预约人姓名不能为空")
    private String appointmentName;

    /**
     * 预约人电话
     */
    @NotBlank(message = "预约人电话不能为空")
    private String appointmentPhone;

    /**
     * 预约时间
     */
    @NotNull(message = "预约时间不能为空")
    private Date appointmentTime;

    /**
     * 预约备注
     */
    private String appointmentNote;
}
