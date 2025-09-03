package cc.carce.sale.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 绿界支付配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "ecpay")
public class ECPayConfig {
    
    /**
     * 绿界支付服务器地址
     */
    private String serverUrl = "https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5"; // 测试环境
    
    /**
     * 绿界支付正式环境地址
     */
    private String productionUrl = "https://payment.ecpay.com.tw/Cashier/AioCheckOut/V5";
    
    /**
     * 商户编号
     */
    private String merchantId;
    
    /**
     * 商户HashKey
     */
    private String hashKey;
    
    /**
     * 商户HashIV
     */
    private String hashIv;
    
    /**
     * 是否使用正式环境
     */
    private boolean production = false;
    
    /**
     * 支付完成后的返回URL
     */
    private String returnUrl;
    
    /**
     * 支付完成后的通知URL
     */
    private String notifyUrl;
    
    /**
     * 客户端返回URL
     */
    private String clientBackUrl;
    
    /**
     * 获取当前环境的服务器地址
     */
    public String getCurrentServerUrl() {
        return production ? productionUrl : serverUrl;
    }
}
