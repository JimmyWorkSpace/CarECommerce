package cc.carce.sale.controller;

import cc.carce.sale.common.R;
import cc.carce.sale.entity.PaymentOrderEntity;
import cc.carce.sale.service.ECPayService;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 绿界支付控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/payment")
public class ECPayController {
    
    @Resource
    private ECPayService ecPayService;
    
    /**
     * 创建支付订单
     */
    @PostMapping("/create")
    public R<Map<String, String>> createPayment(@RequestBody Map<String, Object> request) {
        try {
            // 检查用户登录状态
            if (!StpUtil.isLogin()) {
                return R.fail("請先登錄", null);
            }
            
            Long userId = StpUtil.getLoginIdAsLong();
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            String itemName = (String) request.get("itemName");
            String description = (String) request.get("description");
            
            // 参数验证
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                return R.fail("支付金額必須大於0", null);
            }
            
            if (itemName == null || itemName.trim().isEmpty()) {
                return R.fail("商品名稱不能為空", null);
            }
            
            if (description == null || description.trim().isEmpty()) {
                description = "购买商品：" + itemName;
            }
            
            log.info("用户创建支付订单，用户ID: {}, 金额: {}, 商品: {}", userId, amount, itemName);
            
            return ecPayService.createPayment(userId, amount, itemName, description);
            
        } catch (Exception e) {
            log.error("创建支付订单异常", e);
            return R.fail("創建支付訂單異常: " + e.getMessage(), null);
        }
    }
    
    /**
     * 查询支付订单状态
     */
    @GetMapping("/status/{merchantTradeNo}")
    public R<PaymentOrderEntity> queryPaymentStatus(@PathVariable String merchantTradeNo) {
        try {
            // 检查用户登录状态
            if (!StpUtil.isLogin()) {
                return R.fail("請先登錄", null);
            }
            
            Long userId = StpUtil.getLoginIdAsLong();
            
            // 查询订单状态
            R<PaymentOrderEntity> result = ecPayService.queryPaymentStatus(merchantTradeNo);
            
            if (result.isSuccess() && result.getData() != null) {
                // 检查订单所属用户
                if (!result.getData().getUserId().equals(userId)) {
                    return R.fail("無權限查看此訂單", null);
                }
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("查询支付订单状态异常", e);
            return R.fail("查詢支付訂單狀態異常", null);
        }
    }
    
    /**
     * 取消支付订单
     */
    @PostMapping("/cancel/{merchantTradeNo}")
    public R<Boolean> cancelPayment(@PathVariable String merchantTradeNo) {
        try {
            // 检查用户登录状态
            if (!StpUtil.isLogin()) {
                return R.fail("請先登錄", null);
            }
            
            Long userId = StpUtil.getLoginIdAsLong();
            
            log.info("用户取消支付订单，用户ID: {}, 商户订单号: {}", userId, merchantTradeNo);
            
            return ecPayService.cancelPayment(merchantTradeNo, userId);
            
        } catch (Exception e) {
            log.error("取消支付订单异常", e);
            return R.fail("取消支付訂單異常", null);
        }
    }
    
    /**
     * 绿界支付回调接口
     */
    @PostMapping("/callback")
    public String handlePaymentCallback(HttpServletRequest request) {
        try {
            // 获取所有回调参数
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, String> callbackParams = new java.util.HashMap<>();
            
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                if (values != null && values.length > 0) {
                    callbackParams.put(key, values[0]);
                }
            }
            
            log.info("收到绿界支付回调，参数数量: {}", callbackParams.size());
            
            // 处理回调
            return ecPayService.handlePaymentCallback(callbackParams);
            
        } catch (Exception e) {
            log.error("处理绿界支付回调异常", e);
            return "0|ErrorMessage";
        }
    }
    
    /**
     * 绿界支付同步返回接口
     */
    @GetMapping("/return")
    public String handlePaymentReturn(HttpServletRequest request) {
        try {
            // 获取所有返回参数
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, String> returnParams = new java.util.HashMap<>();
            
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                if (values != null && values.length > 0) {
                    returnParams.put(key, values[0]);
                }
            }
            
            log.info("收到绿界支付同步返回，参数数量: {}", returnParams.size());
            
            // 这里可以重定向到前端页面，显示支付结果
            // 或者返回支付结果页面
            return "支付處理完成，請查看訂單狀態";
            
        } catch (Exception e) {
            log.error("处理绿界支付同步返回异常", e);
            return "支付處理異常";
        }
    }
}
