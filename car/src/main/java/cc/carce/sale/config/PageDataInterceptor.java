package cc.carce.sale.config;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cc.carce.sale.service.CarConfigService;
import cc.carce.sale.service.CarMenuService;
import lombok.extern.slf4j.Slf4j;

/**
 * 页面数据拦截器
 * 自动为所有页面添加菜单数据和页脚数据
 */
@Component
@Slf4j
public class PageDataInterceptor implements HandlerInterceptor {

    @Resource
    private CarMenuService carMenuService;
    
    @Resource
    private CarConfigService carConfigService;

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, 
                          @NonNull Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        
        // 只处理返回视图的请求（非REST API）
        if (modelAndView != null && !isRestApiRequest(request)) {
            try {
                // 添加菜单数据
                addMenuData(modelAndView);
                
                // 添加页脚数据
                addFooterData(modelAndView);
                
                // 添加OG标签相关数据
                addOgData(modelAndView, request);
                
                log.debug("页面数据拦截器已为请求 {} 添加菜单、页脚和OG标签数据", request.getRequestURI());
            } catch (Exception e) {
                log.error("页面数据拦截器处理失败", e);
                // 即使失败也不影响页面正常显示，只是没有菜单和页脚数据
            }
        }
    }

    /**
     * 判断是否为REST API请求
     */
    private boolean isRestApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        
        // 如果URI包含/api/，则认为是REST API
        if (uri.contains("/api/")) {
            return true;
        }
        
        // 如果请求方法不是GET，且返回的是JSON，则认为是REST API
        if (!"GET".equals(method)) {
            String accept = request.getHeader("Accept");
            if (accept != null && accept.contains("application/json")) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 添加菜单数据到ModelAndView
     */
    private void addMenuData(ModelAndView modelAndView) {
        try {
            modelAndView.addObject("menus", carMenuService.getVisibleMenus());
        } catch (Exception e) {
            log.error("获取菜单数据失败", e);
            // 如果获取菜单失败，设置空列表避免模板报错
            modelAndView.addObject("menus", new ArrayList<>());
        }
    }

    /**
     * 添加页脚数据到ModelAndView
     */
    private void addFooterData(ModelAndView modelAndView) {
        try {
            // 添加网站配置数据到页脚
            modelAndView.addObject("config", carConfigService.getAllConfigs());
            
            // 添加页脚链接配置Map，方便模板通过code获取value
            java.util.Map<String, String> footerLinks = new java.util.HashMap<>();
            try {
                java.util.List<cc.carce.sale.entity.CarConfigEntity> configs = carConfigService.getAllConfigs();
                for (cc.carce.sale.entity.CarConfigEntity config : configs) {
                    // 只添加页脚相关的配置（code以footer_开头）
                    if (config.getCode() != null && config.getCode().startsWith("footer_")) {
                        footerLinks.put(config.getCode(), config.getValue() != null ? config.getValue() : "#");
                    }
                }
            } catch (Exception e) {
                log.warn("获取页脚链接配置失败", e);
            }
            modelAndView.addObject("footerLinks", footerLinks);
            
            // 添加配置内容对象（用于模板中的configContent）
            try {
                cc.carce.sale.entity.dto.CarConfigContent configContent = carConfigService.getConfigContent();
                modelAndView.addObject("configContent", configContent);
            } catch (Exception e) {
                log.warn("获取配置内容失败，使用默认配置", e);
                // 设置默认配置内容
                cc.carce.sale.entity.dto.CarConfigContent defaultConfig = new cc.carce.sale.entity.dto.CarConfigContent();
                defaultConfig.setKefu("400-123-4567");
                defaultConfig.setYouxiang("service@carce.cc");
                defaultConfig.setDizhi("台北市信義區信義路五段7號");
                defaultConfig.setFwsj1("週一至週五：9:00-18:00");
                defaultConfig.setFwsj2("週六至週日：10:00-17:00");
                defaultConfig.setFwsj3("節假日：10:00-16:00");
                modelAndView.addObject("configContent", defaultConfig);
            }
        } catch (Exception e) {
            log.error("获取页脚配置数据失败", e);
            // 如果获取配置失败，设置空对象避免模板报错
            modelAndView.addObject("config", new java.util.HashMap<>());
            modelAndView.addObject("footerLinks", new java.util.HashMap<>());
        }
    }

    /**
     * 添加OG标签相关数据到ModelAndView
     */
    private void addOgData(ModelAndView modelAndView, HttpServletRequest request) {
        try {
            // 构建基础URL，参考RobotsController的方法
            String scheme = request.getScheme();
            String serverName = request.getServerName();
            int serverPort = request.getServerPort();
            
            String baseUrl = "https://" + serverName;
            if ((scheme.equals("http") && serverPort != 80) || 
                (scheme.equals("https") && serverPort != 443)) {
                baseUrl += ":" + serverPort;
            }
            
            // 添加baseUrl变量，供所有页面使用
            modelAndView.addObject("baseUrl", baseUrl);
            
            // 添加当前页面完整URL
            String currentUrl = baseUrl + request.getRequestURI();
            if (request.getQueryString() != null) {
                currentUrl += "?" + request.getQueryString();
            }
            
            // 添加title变量，如果不存在则使用默认值
            if (!modelAndView.getModel().containsKey("title")) {
                modelAndView.addObject("title", "車勢汽車交易網-最保障消費者的一站式買賣二手車平台");
            }

            if (!modelAndView.getModel().containsKey("ogTitle")) {
                modelAndView.addObject("ogTitle", "車勢汽車交易網-最保障消費者的一站式買賣二手車平台");
            }
            
            // 添加description变量，如果不存在则使用默认值
            if (!modelAndView.getModel().containsKey("ogDescription")) {
                modelAndView.addObject("ogDescription", "車勢汽車交易網-最保障消費者的一站式買賣二手車平台");
            }
            
            // 添加url变量，如果不存在则使用当前请求URL
            if (!modelAndView.getModel().containsKey("ogUrl")) {
                modelAndView.addObject("ogUrl", currentUrl);
            }
            
            // 添加image变量，如果不存在则使用默认图片
            if (!modelAndView.getModel().containsKey("ogImage")) {
                modelAndView.addObject("ogImage", "https://testcloud.carce.cc/img/car_sale/banner/61a9d426-c81e-4e84-89ec-8ce53b793be4.jpg");
            }
            
            log.debug("已添加OG标签相关数据和baseUrl到ModelAndView");
        } catch (Exception e) {
            log.error("添加OG标签数据失败", e);
        }
    }
}
