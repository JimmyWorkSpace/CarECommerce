package cc.carce.sale.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Web配置类 - 注册拦截器和静态资源
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private AuthInterceptor authInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册认证拦截器，应用到所有请求
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/assets/**", "/css/**", "/js/**", "/img/**", "/favicon.ico", "/static/**");
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源映射
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        
        // 确保PDF文件可以正确访问
        registry.addResourceHandler("/static/*.pdf")
                .addResourceLocations("classpath:/static/");
    }
} 