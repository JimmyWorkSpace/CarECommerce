package cc.carce.sale.common;

import cc.carce.sale.config.ECPayConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 绿界支付工具类
 */
@Slf4j
@Component
public class ECPayUtils {
    
    @Resource
    private ECPayConfig ecPayConfig;
    
    /**
     * 生成绿界支付签名
     */
    public String generateSignature(Map<String, String> params) {
        try {
            // 1. 过滤空值参数
            Map<String, String> filteredParams = new TreeMap<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (StringUtils.isNotBlank(entry.getValue())) {
                    filteredParams.put(entry.getKey(), entry.getValue());
                }
            }
            
            // 2. 按参数名排序
            StringBuilder queryString = new StringBuilder();
            for (Map.Entry<String, String> entry : filteredParams.entrySet()) {
                if (queryString.length() > 0) {
                    queryString.append("&");
                }
                queryString.append(entry.getKey()).append("=").append(entry.getValue());
            }
            
            // 3. 加入HashKey和HashIV
            String hashKey = ecPayConfig.getHashKey();
            String hashIv = ecPayConfig.getHashIv();
            
            String signString = "HashKey=" + hashKey + "&" + queryString + "&HashIV=" + hashIv;
            
            // 4. URL编码
            String encodedString = URLEncoder.encode(signString, StandardCharsets.UTF_8.name());
            
            // 5. 转换为小写
            String lowerString = encodedString.toLowerCase();
            
            // 6. SHA256加密
            String signature = DigestUtils.sha256Hex(lowerString);
            
            log.debug("绿界支付签名生成 - 原始字符串: {}, 签名: {}", signString, signature);
            
            return signature.toUpperCase();
            
        } catch (Exception e) {
            log.error("生成绿界支付签名失败", e);
            throw new RuntimeException("生成支付签名失败", e);
        }
    }
    
    /**
     * 验证绿界支付回调签名
     */
    public boolean verifySignature(Map<String, String> params, String signature) {
        try {
            String calculatedSignature = generateSignature(params);
            boolean isValid = calculatedSignature.equalsIgnoreCase(signature);
            
            log.debug("绿界支付签名验证 - 计算签名: {}, 接收签名: {}, 验证结果: {}", 
                     calculatedSignature, signature, isValid);
            
            return isValid;
        } catch (Exception e) {
            log.error("验证绿界支付签名失败", e);
            return false;
        }
    }
    
    /**
     * 构建支付参数
     */
    public Map<String, String> buildPaymentParams(String orderId, String description, 
                                                 Integer totalAmount, String itemName) {
        Map<String, String> params = new HashMap<>();
        
        // 基本参数
        params.put("MerchantID", ecPayConfig.getMerchantId());
        params.put("MerchantTradeNo", orderId);
        params.put("MerchantTradeDate", formatDate(new Date()));
        params.put("PaymentType", "aio");
        params.put("TotalAmount", String.valueOf(totalAmount));
        params.put("TradeDesc", description);
        params.put("ItemName", itemName);
        params.put("ReturnURL", ecPayConfig.getReturnUrl());
        params.put("ClientBackURL", ecPayConfig.getClientBackUrl());
        params.put("OrderResultURL", ecPayConfig.getReturnUrl());
        
        // 支付方式设置
        params.put("ChoosePayment", "ALL"); // 全部支付方式
        params.put("IgnorePayment", ""); // 不忽略的支付方式
        
        // 物流设置（如果需要）
        // params.put("LogisticsType", "");
        // params.put("LogisticsSubType", "");
        
        // 发票设置（如果需要）
        // params.put("InvoiceMark", "N");
        
        // 语言设置
        params.put("Language", "ZH-TW");
        
        // 生成签名
        String signature = generateSignature(params);
        params.put("CheckMacValue", signature);
        
        return params;
    }
    
    /**
     * 格式化日期为绿界支付要求的格式 (yyyy/MM/dd HH:mm:ss)
     */
    private String formatDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        
        return String.format("%04d/%02d/%02d %02d:%02d:%02d", 
                           year, month, day, hour, minute, second);
    }
    
    /**
     * 解析绿界支付回调参数
     */
    public Map<String, String> parseCallbackParams(String queryString) {
        Map<String, String> params = new HashMap<>();
        
        if (StringUtils.isNotBlank(queryString)) {
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    try {
                        String key = keyValue[0];
                        String value = java.net.URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name());
                        params.put(key, value);
                    } catch (Exception e) {
                        log.warn("解析回调参数失败: {}", pair, e);
                    }
                }
            }
        }
        
        return params;
    }
}
