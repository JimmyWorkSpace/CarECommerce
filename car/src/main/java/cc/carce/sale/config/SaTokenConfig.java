//package cc.carce.sale.config;
//
//import cn.dev33.satoken.context.SaHolder;
//import cn.dev33.satoken.filter.SaServletFilter;
//import cn.dev33.satoken.interceptor.SaInterceptor;
//import cn.dev33.satoken.router.SaRouter;
//import cn.dev33.satoken.stp.StpUtil;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Sa-Token配置类
// * 配置Sa-Token使用Redis存储和拦截器
// */
//@Configuration
//public class SaTokenConfig implements WebMvcConfigurer {
//
//    /**
//     * 定义公开路径，这些路径不需要登录即可访问
//     */
//    private static final List<String> PUBLIC_PATHS = Arrays.asList(
//            // 公开页面
//            "/", "/home", "/buy-cars", "/mall", "/about",
//            "/login", "/register", "/logout",
//            
//            // 静态资源
//            "/css/**", "/js/**", "/img/**", "/assets/**", "/lib/**",
//            
//            // 公开API
//            "/api/banner/**", "/api/advertisement/**", "/api/cars/**", 
//            "/api/test/**",
//            "/api/sms/**", "/api/verify/**", "/api/captcha/**",
//            
//            // 其他
//            "/error", "/favicon.ico", "/swagger-ui/**", "/v2/api-docs/**"
//    );
//
//    /**
//     * 注册Sa-Token拦截器
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 注册Sa-Token拦截器，打开注解式鉴权功能
//        registry.addInterceptor(new SaInterceptor(handle -> {
//            // 指定拦截的路由
//            SaRouter.match("/**")
//                    // 排除不需要登录的路径
//                    .notMatch(PUBLIC_PATHS)
//                    .check(r -> {
//                        // 检查是否登录
//                        StpUtil.checkLogin();
//                    });
//        })).addPathPatterns("/**");
//    }
//
//    /**
//     * 注册Sa-Token过滤器
//     */
//    @Bean
//    public SaServletFilter getSaServletFilter() {
//        return new SaServletFilter()
//                .addInclude("/**")
//                .addExclude("/favicon.ico")
//                .setAuth(obj -> {
//                    // 设置认证函数
//                    SaRouter.match("/**")
//                            .notMatch(PUBLIC_PATHS)
//                            .check(r -> {
//                                // 检查是否登录
//                                StpUtil.checkLogin();
//                            });
//                })
//                .setError(e -> {
//                    // 设置错误处理函数
//                    SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
//                    SaHolder.getResponse().setStatus(401);
//                    return "{\"code\": 401, \"msg\": \"请先登录\"}";
//                });
//    }
//}
