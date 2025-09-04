package cc.carce.sale.controller;

import cc.carce.sale.common.ECPayUtils;
import cc.carce.sale.common.RedisUtil;
import cc.carce.sale.config.ECPayConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 * 用于测试Redis配置和绿界支付
 */
@Api(tags = "测试接口")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired(required = false)
    private RedisUtil redisUtil;
    
    @Resource
    private ECPayUtils ecPayUtils;
    
    @Resource
    private ECPayConfig ecPayConfig;

    /**
     * 测试Redis连接
     */
    @ApiOperation(value = "测试Redis连接", notes = "测试Redis是否正常工作")
    @GetMapping("/redis")
    public Map<String, Object> testRedis() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (redisUtil != null) {
                // 测试Redis写入
                redisUtil.set("test:key", "Hello Redis!");
                // 测试Redis读取
                Object value = redisUtil.get("test:key");
                
                result.put("success", true);
                result.put("message", "Redis连接正常");
                result.put("data", value);
            } else {
                result.put("success", false);
                result.put("message", "Redis未配置或不可用");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Redis连接失败: " + e.getMessage());
            result.put("error", e.toString());
        }
        
        return result;
    }

    /**
     * 测试Redis基本操作
     */
    @ApiOperation(value = "测试Redis基本操作", notes = "测试Redis的增删改查操作")
    @GetMapping("/redis/operations")
    public Map<String, Object> testRedisOperations() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (redisUtil != null) {
                // 测试字符串操作
                redisUtil.set("test:string", "测试字符串", 60);
                Object stringValue = redisUtil.get("test:string");
                
                // 测试Hash操作
                redisUtil.hset("test:hash", "field1", "value1");
                Object hashValue = redisUtil.hget("test:hash", "field1");
                
                // 测试过期时间
                long expire = redisUtil.getExpire("test:string");
                
                result.put("success", true);
                result.put("message", "Redis操作测试成功");
                result.put("data", new HashMap<String, Object>() {{
                    put("string", stringValue);
                    put("hash", hashValue);
                    put("expire", expire);
                }});
            } else {
                result.put("success", false);
                result.put("message", "Redis未配置或不可用");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Redis操作测试失败: " + e.getMessage());
            result.put("error", e.toString());
        }
        
        return result;
    }
    
    /**
     * 测试绿界支付签名生成
     */
    @ApiOperation(value = "测试绿界支付签名", notes = "测试绿界支付签名生成是否正确")
    @GetMapping("/ecpay/signature")
    public Map<String, Object> testECPaySignature() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 创建测试参数
            Map<String, String> testParams = new HashMap<>();
            testParams.put("MerchantID", ecPayConfig.getMerchantId());
            testParams.put("MerchantTradeNo", "202412011200001");
            testParams.put("MerchantTradeDate", "2024/12/01 12:00:00");
            testParams.put("PaymentType", "aio");
            testParams.put("TotalAmount", "1");
            testParams.put("TradeDesc", "测试订单");
            testParams.put("ItemName", "测试商品");
            testParams.put("ReturnURL", ecPayConfig.getReturnUrl());
            testParams.put("ClientBackURL", ecPayConfig.getClientBackUrl());
            testParams.put("OrderResultURL", ecPayConfig.getReturnUrl());
            testParams.put("ChoosePayment", "ALL");
            testParams.put("Language", "ZH-TW");
            
            // 生成签名
            String signature = ecPayUtils.generateSignature(testParams);
            
            result.put("success", true);
            result.put("message", "绿界支付签名生成成功");
            result.put("data", new HashMap<String, Object>() {{
                put("merchantId", ecPayConfig.getMerchantId());
                put("hashKey", ecPayConfig.getHashKey());
                put("hashIv", ecPayConfig.getHashIv());
                put("production", ecPayConfig.isProduction());
                put("testParams", testParams);
                put("signature", signature);
            }});
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "绿界支付签名生成失败: " + e.getMessage());
            result.put("error", e.toString());
        }
        
        return result;
    }
    
    /**
     * 详细测试绿界支付签名生成过程
     */
    @ApiOperation(value = "详细测试绿界支付签名", notes = "显示签名生成的详细步骤")
    @GetMapping("/ecpay/signature/detail")
    public Map<String, Object> testECPaySignatureDetail() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 创建测试参数
            Map<String, String> testParams = new HashMap<>();
            testParams.put("MerchantID", ecPayConfig.getMerchantId());
            testParams.put("MerchantTradeNo", "202412011200001");
            testParams.put("MerchantTradeDate", "2024/12/01 12:00:00");
            testParams.put("PaymentType", "aio");
            testParams.put("TotalAmount", "1");
            testParams.put("TradeDesc", "测试订单");
            testParams.put("ItemName", "测试商品");
            testParams.put("ReturnURL", ecPayConfig.getReturnUrl());
            testParams.put("ClientBackURL", ecPayConfig.getClientBackUrl());
            testParams.put("OrderResultURL", ecPayConfig.getReturnUrl());
            testParams.put("ChoosePayment", "ALL");
            testParams.put("Language", "ZH-TW");
            
            // 手动执行签名生成步骤
            Map<String, String> filteredParams = new java.util.TreeMap<>();
            for (Map.Entry<String, String> entry : testParams.entrySet()) {
                if (entry.getValue() != null && !entry.getValue().trim().isEmpty() && !"CheckMacValue".equals(entry.getKey())) {
                    filteredParams.put(entry.getKey(), entry.getValue());
                }
            }
            
            // 构建查询字符串
            StringBuilder queryString = new StringBuilder();
            for (Map.Entry<String, String> entry : filteredParams.entrySet()) {
                if (queryString.length() > 0) {
                    queryString.append("&");
                }
                queryString.append(entry.getKey()).append("=").append(entry.getValue());
            }
            
            // 加入HashKey和HashIV
            String hashKey = ecPayConfig.getHashKey();
            String hashIv = ecPayConfig.getHashIv();
            String signString = "HashKey=" + hashKey + "&" + queryString + "&HashIV=" + hashIv;
            
            // URL编码 - 按照绿界支付官方文档要求
            // 根据官方文档：https://developers.ecpay.com.tw/?p=2902
            // 需要按照.NET编码(ecpay)规则进行URL编码
            String encodedString = java.net.URLEncoder.encode(signString, "UTF-8")
                    .replace("%2d", "-")   // 连字符
                    .replace("%5f", "_")   // 下划线
                    .replace("%2e", ".")   // 点号
                    .replace("%21", "!")   // 感叹号
                    .replace("%2a", "*")   // 星号
                    .replace("%28", "(")   // 左括号
                    .replace("%29", ")");  // 右括号
            
            // 转换为小写
            String lowerString = encodedString.toLowerCase();
            
            // SHA256加密
            String signature = org.apache.commons.codec.digest.DigestUtils.sha256Hex(lowerString).toUpperCase();
            
            result.put("success", true);
            result.put("message", "绿界支付签名详细生成成功");
            result.put("data", new HashMap<String, Object>() {{
                put("step1_filteredParams", filteredParams);
                put("step2_queryString", queryString.toString());
                put("step3_signString", signString);
                put("step4_encodedString", encodedString);
                put("step5_lowerString", lowerString);
                put("step6_signature", signature);
                put("hashKey", hashKey);
                put("hashIv", hashIv);
            }});
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "绿界支付签名详细生成失败: " + e.getMessage());
            result.put("error", e.toString());
        }
        
        return result;
    }
    
    /**
     * 测试绿界支付配置
     */
    @ApiOperation(value = "测试绿界支付配置", notes = "检查绿界支付配置是否正确")
    @GetMapping("/ecpay/config")
    public Map<String, Object> testECPayConfig() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            result.put("success", true);
            result.put("message", "绿界支付配置检查完成");
            result.put("data", new HashMap<String, Object>() {{
                put("merchantId", ecPayConfig.getMerchantId());
                put("hashKey", ecPayConfig.getHashKey());
                put("hashIv", ecPayConfig.getHashIv());
                put("production", ecPayConfig.isProduction());
                put("serverUrl", ecPayConfig.getCurrentServerUrl());
                put("returnUrl", ecPayConfig.getReturnUrl());
                put("notifyUrl", ecPayConfig.getNotifyUrl());
                put("clientBackUrl", ecPayConfig.getClientBackUrl());
            }});
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "绿界支付配置检查失败: " + e.getMessage());
            result.put("error", e.toString());
        }
        
        return result;
    }
    
    /**
     * 绿界支付测试页面
     */
    @GetMapping("/ecpay")
    public String ecpayTestPage(Model model) {
        model.addAttribute("title", "绿界支付测试");
        return "test/ecpay-test";
    }
    
    /**
     * 绿界支付签名验证页面
     */
    @GetMapping("/ecpay/verify")
    public String ecpayVerifyPage(Model model) {
        model.addAttribute("title", "绿界支付签名验证");
        return "test/ecpay-verify";
    }
    
    /**
     * 测试全方位金流支付参数构建
     */
    @ApiOperation(value = "测试全方位金流支付", notes = "按照绿界支付官方API文档测试全方位金流支付参数")
    @GetMapping("/ecpay/aio")
    public Map<String, Object> testAioPayment() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 使用ECPayUtils构建支付参数
            Map<String, String> paymentParams = ecPayUtils.buildPaymentParams(
                "202412011200001", 
                "全方位金流测试订单", 
                1, 
                "测试商品"
            );
            
            result.put("success", true);
            result.put("message", "全方位金流支付参数构建成功");
            result.put("data", new HashMap<String, Object>() {{
                put("paymentParams", paymentParams);
                put("paramCount", paymentParams.size());
                put("requiredParams", new String[]{
                    "MerchantID", "MerchantTradeNo", "MerchantTradeDate", 
                    "PaymentType", "TotalAmount", "TradeDesc", "ItemName", 
                    "ReturnURL", "ChoosePayment", "EncryptType", "CheckMacValue"
                });
                put("apiUrl", ecPayConfig.getCurrentServerUrl());
                put("isProduction", ecPayConfig.isProduction());
            }});
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "全方位金流支付参数构建失败: " + e.getMessage());
            result.put("error", e.toString());
        }
        
        return result;
    }
} 