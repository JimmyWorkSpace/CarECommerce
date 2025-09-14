package cc.carce.sale.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cc.carce.sale.config.AuthInterceptor.UserInfo;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BaseController {

	protected boolean isLogin() {
		ServletRequestAttributes attrs =
	            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attrs.getRequest().getSession().getAttribute("user") != null;
	}
	
	protected UserInfo getSessionUser() {
		ServletRequestAttributes attrs =
	            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		Object userJson = attrs.getRequest().getSession().getAttribute("user");
		if(userJson != null) {
			try {
				return (UserInfo) userJson;
			}catch(Exception ex) {
				return null;
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
}
