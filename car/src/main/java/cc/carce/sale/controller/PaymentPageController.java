package cc.carce.sale.controller;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cc.carce.sale.config.AuthInterceptor.UserInfo;
import cc.carce.sale.form.PaymentRequestForm;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付页面控制器
 */
@Slf4j
@Controller
@RequestMapping("/payment")
public class PaymentPageController extends BaseController {

	/**
	 * 显示支付页面（GET请求，用于直接访问）
	 */
	@GetMapping("/index")
	public String showPaymentPageGet(@RequestParam(required = false) String itemName,
			@RequestParam(required = false) Integer amount, @RequestParam(required = false) String description,
			@RequestParam(required = false) String cartData, Model model, HttpSession session) {

		// 检查用户登录状态
		UserInfo userInfo = getSessionUser();
		if (userInfo == null) {
			log.warn("未登录用户尝试访问支付页面");
			return "redirect:/login?returnUrl=/payment/index";
		}

		// 设置页面数据
		model.addAttribute("itemName", itemName != null ? itemName : "汽车商品");
		model.addAttribute("amount", amount != null ? amount : 0);
		model.addAttribute("description", description != null ? description : "");
		model.addAttribute("cartData", cartData);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("isDevOrTest", isDevOrTestEnvironment());

		log.info("用户通过GET请求访问支付页面，用户ID: {}, 商品: {}, 金额: {}", userInfo.getId(), itemName, amount);

		return "payment/index";
	}

	/**
	 * 处理支付页面请求（POST请求，用于购物车结算）
	 */
	@PostMapping("/index")
	public String showPaymentPagePost(@Valid PaymentRequestForm paymentRequest,
			BindingResult bindingResult, Model model, HttpSession session) {

		// 检查用户登录状态
		UserInfo userInfo = getSessionUser();
		if (userInfo == null) {
			log.warn("未登录用户尝试访问支付页面");
			return "redirect:/login?returnUrl=/payment/index";
		}

		// 参数验证
		if (bindingResult.hasErrors()) {
			log.warn("支付请求参数验证失败: {}", bindingResult.getAllErrors());
			// 重定向到购物车页面，显示错误信息
			return "redirect:/cart?error=支付参数错误";
		}

		// 获取当前环境配置
		String activeProfile = System.getProperty("spring.profiles.active");
		if (activeProfile == null) {
			activeProfile = "dev"; // 默认使用dev环境
		}

		// 如果是dev或test环境，金额固定为1元
		Integer finalAmount = paymentRequest.getAmount();
		if ("dev".equals(activeProfile) || "test".equals(activeProfile)) {
			finalAmount = 1;
			log.info("开发/测试环境，支付金额固定为1元，原始金额: {}", paymentRequest.getAmount());
		}

		// 设置页面数据
		model.addAttribute("itemName", paymentRequest.getItemName());
		model.addAttribute("amount", finalAmount);
		model.addAttribute("description", paymentRequest.getDescription());
		model.addAttribute("cartData", paymentRequest.getCartData());
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("isDevOrTest", "dev".equals(activeProfile) || "test".equals(activeProfile));

		log.info("用户通过POST请求访问支付页面，用户ID: {}, 商品: {}, 金额: {}, 环境: {}", userInfo.getId(), paymentRequest.getItemName(),
				finalAmount, activeProfile);

		return "payment/index";
	}

	/**
	 * 检查是否为开发或测试环境
	 */
	private boolean isDevOrTestEnvironment() {
		String activeProfile = System.getProperty("spring.profiles.active");
		if (activeProfile == null) {
			activeProfile = "dev"; // 默认使用dev环境
		}
		return "dev".equals(activeProfile) || "test".equals(activeProfile);
	}
}
