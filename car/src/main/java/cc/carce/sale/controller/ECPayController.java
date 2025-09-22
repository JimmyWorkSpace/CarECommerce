package cc.carce.sale.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.carce.sale.common.R;
import cc.carce.sale.config.AuthInterceptor.UserInfo;
import cc.carce.sale.entity.CarPaymentOrderEntity;
import cc.carce.sale.form.CreatePaymentForm;
import cc.carce.sale.service.ECPayService;
import lombok.extern.slf4j.Slf4j;

/**
 * 绿界支付控制器
 */
@Slf4j
@RestController
@RequestMapping({"/api/payment", "ecpay"})
public class ECPayController extends BaseController{
    
    @Resource
    private ECPayService ecPayService;
    
    /**
     * 创建支付订单
     */
    @PostMapping("/create")
    public R<Map<String, String>> createPayment(@RequestBody CreatePaymentForm form) {
        try {
            // 检查用户登录状态
            if (!isLogin()) {
                return R.fail("請先登錄", null);
            }
            
            UserInfo user = getSessionUser();
            
            // 参数验证
            if (form.getAmount() == null || form.getAmount() <= 0) {
                return R.fail("支付金額必須大於0", null);
            }
            
            if (form.getItemName() == null || form.getItemName().trim().isEmpty()) {
                return R.fail("商品名稱不能為空", null);
            }
            
            if (form.getReceiverName() == null || form.getReceiverName().trim().isEmpty()) {
                return R.fail("收件人姓名不能為空", null);
            }
            
            if (form.getReceiverMobile() == null || form.getReceiverMobile().trim().isEmpty()) {
                return R.fail("收件人手機號不能為空", null);
            }
            
            // 根据订单类型验证相应字段
            if (form.getOrderType() == null) {
                form.setOrderType(1); // 默认宅配到府
            }
            
            if (form.getOrderType() == 1) {
                // 宅配到府，验证地址
                if (form.getReceiverAddress() == null || form.getReceiverAddress().trim().isEmpty()) {
                    return R.fail("收件人地址不能為空", null);
                }
            } else if (form.getOrderType() == 2) {
                // 超商取货，验证门店信息
                if (form.getCvsStoreID() == null || form.getCvsStoreID().trim().isEmpty()) {
                    return R.fail("請選擇取貨門店", null);
                }
            }
            
            if (form.getDescription() == null || form.getDescription().trim().isEmpty()) {
                form.setDescription("购买商品：" + form.getItemName());
            }
            
            log.info("用户创建支付订单，用户ID: {}, 金额: {}, 商品: {}, 收件人: {}", 
                    user.getId(), form.getAmount(), form.getItemName(), form.getReceiverName());
            
            return ecPayService.createPaymentWithOrder(user.getId(), form);
            
        } catch (Exception e) {
            log.error("创建支付订单异常", e);
            return R.fail("創建支付訂單異常: " + e.getMessage(), null);
        }
    }
    
    /**
     * 查询支付订单状态
     */
    @GetMapping("/status/{merchantTradeNo}")
    public R<CarPaymentOrderEntity> queryPaymentStatus(@PathVariable String merchantTradeNo) {
        try {
            // 检查用户登录状态
            if (!isLogin()) {
                return R.fail("請先登錄", null);
            }
            
            UserInfo user = getSessionUser();
            
            // 查询订单状态
            R<CarPaymentOrderEntity> result = ecPayService.queryPaymentStatus(merchantTradeNo);
            
            if (result.isSuccess() && result.getData() != null) {
                // 检查订单所属用户
                if (!result.getData().getUserId().equals(user.getId())) {
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
            if (!isLogin()) {
                return R.fail("請先登錄", null);
            }
            
            UserInfo user = getSessionUser();
            
            log.info("用户取消支付订单，用户ID: {}, 商户订单号: {}", user.getId(), merchantTradeNo);
            
            return ecPayService.cancelPayment(merchantTradeNo, user.getId());
            
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
     * 获取支付配置信息
     */
    @GetMapping("/config")
    public R<Map<String, Object>> getPaymentConfig() {
        try {
            Map<String, Object> config = new HashMap<>();
            config.put("isProduction", ecPayService.isProductionEnvironment());
            config.put("serverUrl", ecPayService.getPaymentServerUrl());
            config.put("environment", ecPayService.getCurrentEnvironment());
            
            return R.ok("获取支付配置成功", config);
        } catch (Exception e) {
            log.error("获取支付配置异常", e);
            return R.fail("获取支付配置异常: " + e.getMessage(), null);
        }
    }
    
    /**
     * 绿界付款结果通知接口
     * 接收绿界POST过来的付款结果通知
     * 
     * @param request HTTP请求对象，包含绿界返回的所有参数
     * @return 返回"1|OK"表示接收成功
     */
    @PostMapping("/return")
    public String paymentReturn(HttpServletRequest request) {
        try {
            log.info("收到绿界付款结果通知");
            
            // 获取所有请求参数
            Map<String, String[]> parameterMap = request.getParameterMap();
            
            // 记录接收到的参数（用于调试）
            StringBuilder paramsLog = new StringBuilder("付款结果通知参数: ");
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                if (values != null && values.length > 0) {
                    paramsLog.append(key).append("=").append(values[0]).append(", ");
                }
            }
            log.info(paramsLog.toString());
            
            // 提取关键参数
            String merchantId = request.getParameter("MerchantID");
            String merchantTradeNo = request.getParameter("MerchantTradeNo");
            String rtnCode = request.getParameter("RtnCode");
            String rtnMsg = request.getParameter("RtnMsg");
            String tradeNo = request.getParameter("TradeNo");
            String tradeAmt = request.getParameter("TradeAmt");
            String paymentDate = request.getParameter("PaymentDate");
            String paymentType = request.getParameter("PaymentType");
            String checkMacValue = request.getParameter("CheckMacValue");
            
            log.info("付款结果通知 - 特店编号: {}, 特店交易编号: {}, 交易状态: {}, 交易消息: {}, 绿界交易编号: {}, 交易金额: {}, 付款时间: {}, 付款方式: {}", 
                    merchantId, merchantTradeNo, rtnCode, rtnMsg, tradeNo, tradeAmt, paymentDate, paymentType);
            
            // 验证必要参数
            if (merchantId == null || merchantTradeNo == null || rtnCode == null) {
                log.error("付款结果通知缺少必要参数");
                return "0|ERROR";
            }
            
            // 验证检查码（这里可以调用ECPayService的验证方法）
            boolean isValid = ecPayService.verifyPaymentResult(request);
            if (!isValid) {
                log.error("付款结果通知检查码验证失败");
                return "0|ERROR";
            }
            
            // 处理付款结果
            boolean processResult = ecPayService.processPaymentResult(
                merchantId, merchantTradeNo, rtnCode, rtnMsg, tradeNo, 
                tradeAmt, paymentDate, paymentType, checkMacValue
            );
            
            if (processResult) {
                log.info("付款结果处理成功 - 特店交易编号: {}", merchantTradeNo);
                return "1|OK";
            } else {
                log.error("付款结果处理失败 - 特店交易编号: {}", merchantTradeNo);
                return "0|ERROR";
            }
            
        } catch (Exception e) {
            log.error("处理付款结果通知时发生异常", e);
            return "0|ERROR";
        }
    }
    
}
