package cc.carce.sale.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 自定义认证拦截器
 * 替代Sa-Token的功能，处理用户登录状态
 */
@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        
        // 检查用户是否已登录
        Object user = session.getAttribute("user");
        
        // 只在开发环境中自动设置登录状态
//        if (user == null && ("dev".equals(activeProfile) || "test".equals(activeProfile))) {
//            // 创建一个模拟用户对象
//            UserInfo testUser = new UserInfo();
//            testUser.setId(1L);
//            testUser.setUsername("test_user");
//            testUser.setNickname("测试用户");
//            testUser.setEmail("test@example.com");
//            testUser.setPhone("13800138000");
//            
//            // 将用户信息存储到session中
//            session.setAttribute("user", testUser);
//            
//            log.info("开发环境自动设置用户登录状态 - 用户ID: {}, 用户名: {}", testUser.getId(), testUser.getUsername());
//        }
        
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