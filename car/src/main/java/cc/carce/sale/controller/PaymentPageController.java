package cc.carce.sale.controller;

import cc.carce.sale.config.AuthInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

/**
 * 支付页面控制器
 */
@Slf4j
@Controller
@RequestMapping("/payment")
public class PaymentPageController {
    
    /**
     * 显示支付页面
     */
    @GetMapping("/index")
    public String showPaymentPage(@RequestParam(required = false) String itemName,
                                 @RequestParam(required = false) BigDecimal amount,
                                 @RequestParam(required = false) String description,
                                 @RequestParam(required = false) String cartData,
                                 Model model,
                                 HttpSession session) {
        
        // 检查用户登录状态
        AuthInterceptor.UserInfo userInfo = (AuthInterceptor.UserInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            log.warn("未登录用户尝试访问支付页面");
            return "redirect:/login?returnUrl=/payment/index";
        }
        
        // 获取当前环境配置
        String activeProfile = System.getProperty("spring.profiles.active");
        if (activeProfile == null) {
            activeProfile = "dev"; // 默认使用dev环境
        }
        
        // 如果是dev或test环境，金额固定为1
        BigDecimal finalAmount = amount;
        if ("dev".equals(activeProfile) || "test".equals(activeProfile)) {
            finalAmount = BigDecimal.ONE;
            log.info("开发/测试环境，支付金额固定为1元，原始金额: {}", amount);
        }
        
        // 设置页面数据
        model.addAttribute("itemName", itemName != null ? itemName : "汽车商品");
        model.addAttribute("amount", finalAmount != null ? finalAmount : BigDecimal.ZERO);
        model.addAttribute("description", description != null ? description : "");
        model.addAttribute("cartData", cartData);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("isDevOrTest", "dev".equals(activeProfile) || "test".equals(activeProfile));
        
        log.info("用户访问支付页面，用户ID: {}, 商品: {}, 金额: {}, 环境: {}", 
                userInfo.getId(), itemName, finalAmount, activeProfile);
        
        return "payment/index";
    }
}
