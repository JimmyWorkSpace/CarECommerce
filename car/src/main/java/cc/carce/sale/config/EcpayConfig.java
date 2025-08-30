package cc.carce.sale.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "ecpay")
@Data
public class EcpayConfig {
    private String serviceUrl;        // 支付 API
    private String returnUrl;         // 支付回調
    private String storeCallbackUrl;  // 超商門市回調
    private String clientBackUrl;     // 使用者付款後返回頁面
    private String mapUrl;            // ✅ 新增：ECPay 物流門市地圖 API
    private String hashKey;
    private String hashIv;
    private String merchantId;
    
    // 🔹 新增物流 API URL
    private String logisticsUrl;
}
