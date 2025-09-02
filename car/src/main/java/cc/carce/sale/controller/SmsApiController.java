package cc.carce.sale.controller;

import cc.carce.sale.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信API控制器
 */
@RestController
@RequestMapping("/api/sms")
@Slf4j
public class SmsApiController {

    @Resource
    private SmsService smsService;

    /**
     * 发送短信验证码
     */
    @PostMapping("/send")
    public Map<String, Object> sendSmsCode(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String phoneNumber = request.get("phoneNumber");
            
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "手機號碼不能為空");
                return response;
            }
            
            // 验证手机号格式
            if (!phoneNumber.matches("^1[3-9]\\d{9}$")) {
                response.put("success", false);
                response.put("message", "手機號碼格式不正確");
                return response;
            }
            
            // 检查是否可以重新发送
            if (!smsService.canResendCode(phoneNumber)) {
                response.put("success", false);
                response.put("message", "請稍後再發送驗證碼");
                return response;
            }
            
            // 发送验证码
            String code = smsService.sendSmsCode(phoneNumber);
            
            response.put("success", true);
            response.put("message", "驗證碼發送成功");
            response.put("code", code); // 开发环境返回验证码，生产环境应该删除
            
            log.info("短信验证码发送成功，手机号: {}", phoneNumber);
            
        } catch (Exception e) {
            log.error("发送短信验证码失败", e);
            response.put("success", false);
            response.put("message", "發送失敗，請稍後重試");
        }
        
        return response;
    }
}
