package cc.carce.sale.config;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import cc.carce.sale.common.JwtUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义认证拦截器
 * 兼容Sa-Token和Session的用户登录状态
 */
@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	List<Cookie> cookies = CollUtil.newArrayList(request.getCookies());
    	Cookie tokenCookie = cookies.stream().filter(c -> c.getName().equals("token")).findFirst().orElse(null);
        if (tokenCookie != null && JwtUtils.verify(tokenCookie.getValue())) {
        	String userJson = redisTemplate.opsForValue().get("TOKEN:" + tokenCookie.getValue());
        	UserInfo sessionUser = JSONUtil.toBean(userJson, UserInfo.class);
        	request.getSession().setAttribute("user", sessionUser);
        	request.setAttribute("sessionUser", sessionUser);
        }
        return true;
    }
    
    /**
     * 用户信息类
     */
    @Data
    public static class UserInfo {
        private Long id;
        private String username;
        private String nickname;
        private String name;
        private String email;
        private String phone;
        private String token;
    }
} 