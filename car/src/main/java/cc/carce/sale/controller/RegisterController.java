package cc.carce.sale.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cc.carce.sale.common.JwtUtils;
import cc.carce.sale.entity.CarUserEntity;
import cc.carce.sale.service.CarUserService;
import cc.carce.sale.service.SmsService;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 注册控制器
 */
@Controller
@Slf4j
public class RegisterController extends BaseController {

    @Resource
    private SmsService smsService;

    @Resource
    private CarUserService carUserService;
    
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 显示注册页面
     */
    @GetMapping("/register")
    public String registerPage(Model model, HttpServletRequest req) {
        // 获取错误信息参数
        String error = req.getParameter("error");
        if (error != null && !error.trim().isEmpty()) {
            model.addAttribute("error", error);
        }
        
        // 设置页面标题和描述
        model.addAttribute("title", "用戶註冊 - 二手車銷售平台");
        model.addAttribute("description", "用戶註冊頁面");
        model.addAttribute("url", req.getRequestURL().toString());
        model.addAttribute("image", "/img/swipper/slide1.jpg");
        
        // 设置模板内容
        model.addAttribute("content", "/register/index.ftl");
        
        return "/layout/main";
    }

    /**
     * 处理注册请求
     */
    @PostMapping("/register")
    public String register(@RequestParam String phoneNumber,
                          @RequestParam String smsCode,
                          @RequestParam String password,
                          @RequestParam String confirmPassword,
                          HttpSession session,
                          HttpServletResponse response) {
        
        try {
            // 验证手机号格式
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                return "redirect:/register?error=" + URLEncoder.encode("請輸入手機號碼", StandardCharsets.UTF_8.toString());
            }
            
            if (!phoneNumber.matches("^09\\d{8}$")) {
                return "redirect:/register?error=" + URLEncoder.encode("手機號碼格式不正確（必須為10碼數字，以09開頭）", StandardCharsets.UTF_8.toString());
            }
            
            // 验证验证码
            if (smsCode == null || smsCode.trim().isEmpty()) {
                return "redirect:/register?error=" + URLEncoder.encode("請輸入驗證碼", StandardCharsets.UTF_8.toString());
            }
            
            if (!smsService.verifySmsCode(phoneNumber, smsCode)) {
                return "redirect:/register?error=" + URLEncoder.encode("驗證碼錯誤或已過期", StandardCharsets.UTF_8.toString());
            }
            
            // 验证密码
            if (password == null || password.trim().isEmpty()) {
                return "redirect:/register?error=" + URLEncoder.encode("請輸入密碼", StandardCharsets.UTF_8.toString());
            }
            
            if (password.length() < 6) {
                return "redirect:/register?error=" + URLEncoder.encode("密碼長度至少6位", StandardCharsets.UTF_8.toString());
            }
            
            if (!password.equals(confirmPassword)) {
                return "redirect:/register?error=" + URLEncoder.encode("兩次輸入的密碼不一致", StandardCharsets.UTF_8.toString());
            }
            
            // 注册或修改密码
            CarUserEntity user = carUserService.registerOrUpdatePassword(phoneNumber.trim(), password);
            
            if (user == null) {
                return "redirect:/register?error=" + URLEncoder.encode("註冊失敗，請稍後再試", StandardCharsets.UTF_8.toString());
            }
            
            // 自动登录
            String token = JwtUtils.generateToken(user.getPhoneNumber());
            
            // 将用户信息存储到session中
            cc.carce.sale.config.AuthInterceptor.UserInfo sessionUser = new cc.carce.sale.config.AuthInterceptor.UserInfo();
            sessionUser.setId(user.getId());
            sessionUser.setUsername(user.getPhoneNumber());
            sessionUser.setNickname(user.getNickName());
            sessionUser.setName(user.getNickName());
            sessionUser.setEmail(user.getPhoneNumber() + "@example.com");
            sessionUser.setPhone(user.getPhoneNumber());
            sessionUser.setToken(token);
            session.setAttribute("user", sessionUser);
            
            // 使用ResponseCookie支持SameSite和Secure属性
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(60 * 60)
                    .sameSite("None")
                    .secure(true)
                    .build();
            
            response.addHeader("Set-Cookie", cookie.toString());
            
            redisTemplate.opsForValue().set("TOKEN:" + token, JSONUtil.toJsonStr(sessionUser), 1, TimeUnit.HOURS);
            
            boolean isRegistered = carUserService.isPhoneNumberRegistered(phoneNumber);
            if (isRegistered) {
                log.info("用户修改密码并自动登录成功: ID={}, 手机号={}", user.getId(), user.getPhoneNumber());
            } else {
                log.info("用户注册并自动登录成功: ID={}, 手机号={}", user.getId(), user.getPhoneNumber());
            }
            
            // 重定向到首页
            return "redirect:/";
            
        } catch (Exception e) {
            log.error("注册失败", e);
            try {
                String errorMsg = URLEncoder.encode("註冊失敗：" + e.getMessage(), StandardCharsets.UTF_8.toString());
                return "redirect:/register?error=" + errorMsg;
            } catch (UnsupportedEncodingException ex) {
                log.error("编码错误信息失败", ex);
                return "redirect:/register?error=註冊失敗";
            }
        }
    }
}

