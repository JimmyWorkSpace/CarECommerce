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
     * 按照绿界支付官方检查码机制文档：https://developers.ecpay.com.tw/?p=2902
     * 1. 将传递参数依照第一个英文字母，由A到Z的顺序来排序
     * 2. 参数最前面加上HashKey、最后面加上HashIV
     * 3. 将整串字串进行URL encode（按照.NET编码(ecpay)规则）
     * 4. 转为小写
     * 5. 以SHA256加密方式来产生杂凑值
     * 6. 再转大写产生CheckMacValue
     */
    public String generateSignature(Map<String, String> params) {
        try {
            // 1. 过滤空值参数和CheckMacValue，并按字母顺序排序
            Map<String, String> filteredParams = new TreeMap<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (StringUtils.isNotBlank(entry.getValue()) && !"CheckMacValue".equals(entry.getKey())) {
                    filteredParams.put(entry.getKey(), entry.getValue());
                }
            }
            
            // 2. 构建查询字符串（参数已按字母顺序排序）
            StringBuilder queryString = new StringBuilder();
            for (Map.Entry<String, String> entry : filteredParams.entrySet()) {
                if (queryString.length() > 0) {
                    queryString.append("&");
                }
                queryString.append(entry.getKey()).append("=").append(entry.getValue());
            }
            
            // 3. 获取HashKey和HashIV
            String hashKey = ecPayConfig.getHashKey();
            String hashIv = ecPayConfig.getHashIv();
            
            // 4. 构建待签名字符串：HashKey=xxx&参数串&HashIV=xxx
            String signString = "HashKey=" + hashKey + "&" + queryString + "&HashIV=" + hashIv;
            
            log.info("绿界支付签名生成 - 步骤1 过滤后参数: {}", filteredParams);
            log.info("绿界支付签名生成 - 步骤2 查询字符串: {}", queryString.toString());
            log.info("绿界支付签名生成 - 步骤3 待签名字符串: {}", signString);
            
            // 5. URL编码（UTF-8）- 按照绿界支付官方文档要求
            // 根据官方文档：https://developers.ecpay.com.tw/?p=2902
            // 需要按照.NET编码(ecpay)规则进行URL编码
            String encodedString = URLEncoder.encode(signString, StandardCharsets.UTF_8.name())
                    .replace("%2d", "-")   // 连字符
                    .replace("%5f", "_")   // 下划线
                    .replace("%2e", ".")   // 点号
                    .replace("%21", "!")   // 感叹号
                    .replace("%2a", "*")   // 星号
                    .replace("%28", "(")   // 左括号
                    .replace("%29", ")");  // 右括号
            log.info("绿界支付签名生成 - 步骤4 URL编码后: {}", encodedString);
            
            // 6. 转换为小写
            String lowerString = encodedString.toLowerCase();
            log.info("绿界支付签名生成 - 步骤5 小写转换后: {}", lowerString);
            
            // 7. SHA256加密
            String signature = DigestUtils.sha256Hex(lowerString);
            log.info("绿界支付签名生成 - 步骤6 SHA256加密后: {}", signature);
            
            // 8. 转换为大写
            String finalSignature = signature.toUpperCase();
            log.info("绿界支付签名生成 - 步骤7 最终签名: {}", finalSignature);
            
            return finalSignature;
            
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
     * 构建全方位金流支付参数
     * 根据绿界支付官方API文档：https://developers.ecpay.com.tw/?p=2862
     */
    public Map<String, String> buildPaymentParams(String orderId, String description, 
                                                 Integer totalAmount, String itemName) {
        Map<String, String> params = new HashMap<>();
        
        // === 必填参数 ===
        // 特店编号
        params.put("MerchantID", ecPayConfig.getMerchantId());
        // 特店订单编号（唯一值，不可重复）
        params.put("MerchantTradeNo", orderId);
        // 特店交易时间（格式：yyyy/MM/dd HH:mm:ss）
        params.put("MerchantTradeDate", formatDate(new Date()));
        // 交易类型（固定填入 aio）
        params.put("PaymentType", "aio");
        // 交易金额（整数，不可有小数点，仅限新台币）
        params.put("TotalAmount", String.valueOf(totalAmount));
        // 交易描述（请勿带入特殊字元）
        params.put("TradeDesc", description);
        // 商品名称（如果有多笔，用#分隔）
        params.put("ItemName", itemName);
        // 付款完成通知回传网址（Server POST方式）
        params.put("ReturnURL", ecPayConfig.getReturnUrl());
        // 选择预设付款方式（ALL：不指定付款方式，由绿界显示付款方式选择页面）
        params.put("ChoosePayment", "ALL");
        
        // === 加密类型（必填） ===
        params.put("EncryptType", "1");
        
        // === 可选参数 ===
        // 客户端返回特店网址
        if (StringUtils.isNotBlank(ecPayConfig.getClientBackUrl())) {
            params.put("ClientBackURL", ecPayConfig.getClientBackUrl() + orderId);
        }
        
        // 客户端回传付款结果网址
        if (StringUtils.isNotBlank(ecPayConfig.getReturnUrl())) {
            params.put("OrderResultURL", ecPayConfig.getReturnUrl());
        }
        
        // 语言设定（预设为中文）
//        params.put("Language", "CHI");
        
        // 是否需要额外的付款资讯（Y/N）
//        params.put("NeedExtraPaidInfo", "N");
        
        // 隐藏付款方式（如要隐藏ATM和CVS：ATM#CVS）
        // params.put("IgnorePayment", "ATM#CVS");
        
        // 备注栏位
        // params.put("Remark", "备注信息");
        
        // 商品网址
        // params.put("ItemURL", "https://www.yourdomain.com/item");
        
        // 客制化栏位
        // params.put("CustomField1", "客制化栏位1");
        // params.put("CustomField2", "客制化栏位2");
        // params.put("CustomField3", "客制化栏位3");
        // params.put("CustomField4", "客制化栏位4");
        
        log.info("构建支付参数 - 订单号: {}, 金额: {}, 商品: {}", orderId, totalAmount, itemName);
        
        // 生成签名
        String signature = generateSignature(params);
        params.put("CheckMacValue", signature);
        
        log.info("支付参数构建完成，包含 {} 个参数", params.size());
        
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
