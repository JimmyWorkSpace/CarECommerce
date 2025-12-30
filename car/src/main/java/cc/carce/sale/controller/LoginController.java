package cc.carce.sale.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.http.ResponseCookie;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cc.carce.sale.common.JwtUtils;
import cc.carce.sale.config.AuthInterceptor.UserInfo;
import cc.carce.sale.entity.CarUserEntity;
import cc.carce.sale.service.CarUserService;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录控制器
 */
@Controller
@Slf4j
public class LoginController extends BaseController {

    @Resource
    private CarUserService carUserService;
    
    @Resource
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 处理登录请求（密码登录）
     */
    @PostMapping("/login")
    public String login(@RequestParam String phoneNumber, 
                       @RequestParam String password,
                       @RequestParam(required = false) String returnUrl,
                       HttpSession session,
                       HttpServletResponse response) {
        
        try {
            // 验证手机号格式
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                String errorMsg = URLEncoder.encode("請輸入手機號碼", StandardCharsets.UTF_8.toString());
                String redirectUrl = "/login?error=" + errorMsg;
                if (returnUrl != null && !returnUrl.trim().isEmpty()) {
                    String cleanedReturnUrl = cleanReturnUrl(returnUrl);
                    redirectUrl += "&returnUrl=" + URLEncoder.encode(cleanedReturnUrl, StandardCharsets.UTF_8.toString());
                }
                return "redirect:" + redirectUrl;
            }
            
            // 验证密码
            if (password == null || password.trim().isEmpty()) {
                String errorMsg = URLEncoder.encode("請輸入密碼", StandardCharsets.UTF_8.toString());
                String redirectUrl = "/login?error=" + errorMsg;
                if (returnUrl != null && !returnUrl.trim().isEmpty()) {
                    String cleanedReturnUrl = cleanReturnUrl(returnUrl);
                    redirectUrl += "&returnUrl=" + URLEncoder.encode(cleanedReturnUrl, StandardCharsets.UTF_8.toString());
                }
                return "redirect:" + redirectUrl;
            }
            
            // 使用密码登录
            CarUserEntity user = carUserService.loginWithPassword(phoneNumber.trim(), password);
            
            if (user == null) {
                String errorMsg = URLEncoder.encode("手機號碼或密碼錯誤", StandardCharsets.UTF_8.toString());
                String redirectUrl = "/login?error=" + errorMsg;
                if (returnUrl != null && !returnUrl.trim().isEmpty()) {
                    String cleanedReturnUrl = cleanReturnUrl(returnUrl);
                    redirectUrl += "&returnUrl=" + URLEncoder.encode(cleanedReturnUrl, StandardCharsets.UTF_8.toString());
                }
                return "redirect:" + redirectUrl;
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
            
            // 使用ResponseCookie支持SameSite和Secure属性
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)           // 防止前端JS读取（安全）
                    .path("/")                // 整个网站都有效
                    .maxAge(60 * 60)          // 1小时有效
                    .sameSite("None")         // 允许跨站请求
                    .secure(true)             // 需要HTTPS（SameSite=None必需）
                    .build();
            
            response.addHeader("Set-Cookie", cookie.toString());
            
            redisTemplate.opsForValue().set("TOKEN:" + token, JSONUtil.toJsonStr(sessionUser), 1, TimeUnit.HOURS);
            log.info("用户登录成功: ID={}, 手机号={}", user.getId(), user.getPhoneNumber());
            
            // 如果有返回URL且是GET请求的页面，则跳转回去
            if (returnUrl != null && !returnUrl.trim().isEmpty() && isValidReturnUrl(returnUrl)) {
                // 清理URL中的不需要的参数
                String cleanedUrl = cleanReturnUrl(returnUrl);
                return "redirect:" + cleanedUrl;
            }
            
            // 重定向到首页
            return "redirect:/";
            
        } catch (Exception e) {
            log.error("登录失败", e);
            try {
                String errorMsg = URLEncoder.encode("登入失敗：" + e.getMessage(), StandardCharsets.UTF_8.toString());
                // 如果有returnUrl，清理后附加到跳转URL
                String redirectUrl = "/login?error=" + errorMsg;
                if (returnUrl != null && !returnUrl.trim().isEmpty()) {
                    String cleanedReturnUrl = cleanReturnUrl(returnUrl);
                    redirectUrl += "&returnUrl=" + URLEncoder.encode(cleanedReturnUrl, StandardCharsets.UTF_8.toString());
                }
                return "redirect:" + redirectUrl;
            } catch (UnsupportedEncodingException ex) {
                log.error("编码错误信息失败", ex);
                String redirectUrl = "/login?error=登入失敗";
                if (returnUrl != null && !returnUrl.trim().isEmpty()) {
                    try {
                        String cleanedReturnUrl = cleanReturnUrl(returnUrl);
                        redirectUrl += "&returnUrl=" + URLEncoder.encode(cleanedReturnUrl, StandardCharsets.UTF_8.toString());
                    } catch (UnsupportedEncodingException e2) {
                        // 忽略编码错误
                    }
                }
                return "redirect:" + redirectUrl;
            }
        }
    }
    
    /**
     * 清理URL中的不需要的参数
     * 移除 error, baseUrl, title, ogTitle, ogDescription, ogUrl, ogImage 等参数
     */
    private String cleanReturnUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return url;
        }
        
        try {
            // 分离路径和查询参数
            String path;
            String queryString = null;
            int queryIndex = url.indexOf('?');
            
            if (queryIndex >= 0) {
                path = url.substring(0, queryIndex);
                queryString = url.substring(queryIndex + 1);
            } else {
                path = url;
            }
            
            // 如果没有查询参数，直接返回路径
            if (queryString == null || queryString.trim().isEmpty()) {
                return path;
            }
            
            // 需要移除的参数名
            String[] paramsToRemove = {
                "error", "baseUrl", "title", "ogTitle", 
                "ogDescription", "ogUrl", "ogImage"
            };
            
            // 解析查询参数
            Map<String, String> params = new LinkedHashMap<>();
            String[] pairs = queryString.split("&");
            
            for (String pair : pairs) {
                if (pair.trim().isEmpty()) {
                    continue;
                }
                
                int equalIndex = pair.indexOf('=');
                String key;
                String value = "";
                
                if (equalIndex >= 0) {
                    key = pair.substring(0, equalIndex);
                    if (equalIndex < pair.length() - 1) {
                        value = pair.substring(equalIndex + 1);
                    }
                } else {
                    key = pair;
                }
                
                // URL解码
                try {
                    key = URLDecoder.decode(key, StandardCharsets.UTF_8.toString());
                    if (!value.isEmpty()) {
                        value = URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
                    }
                } catch (UnsupportedEncodingException e) {
                    // 如果解码失败，使用原始值
                }
                
                // 检查是否需要保留此参数
                boolean shouldKeep = true;
                for (String paramToRemove : paramsToRemove) {
                    if (key.equals(paramToRemove)) {
                        shouldKeep = false;
                        break;
                    }
                }
                
                if (shouldKeep) {
                    params.put(key, value);
                }
            }
            
            // 重新构建URL
            if (params.isEmpty()) {
                return path;
            }
            
            StringBuilder newQueryString = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (!first) {
                    newQueryString.append("&");
                }
                first = false;
                
                try {
                    String encodedKey = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString());
                    String encodedValue = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString());
                    newQueryString.append(encodedKey).append("=").append(encodedValue);
                } catch (UnsupportedEncodingException e) {
                    // 如果编码失败，使用原始值
                    newQueryString.append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
            
            return path + "?" + newQueryString.toString();
            
        } catch (Exception e) {
            log.warn("清理URL参数失败: {}", url, e);
            // 如果清理失败，返回原始URL（但至少移除查询参数部分）
            int queryIndex = url.indexOf('?');
            return queryIndex >= 0 ? url.substring(0, queryIndex) : url;
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
    public String logout(HttpSession session, HttpServletResponse response) {
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
        
        // 清除Cookie中的token
        ResponseCookie tokenCookie = ResponseCookie.from("token", "")
                .path("/")
                .maxAge(0)              // 设置为0立即删除
                .httpOnly(true)
                .sameSite("None")       // 保持与设置时一致
                .secure(true)           // 保持与设置时一致
                .build();
        
        response.addHeader("Set-Cookie", tokenCookie.toString());
        
        log.info("用户登出成功");
        
        // 重定向到首页
        return "redirect:/";
    }
} 