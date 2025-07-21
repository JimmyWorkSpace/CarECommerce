package cc.carce.sale.controller;

import cc.carce.sale.config.AuthInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 测试控制器 - 用于验证登录状态
 */
@Controller
public class TestController {

    /**
     * 测试页面 - 显示当前登录状态
     */
    @GetMapping("/test")
    public String testPage(Model model, HttpServletRequest req) {
        HttpSession session = req.getSession();
        Object user = session.getAttribute("user");
        
        model.addAttribute("user", user);
        model.addAttribute("title", "测试页面 - 登录状态");
        model.addAttribute("description", "测试用户登录状态");
        model.addAttribute("url", req.getRequestURL().toString());
        model.addAttribute("image", "/img/swipper/slide1.jpg");
        model.addAttribute("content", "/test/index.ftl");
        
        return "/layout/main";
    }

    /**
     * 手动设置登录状态
     */
    @GetMapping("/test/login")
    public String testLogin(HttpServletRequest req) {
        HttpSession session = req.getSession();
        
        // 创建测试用户
        AuthInterceptor.UserInfo testUser = new AuthInterceptor.UserInfo();
        testUser.setId(999L);
        testUser.setUsername("manual_test_user");
        testUser.setNickname("手动测试用户");
        testUser.setEmail("manual@test.com");
        testUser.setPhone("13900139000");
        
        session.setAttribute("user", testUser);
        
        return "redirect:/test";
    }

    /**
     * 清除登录状态
     */
    @GetMapping("/test/logout")
    public String testLogout(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.removeAttribute("user");
        
        return "redirect:/test";
    }
} 