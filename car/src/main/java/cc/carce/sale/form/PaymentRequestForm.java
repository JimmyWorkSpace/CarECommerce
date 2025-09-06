package cc.carce.sale.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 支付请求表单
 */
@Data
public class PaymentRequestForm {
    
    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    private String itemName;
    
    /**
     * 支付金额
     */
    @NotNull(message = "支付金额不能为空")
    @Min(value = 1, message = "支付金额必须大于0")
    private Integer amount;
    
    /**
     * 订单描述
     */
    private String description;
    
    /**
     * 购物车数据（JSON字符串）
     */
    private String cartData;
    
    /**
     * 返回URL
     */
    private String returnUrl;
}
