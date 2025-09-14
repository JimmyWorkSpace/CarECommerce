package cc.carce.sale.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 車輛檢舉表單
 */
@Data
public class CarReportForm {
    
    @NotNull(message = "車輛銷售ID不能為空")
    private Long saleId;
    
    @NotNull(message = "檢舉人ID不能為空")
    private Long reporterId;
    
    @NotBlank(message = "檢舉原因不能為空")
    private String reason;
    
    @NotBlank(message = "詳細說明不能為空")
    private String description;
    
    private Boolean anonymous = false;
}
