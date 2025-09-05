package cc.carce.sale.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cc.carce.sale.common.R;
import cc.carce.sale.config.AuthInterceptor.UserInfo;
import cc.carce.sale.entity.CarOrderInfoEntity;
import cc.carce.sale.entity.CarOrderDetailEntity;
import cc.carce.sale.service.CarOrderInfoService;
import cc.carce.sale.service.CarOrderDetailService;
import cc.carce.sale.service.CarShoppingCartService;
import cn.hutool.json.JSONUtil;
import cc.carce.sale.entity.CarShoppingCartEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * 我的订单控制器
 */
@Slf4j
@Controller
@RequestMapping("/my-order")
public class MyOrderController extends BaseController {

    @Resource
    private CarOrderInfoService carOrderInfoService;
    
    @Resource
    private CarOrderDetailService carOrderDetailService;
    
    @Resource
    private CarShoppingCartService carShoppingCartService;

    /**
     * 显示我的订单页面
     */
    @GetMapping("/index")
    public String showMyOrderPage(Model model, HttpServletRequest request) {
        try {
            // 检查用户登录状态
            UserInfo userInfo = getSessionUser();
            if (userInfo == null) {
                log.warn("未登录用户尝试访问我的订单页面");
                return "redirect:/login?returnUrl=/my-order/index";
            }

            // 获取用户的所有订单
            List<CarOrderInfoEntity> orders = carOrderInfoService.getOrdersByUserId(userInfo.getId());
            model.addAttribute("orders", orders);
            model.addAttribute("ordersJson", JSONUtil.toJsonPrettyStr(orders));
            model.addAttribute("userInfo", userInfo);

            // 设置模板内容
            model.addAttribute("content", "/my-order/index.ftl");

            log.info("用户访问我的订单页面，用户ID: {}, 订单数量: {}", userInfo.getId(), orders.size());

            return "/layout/main";
        } catch (Exception e) {
            log.error("显示我的订单页面异常", e);
            model.addAttribute("error", "页面加载失败：" + e.getMessage());
            return "/layout/main";
        }
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/detail")
    @ResponseBody
    public R<List<CarOrderDetailEntity>> getOrderDetail(@RequestParam Long orderId) {
        try {
            // 检查用户登录状态
            if (!isLogin()) {
                return R.fail("请先登录", null);
            }

            UserInfo user = getSessionUser();
            
            // 验证订单是否属于当前用户
            CarOrderInfoEntity order = carOrderInfoService.getOrderById(orderId);
            if (order == null || !order.getUserId().equals(user.getId())) {
                return R.fail("订单不存在或无权限访问", null);
            }

            // 获取订单详情
            List<CarOrderDetailEntity> orderDetails = carOrderDetailService.getOrderDetailsByOrderId(orderId);
            
            return R.ok("获取订单详情成功", orderDetails);
        } catch (Exception e) {
            log.error("获取订单详情异常", e);
            return R.fail("获取订单详情异常: " + e.getMessage(), null);
        }
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel")
    @ResponseBody
    public R<String> cancelOrder(@RequestParam Long orderId) {
        try {
            // 检查用户登录状态
            if (!isLogin()) {
                return R.fail("请先登录", null);
            }

            UserInfo user = getSessionUser();
            
            // 验证订单是否属于当前用户
            CarOrderInfoEntity order = carOrderInfoService.getOrderById(orderId);
            if (order == null || !order.getUserId().equals(user.getId())) {
                return R.fail("订单不存在或无权限访问", null);
            }

            // 检查订单状态，只有未支付的订单才能取消
            if (!CarOrderInfoEntity.OrderStatus.UNPAID.getCode().equals(order.getOrderStatus())) {
                return R.fail("只有未支付的订单才能取消", null);
            }

            // 获取订单详情，将商品放回购物车
            List<CarOrderDetailEntity> orderDetails = carOrderDetailService.getOrderDetailsByOrderId(orderId);
            for (CarOrderDetailEntity detail : orderDetails) {
                CarShoppingCartEntity cartItem = new CarShoppingCartEntity();
                cartItem.setUserId(user.getId());
                cartItem.setProductId(detail.getProductId());
                cartItem.setProductName(detail.getProductName());
                cartItem.setProductAmount(detail.getProductAmount());
                cartItem.setProductPrice(detail.getProductPrice());
                cartItem.setCreateTime(new java.util.Date());
                cartItem.setShowOrder(0);
                
                carShoppingCartService.addToCart(cartItem);
            }

            // 取消订单
            boolean success = carOrderInfoService.cancelOrder(orderId);
            if (success) {
                log.info("用户取消订单成功，用户ID: {}, 订单ID: {}", user.getId(), orderId);
                return R.ok("订单取消成功，商品已放回购物车", null);
            } else {
                return R.fail("取消订单失败", null);
            }
        } catch (Exception e) {
            log.error("取消订单异常", e);
            return R.fail("取消订单异常: " + e.getMessage(), null);
        }
    }

    /**
     * 重新支付订单
     */
    @PostMapping("/repay/{orderId}")
    @ResponseBody
    public R<String> repayOrder(@PathVariable("orderId") Long orderId) {
        try {
            // 检查用户登录状态
            if (!isLogin()) {
                return R.fail("请先登录", null);
            }

            UserInfo user = getSessionUser();
            
            // 验证订单是否属于当前用户
            CarOrderInfoEntity order = carOrderInfoService.getOrderById(orderId);
            if (order == null || !order.getUserId().equals(user.getId())) {
                return R.fail("订单不存在或无权限访问", null);
            }

            // 检查订单状态，只有未支付的订单才能重新支付
            if (!CarOrderInfoEntity.OrderStatus.UNPAID.getCode().equals(order.getOrderStatus())) {
                return R.fail("只有未支付的订单才能重新支付", null);
            }

            // 重定向到支付页面
            String paymentUrl = "/payment/index?orderId=" + orderId;
            return R.ok("跳转到支付页面", paymentUrl);
        } catch (Exception e) {
            log.error("重新支付订单异常", e);
            return R.fail("重新支付订单异常: " + e.getMessage(), null);
        }
    }
}
