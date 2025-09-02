package cc.carce.sale.config;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 自定义认证拦截器
 * 兼容Sa-Token和Session的用户登录状态
 */
@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        
        // 检查用户是否已登录（优先检查Sa-Token）
        boolean isLoggedIn = StpUtil.isLogin();
        Object user = session.getAttribute("user");
        
        // 如果Sa-Token显示已登录但session中没有用户信息，尝试从Sa-Token获取
        if (isLoggedIn && user == null) {
            try {
                Object loginId = StpUtil.getLoginId();
                if (loginId != null) {
                    log.info("Sa-Token检测到用户已登录，登录ID: {}", loginId);
                }
            } catch (Exception e) {
                log.warn("Sa-Token状态异常", e);
            }
        }
        
        return true;
    }
    
    /**
     * 用户信息类
     */
    public static class UserInfo {
        private Long id;
        private String username;
        private String nickname;
        private String name;
        private String email;
        private String phone;
        
        // Getters and Setters
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getNickname() {
            return nickname;
        }
        
        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getPhone() {
            return phone;
        }
        
        public void setPhone(String phone) {
            this.phone = phone;
        }
        
        @Override
        public String toString() {
            return "UserInfo{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", phone='" + phone + '\'' +
                    '}';
        }
    }
} 