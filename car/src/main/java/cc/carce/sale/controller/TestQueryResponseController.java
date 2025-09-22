package cc.carce.sale.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.carce.sale.common.R;
import cc.carce.sale.service.ECPayService;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试查询响应解析控制器
 * 用于测试绿界查询响应解析功能
 */
@Slf4j
@RestController
@RequestMapping("/test/query-response")
public class TestQueryResponseController {

    @Resource
    private ECPayService ecPayService;
    
    /**
     * 测试解析绿界查询响应
     * 
     * @return 解析结果
     */
    @GetMapping("/parse")
    public R<Map<String, String>> testParseResponse() {
        try {
            // 模拟绿界返回的响应数据
            String mockResponse = "CustomField1=&CustomField2=&CustomField3=&CustomField4=&HandlingCharge=0&ItemName=購物車商品 (1件)&MerchantID=2000132&MerchantTradeNo=202509221707307929&PaymentDate=&PaymentType=&PaymentTypeChargeFee=0&StoreID=&TradeAmt=200&TradeDate=2025/09/22 17:07:37&TradeNo=2509221707377125&TradeStatus=0&CheckMacValue=F3800E50E12F05B70D75124E44C73803A2E74D95E2892A58C380B0AD86DB3B30";
            
            log.info("测试解析响应: {}", mockResponse);
            
            // 使用反射调用私有方法进行测试
            java.lang.reflect.Method method = ECPayService.class.getDeclaredMethod("parseQueryResponse", String.class);
            method.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, String> result = (Map<String, String>) method.invoke(ecPayService, mockResponse);
            
            log.info("解析结果: {}", result);
            
            return R.ok("解析成功", result);
            
        } catch (Exception e) {
            log.error("测试解析响应异常", e);
            return R.fail("解析异常: " + e.getMessage(), null);
        }
    }
}
