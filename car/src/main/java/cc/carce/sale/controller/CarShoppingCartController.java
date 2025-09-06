package cc.carce.sale.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.carce.sale.dto.CarShoppingCartDto;
import cc.carce.sale.entity.CarShoppingCartEntity;
import cc.carce.sale.service.CarShoppingCartService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/shopping-cart")
@Slf4j
public class CarShoppingCartController {

	@Resource
	private CarShoppingCartService carShoppingCartService;
	
	/**
	 * 获取用户购物车列表
	 */
	@GetMapping("/list")
	public Map<String, Object> getUserCart(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 从session获取用户ID
			HttpSession session = request.getSession();
			Object user = session.getAttribute("user");
			
			if (user == null) {
				result.put("success", false);
				result.put("message", "用户未登录");
				return result;
			}
			
			// 这里需要根据实际的用户对象结构获取用户ID
			// 假设用户对象有getId()方法
			Long userId = getUserIdFromUser(user);
			if (userId == null) {
				result.put("success", false);
				result.put("message", "无法获取用户ID");
				return result;
			}
			
			List<CarShoppingCartDto> cartItems = carShoppingCartService.getUserCartDetails(userId);
			result.put("success", true);
			result.put("data", cartItems);
			result.put("message", "获取购物车成功");
			
		} catch (Exception e) {
			log.error("获取购物车失败", e);
			result.put("success", false);
			result.put("message", "获取购物车失败：" + e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 添加商品到购物车
	 */
	@PostMapping("/add")
	public Map<String, Object> addToCart(@RequestBody CarShoppingCartEntity cartItem, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 从session获取用户ID
			HttpSession session = request.getSession();
			Object user = session.getAttribute("user");
			
			if (user == null) {
				result.put("success", false);
				result.put("message", "用户未登录");
				return result;
			}
			
			Long userId = getUserIdFromUser(user);
			if (userId == null) {
				result.put("success", false);
				result.put("message", "无法获取用户ID");
				return result;
			}
			
			cartItem.setUserId(userId);
			
			boolean success = carShoppingCartService.addToCart(cartItem);
			if (success) {
				result.put("success", true);
				result.put("message", "添加到购物车成功");
			} else {
				result.put("success", false);
				result.put("message", "添加到购物车失败");
			}
			
		} catch (Exception e) {
			log.error("添加购物车失败", e);
			result.put("success", false);
			result.put("message", "添加购物车失败：" + e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 更新购物车商品数量
	 */
	@PutMapping("/update/{id}")
	public Map<String, Object> updateQuantity(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			Integer quantity = request.get("quantity");
			if (quantity == null) {
				result.put("success", false);
				result.put("message", "数量不能为空");
				return result;
			}
			
			boolean success = carShoppingCartService.updateQuantity(id, quantity);
			if (success) {
				result.put("success", true);
				result.put("message", "更新数量成功");
			} else {
				result.put("success", false);
				result.put("message", "更新数量失败");
			}
			
		} catch (Exception e) {
			log.error("更新购物车数量失败", e);
			result.put("success", false);
			result.put("message", "更新数量失败：" + e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 从购物车移除商品
	 */
	@DeleteMapping("/remove/{id}")
	public Map<String, Object> removeFromCart(@PathVariable Long id) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			boolean success = carShoppingCartService.removeFromCart(id);
			if (success) {
				result.put("success", true);
				result.put("message", "移除商品成功");
			} else {
				result.put("success", false);
				result.put("message", "移除商品失败");
			}
			
		} catch (Exception e) {
			log.error("移除购物车商品失败", e);
			result.put("success", false);
			result.put("message", "移除商品失败：" + e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 清空用户购物车
	 */
	@DeleteMapping("/clear")
	public Map<String, Object> clearCart(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		
		try {
			// 从session获取用户ID
			HttpSession session = request.getSession();
			Object user = session.getAttribute("user");
			
			if (user == null) {
				result.put("success", false);
				result.put("message", "用户未登录");
				return result;
			}
			
			Long userId = getUserIdFromUser(user);
			if (userId == null) {
				result.put("success", false);
				result.put("message", "无法获取用户ID");
				return result;
			}
			
			boolean success = carShoppingCartService.clearUserCart(userId);
			if (success) {
				result.put("success", true);
				result.put("message", "清空购物车成功");
			} else {
				result.put("success", false);
				result.put("message", "清空购物车失败");
			}
			
		} catch (Exception e) {
			log.error("清空购物车失败", e);
			result.put("success", false);
			result.put("message", "清空购物车失败：" + e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 从用户对象中获取用户ID的辅助方法
	 * 需要根据实际的用户对象结构进行调整
	 */
	private Long getUserIdFromUser(Object user) {
		try {
			// 检查是否是AuthInterceptor.UserInfo类型
			if (user instanceof cc.carce.sale.config.AuthInterceptor.UserInfo) {
				cc.carce.sale.config.AuthInterceptor.UserInfo userInfo = (cc.carce.sale.config.AuthInterceptor.UserInfo) user;
				return userInfo.getId();
			}
			
			// 检查是否是CarUserEntity类型
			if (user instanceof cc.carce.sale.entity.CarUserEntity) {
				cc.carce.sale.entity.CarUserEntity carUser = (cc.carce.sale.entity.CarUserEntity) user;
				return carUser.getId();
			}
			
			// 使用反射作为后备方案
			if (user != null && user.getClass().getMethod("getId") != null) {
				return (Long) user.getClass().getMethod("getId").invoke(user);
			}
		} catch (Exception e) {
			log.error("获取用户ID失败", e);
		}
		return null;
	}
}
