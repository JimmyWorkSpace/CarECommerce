package cc.carce.sale.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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
import cc.carce.sale.entity.CarShoppingCartEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * 我的订单控制器
 */
@Slf4j
@Controller
@RequestMapping("/my-order")
public class MyOrderController extends BaseController {

    private static final String CurrencyUnit = "$";

    @Resource
    private CarOrderInfoService carOrderInfoService;
    
    @Resource
    private CarOrderDetailService carOrderDetailService;
    
    @Resource
    private CarShoppingCartService carShoppingCartService;


    /**
     * 显示订单详情页面
     */
    @GetMapping("/detail-page")
    public String showOrderDetailPage(@RequestParam Long orderId, Model model, HttpSession session) {
        try {
            // 检查用户登录状态
            if (!isLogin()) {
                log.warn("未登录用户尝试访问订单详情页面");
                return "redirect:/login?returnUrl=/my-order/detail-page?orderId=" + orderId;
            }

            UserInfo user = getSessionUser();
            
            // 验证订单是否属于当前用户
            CarOrderInfoEntity order = carOrderInfoService.getOrderById(orderId);
            if (order == null || !order.getUserId().equals(user.getId())) {
                log.warn("订单不存在或无权限访问，用户ID: {}, 订单ID: {}", user.getId(), orderId);
                model.addAttribute("errorMessage", "订单不存在或无权限访问");
                return "/order/detail";
            }

            // 设置页面数据
            model.addAttribute("orderId", orderId);
            model.addAttribute("orderNo", order.getOrderNo());
            model.addAttribute("userInfo", user);
            model.addAttribute("CurrencyUnit", CurrencyUnit);
            // 设置模板内容
            model.addAttribute("content", "/order/detail.ftl");

            log.info("用户访问订单详情页面，用户ID: {}, 订单ID: {}, 订单号: {}", 
                    user.getId(), orderId, order.getOrderNo());

            return "/layout/main";
        } catch (Exception e) {
            log.error("显示订单详情页面异常", e);
            model.addAttribute("errorMessage", "获取订单详情失败");
            return "/order/detail";
        }
    }

    /**
     * 获取订单详情（API接口）
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
     * 获取订单完整信息（API接口）
     */
    @GetMapping("/order-info")
    @ResponseBody
    public R<CarOrderInfoEntity> getOrderInfo(@RequestParam Long orderId) {
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
            
            return R.ok("获取订单信息成功", order);
        } catch (Exception e) {
            log.error("获取订单信息异常", e);
            return R.fail("获取订单信息异常: " + e.getMessage(), null);
        }
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel/{orderId}")
    @ResponseBody
    public R<String> cancelOrder(@PathVariable("orderId") Long orderId) {
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
