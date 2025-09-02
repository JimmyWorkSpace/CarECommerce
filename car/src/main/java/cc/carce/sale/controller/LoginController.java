package cc.carce.sale.controller;

import cc.carce.sale.entity.CarUserEntity;
import cc.carce.sale.service.CarUserService;
import cc.carce.sale.service.SmsService;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 登录控制器
 */
@Controller
@Slf4j
public class LoginController {

    @Resource
    private SmsService smsService;

    @Resource
    private CarUserService carUserService;

    /**
     * 显示登录页面
     */
    @GetMapping("/login")
    public String loginPage(Model model, HttpServletRequest req) {
        // 获取返回URL参数
        String returnUrl = req.getParameter("returnUrl");
        if (returnUrl != null && !returnUrl.trim().isEmpty()) {
            model.addAttribute("returnUrl", returnUrl);
        }
        
        // 设置页面标题和描述
        model.addAttribute("title", "登入/註冊 - 二手車銷售平台");
        model.addAttribute("description", "用戶登入和註冊頁面");
        model.addAttribute("url", req.getRequestURL().toString());
        model.addAttribute("image", "/img/swipper/slide1.jpg");
        
        // 设置模板内容
        model.addAttribute("content", "/login/index.ftl");
        
        return "/layout/main";
    }

    /**
     * 处理登录请求
     */
    @PostMapping("/login")
    public String login(@RequestParam String phoneNumber, 
                       @RequestParam String smsCode,
                       @RequestParam(required = false) String returnUrl,
                       HttpSession session,
                       Model model) {
        
        try {
//            // 验证手机号格式
//            if (phoneNumber == null || !phoneNumber.matches("^1[3-9]\\d{9}$")) {
//                model.addAttribute("error", "手機號碼格式不正確");
//                return getLoginPage(model);
//            }
            
            // 验证验证码格式
            if (smsCode == null || !smsCode.matches("\\d{6}")) {
                model.addAttribute("error", "驗證碼格式不正確");
                return getLoginPage(model);
            }
            
            // 验证短信验证码
            if (!smsService.verifySmsCode(phoneNumber, smsCode)) {
                model.addAttribute("error", "驗證碼錯誤或已過期");
                return getLoginPage(model);
            }
            
            // 用户登录或注册
            CarUserEntity user = carUserService.loginOrRegister(phoneNumber);
            
            if (user == null) {
                model.addAttribute("error", "用戶創建失敗");
                return getLoginPage(model);
            }
            
            // 使用Sa-Token进行登录
            StpUtil.login(user.getId());
            
            // 将用户信息存储到session中（兼容现有代码）
            cc.carce.sale.config.AuthInterceptor.UserInfo sessionUser = new cc.carce.sale.config.AuthInterceptor.UserInfo();
            sessionUser.setId(user.getId());
            sessionUser.setUsername(user.getPhoneNumber());
            sessionUser.setNickname(user.getNickName());
            sessionUser.setName(user.getNickName());
            sessionUser.setEmail(user.getPhoneNumber() + "@example.com");
            sessionUser.setPhone(user.getPhoneNumber());
            
            session.setAttribute("user", sessionUser);
            
            log.info("用户登录成功: ID={}, 手机号={}", user.getId(), user.getPhoneNumber());
            
            // 如果有返回URL且是GET请求的页面，则跳转回去
            if (returnUrl != null && !returnUrl.trim().isEmpty() && isValidReturnUrl(returnUrl)) {
                return "redirect:" + returnUrl;
            }
            
            // 重定向到首页
            return "redirect:/";
            
        } catch (Exception e) {
            log.error("登录失败", e);
            model.addAttribute("error", "登入失敗：" + e.getMessage());
            return getLoginPage(model);
        }
    }
    
    /**
     * 获取登录页面
     */
    private String getLoginPage(Model model) {
        model.addAttribute("title", "登入/註冊 - 二手車銷售平台");
        model.addAttribute("description", "用戶登入和註冊頁面");
        model.addAttribute("content", "/login/index.ftl");
        return "/layout/main";
    }

    /**
     * 验证返回URL是否有效
     * 只允许GET请求的页面，防止CSRF攻击
     */
    private boolean isValidReturnUrl(String returnUrl) {
        if (returnUrl == null || returnUrl.trim().isEmpty()) {
            return false;
        }
        
        // 检查URL格式
        if (!returnUrl.startsWith("/")) {
            return false;
        }
        
        // 排除一些敏感路径
        String[] forbiddenPaths = {
            "/login", "/logout", "/admin", "/api"
        };
        
        for (String forbiddenPath : forbiddenPaths) {
            if (returnUrl.startsWith(forbiddenPath)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 处理登出请求
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 使用Sa-Token登出
        StpUtil.logout();
        
        // 清除session中的用户信息
        session.removeAttribute("user");
        
        log.info("用户登出成功");
        
        // 重定向到首页
        return "redirect:/";
    }
} 