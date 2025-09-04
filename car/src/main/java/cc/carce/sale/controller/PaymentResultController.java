package cc.carce.sale.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cc.carce.sale.common.R;
import cc.carce.sale.config.AuthInterceptor.UserInfo;
import cc.carce.sale.entity.CarPaymentOrderEntity;
import cc.carce.sale.service.ECPayService;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付结果页面控制器
 */
@Slf4j
@Controller
@RequestMapping("/payment")
public class PaymentResultController extends BaseController {
    
    @Resource
    private ECPayService ecPayService;
    
    /**
     * 显示支付结果页面
     */
    @GetMapping("/result")
    public String showPaymentResult(@RequestParam(required = false) String merchantTradeNo,
                                   Model model,
                                   HttpSession session) {
        
        // 检查用户登录状态
        UserInfo userInfo = getSessionUser();
        if (userInfo == null) {
            log.warn("未登录用户尝试访问支付结果页面");
            return "redirect:/login?returnUrl=/payment/result";
        }
        
        try {
            if (merchantTradeNo != null && !merchantTradeNo.trim().isEmpty()) {
                // 查询支付订单状态
            	R<CarPaymentOrderEntity> result = ecPayService.queryPaymentStatus(merchantTradeNo);
                
                if (result.isSuccess() && result.getData() != null) {
                    CarPaymentOrderEntity paymentOrder = result.getData();
                    
                    // 检查订单所属用户
                    if (!paymentOrder.getUserId().equals(userInfo.getId())) {
                        log.warn("用户尝试查看不属于自己的支付订单，用户ID: {}, 订单用户ID: {}", 
                                userInfo.getId(), paymentOrder.getUserId());
                        return "redirect:/payment/result";
                    }
                    
                    model.addAttribute("paymentOrder", paymentOrder);
                    model.addAttribute("paymentStatus", paymentOrder.getPaymentStatus());
                    
                    log.info("显示支付结果页面，商户订单号: {}, 支付状态: {}", 
                            merchantTradeNo, paymentOrder.getPaymentStatus());
                } else {
                    log.warn("未找到支付订单，商户订单号: {}", merchantTradeNo);
                    model.addAttribute("paymentStatus", -1); // 未知状态
                }
            } else {
                // 没有订单号，显示默认状态
                model.addAttribute("paymentStatus", -1);
            }
            
        } catch (Exception e) {
            log.error("查询支付订单状态异常", e);
            model.addAttribute("paymentStatus", -1);
        }
        
        model.addAttribute("userInfo", userInfo);
        return "payment/result";
    }
}
