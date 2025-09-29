package cc.carce.sale.controller;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cc.carce.sale.common.JwtUtils;
import cc.carce.sale.config.AuthInterceptor.UserInfo;
import cc.carce.sale.entity.CarUserEntity;
import cc.carce.sale.service.CarUserService;
import cc.carce.sale.service.SmsService;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录控制器
 */
@Controller
@Slf4j
public class LoginController extends BaseController {

    @Resource
    private SmsService smsService;

    @Resource
    private CarUserService carUserService;
    
    @Resource
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 处理登录请求
     */
    @PostMapping("/login")
    public String login(@RequestParam String phoneNumber, 
                       @RequestParam String smsCode,
                       @RequestParam(required = false) String returnUrl,
                       HttpSession session,
                       HttpServletResponse response) {
        
        try {
//            // 验证手机号格式
//            if (phoneNumber == null || !phoneNumber.matches("^1[3-9]\\d{9}$")) {
//                model.addAttribute("error", "手機號碼格式不正確");
//                return getLoginPage(model);
//            }
            
            // // 验证验证码格式
            // if (smsCode == null || !smsCode.matches("\\d{6}")) {
            //     return "redirect:/login?error=驗證碼格式不正確";
            // }
            
            // 验证短信验证码
            if (!smsService.verifySmsCode(phoneNumber, smsCode)) {
                return "redirect:/login?error=驗證碼錯誤或已過期";
            }
            
            // 用户登录或注册
            CarUserEntity user = carUserService.loginOrRegister(phoneNumber);
            
            if (user == null) {
                return "redirect:/login?error=用戶創建失敗";
            }
            
            String token = JwtUtils.generateToken(user.getPhoneNumber());
            // 保存到 Redis，过期时间与 JWT 保持一致（1小时）
            
            // 将用户信息存储到session中（兼容现有代码）
            cc.carce.sale.config.AuthInterceptor.UserInfo sessionUser = new cc.carce.sale.config.AuthInterceptor.UserInfo();
            sessionUser.setId(user.getId());
            sessionUser.setUsername(user.getPhoneNumber());
            sessionUser.setNickname(user.getNickName());
            sessionUser.setName(user.getNickName());
            sessionUser.setEmail(user.getPhoneNumber() + "@example.com");
            sessionUser.setPhone(user.getPhoneNumber());
            sessionUser.setToken(token);
            session.setAttribute("user", sessionUser);
            
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);   // 防止前端JS读取（安全）
            cookie.setPath("/");        // 整个网站都有效
            cookie.setMaxAge(60 * 60);  // 1小时有效
            response.addCookie(cookie);
            
            redisTemplate.opsForValue().set("TOKEN:" + token, JSONUtil.toJsonStr(sessionUser), 1, TimeUnit.HOURS);
            log.info("用户登录成功: ID={}, 手机号={}", user.getId(), user.getPhoneNumber());
            
            // 如果有返回URL且是GET请求的页面，则跳转回去
            if (returnUrl != null && !returnUrl.trim().isEmpty() && isValidReturnUrl(returnUrl)) {
                return "redirect:" + returnUrl;
            }
            
            // 重定向到首页
            return "redirect:/";
            
        } catch (Exception e) {
            log.error("登录失败", e);
            return "redirect:/login?error=登入失敗：" + e.getMessage();
        }
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
        if(session.getAttribute("user") != null) {
        	try {
        		UserInfo sessionUser = JSONUtil.toBean(session.getAttribute("user").toString(), UserInfo.class);
        		redisTemplate.delete("TOKEN:" + sessionUser.getToken());
        	}catch(Exception ex) {
        		log.error("从session中读取用户信息失败", ex);
        	}
        }
        
        // 清除session中的用户信息
        session.removeAttribute("user");
        
        log.info("用户登出成功");
        
        // 重定向到首页
        return "redirect:/";
    }
} 