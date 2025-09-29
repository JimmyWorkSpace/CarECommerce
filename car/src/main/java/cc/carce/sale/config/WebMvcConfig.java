package cc.carce.sale.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private PageDataInterceptor pageDataInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册页面数据拦截器
        registry.addInterceptor(pageDataInterceptor)
                .addPathPatterns("/**")  // 拦截所有请求
                .excludePathPatterns(
                    "/api/**",           // 排除所有API请求
                    "/static/**",        // 排除静态资源
                    "/assets/**",        // 排除静态资源
                    "/css/**",           // 排除CSS文件
                    "/js/**",            // 排除JS文件
                    "/img/**",           // 排除图片文件
                    "/favicon.ico",      // 排除网站图标
                    "/error"             // 排除错误页面
                );
    }
}
