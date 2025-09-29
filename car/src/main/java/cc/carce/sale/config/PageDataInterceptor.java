package cc.carce.sale.config;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public void postHandle(HttpServletRequest request, HttpServletResponse response, 
                          Object handler, ModelAndView modelAndView) throws Exception {
        
        // 只处理返回视图的请求（非REST API）
        if (modelAndView != null && !isRestApiRequest(request)) {
            try {
                // 添加菜单数据
                addMenuData(modelAndView);
                
                // 添加页脚数据
                addFooterData(modelAndView);
                
                log.debug("页面数据拦截器已为请求 {} 添加菜单和页脚数据", request.getRequestURI());
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
        }
    }
}
