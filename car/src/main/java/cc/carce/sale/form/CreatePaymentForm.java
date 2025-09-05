package cc.carce.sale.form;

import java.util.List;
import lombok.Data;

@Data
public class CreatePaymentForm {
    
    /**
     * 支付金额
     */
    private Integer amount;
    
    /**
     * 商品名称
     */
    private String itemName;
    
    /**
     * 商品描述
     */
    private String description;
    
    /**
     * 收件人姓名
     */
    private String receiverName;
    
    /**
     * 收件人手机号
     */
    private String receiverMobile;
    
    /**
     * 收件人地址
     */
    private String receiverAddress;
    
    /**
     * 购物车数据
     */
    private List<CartItem> cartData;
}
