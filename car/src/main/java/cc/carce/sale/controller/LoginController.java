package cc.carce.sale.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cc.carce.sale.config.AuthInterceptor;

/**
 * 登录控制器
 */
@Controller
public class LoginController {

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
        model.addAttribute("title", "登录/注册 - 二手车销售平台");
        model.addAttribute("description", "用户登录和注册页面");
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
    public String login(@RequestParam String username, 
                       @RequestParam String password,
                       @RequestParam(required = false) String returnUrl,
                       HttpSession session,
                       Model model) {
        
        // 简单的测试登录逻辑 - 任何用户名和密码都可以登录
        if (username != null && !username.trim().isEmpty()) {
            // 创建用户信息
            AuthInterceptor.UserInfo user = new AuthInterceptor.UserInfo();
            user.setId(1L);
            user.setUsername(username);
            user.setNickname(username);
            user.setName(username); // 添加name字段
            user.setEmail(username + "@example.com");
            user.setPhone("13800138000");
            
            // 将用户信息存储到session中
            session.setAttribute("user", user);
            
            // 如果有返回URL且是GET请求的页面，则跳转回去
            if (returnUrl != null && !returnUrl.trim().isEmpty() && isValidReturnUrl(returnUrl)) {
                return "redirect:" + returnUrl;
            }
            
            // 重定向到首页
            return "redirect:/";
        }
        
        // 登录失败，返回登录页面
        model.addAttribute("error", "登录失败，请检查用户名和密码");
        model.addAttribute("title", "登录/注册 - 二手车销售平台");
        model.addAttribute("description", "用户登录和注册页面");
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
        // 清除session中的用户信息
        session.removeAttribute("user");
        
        // 重定向到首页
        return "redirect:/";
    }
} 