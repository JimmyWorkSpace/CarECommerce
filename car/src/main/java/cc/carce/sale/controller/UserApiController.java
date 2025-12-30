package cc.carce.sale.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.carce.sale.service.CarUserService;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户API控制器
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserApiController {

    @Resource
    private CarUserService carUserService;

    /**
     * 检查手机号是否已注册
     */
    @GetMapping("/check-phone")
    public Map<String, Object> checkPhoneRegistered(@RequestParam String phoneNumber) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean registered = carUserService.isPhoneNumberRegistered(phoneNumber);
            response.put("success", true);
            response.put("registered", registered);
        } catch (Exception e) {
            log.error("检查手机号失败", e);
            response.put("success", false);
            response.put("registered", false);
            response.put("message", "檢查失敗");
        }
        
        return response;
    }
}

