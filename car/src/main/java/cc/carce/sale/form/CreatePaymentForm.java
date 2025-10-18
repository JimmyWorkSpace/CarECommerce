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

    private String receiverCity;

    private String receiverDistrict;
    
    private String receiverZipCode;
    /**
     * 订单类型 1 宅配到府 2 超商取货
     */
    private Integer orderType;
    
    /**
     * 超商店舖編號
     */
    private String cvsStoreID;
    
    /**
     * 超商店舖名稱
     */
    private String cvsStoreName;
    
    /**
     * 超商店舖地址
     */
    private String cvsAddress;
    
    /**
     * 超商店舖電話
     */
    private String cvsTelephone;
    
    /**
     * 使用者選擇的超商店舖是否為離島店鋪.0：本島,1：離島
     */
    private Integer cvsOutSide;
    
    /**
     * 购物车数据
     */
    private List<CartItem> cartData;
    
    /**
     * 订单ID（用于重新支付）
     */
    private Long orderId;
}
