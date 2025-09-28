package cc.carce.sale.controller;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cc.carce.sale.config.AuthInterceptor.UserInfo;
import cc.carce.sale.service.CarMenuService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BaseController {

	@Resource
	private CarMenuService carMenuService;

	protected boolean isLogin() {
		ServletRequestAttributes attrs =
	            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs != null) {
			return attrs.getRequest().getSession().getAttribute("user") != null;
		}
		return false;
	}
	
	protected UserInfo getSessionUser() {
		ServletRequestAttributes attrs =
	            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs != null) {
			Object userJson = attrs.getRequest().getSession().getAttribute("user");
			if(userJson != null) {
				try {
					return (UserInfo) userJson;
				}catch(Exception ex) {
					return null;
				}
			}
		}
		return null;
	}

	protected Long getCurrentUserId(){
		UserInfo sessionUser = getSessionUser();
		if(sessionUser != null) {
			return sessionUser.getId();
		}
		return null;
	}
	
	/**
	 * 添加菜单数据到Model
	 * @param model Model对象
	 */
	protected void addMenuData(Model model) {
		try {
			model.addAttribute("menus", carMenuService.getVisibleMenus());
		} catch (Exception e) {
			log.error("获取菜单数据失败", e);
			// 如果获取菜单失败，设置空列表避免模板报错
			model.addAttribute("menus", new ArrayList<>());
		}
	}
}
