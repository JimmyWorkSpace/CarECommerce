//package cc.carce.sale.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 购物车控制器
// */
//@Controller
//public class CartController {
//
//    /**
//     * 购物车页面
//     */
//    @GetMapping("/cart")
//    public String cartPage(Model model, HttpServletRequest req) {
//        // 检查用户登录状态
//        Object user = req.getSession().getAttribute("user");
//        model.addAttribute("user", user);
//        
//        // 从session中获取购物车数据
//        @SuppressWarnings("unchecked")
//        List<Map<String, Object>> cart = (List<Map<String, Object>>) req.getSession().getAttribute("cart");
//        if (cart == null) {
//            cart = new ArrayList<>();
//        }
//        model.addAttribute("cart", cart);
//        
//        // 计算购物车统计信息
//        int totalItems = cart.size();
//        int totalQuantity = 0;
//        double totalPrice = 0.0;
//        
//        for (Map<String, Object> item : cart) {
//            Integer quantity = (Integer) item.get("quantity");
//            Double price = (Double) item.get("price");
//            if (quantity != null && price != null) {
//                totalQuantity += quantity;
//                totalPrice += price * quantity;
//            }
//        }
//        
//        model.addAttribute("totalItems", totalItems);
//        model.addAttribute("totalQuantity", totalQuantity);
//        model.addAttribute("totalPrice", totalPrice);
//        
//        // 设置页面标题和描述
//        model.addAttribute("title", "購物車 - 二手車銷售平台");
//        model.addAttribute("description", "查看和管理您的購物車商品");
//        model.addAttribute("url", req.getRequestURL().toString());
//        model.addAttribute("image", "/img/swipper/slide1.jpg");
//        
//        // 处理URL参数中的消息
//        String message = req.getParameter("message");
//        if (message != null) {
//            model.addAttribute("message", message);
//        }
//        
//        // 设置模板内容
//        model.addAttribute("content", "/cart/index.ftl");
//        
//        return "/layout/main";
//    }
//
//    /**
//     * 添加商品到购物车
//     */
//    @PostMapping("/cart/add")
//    public String addToCart(@RequestParam String productId,
//                           @RequestParam String productName,
//                           @RequestParam Double productPrice,
//                           @RequestParam String productImage,
//                           HttpSession session) {
//        
//        // 从session中获取购物车
//        @SuppressWarnings("unchecked")
//        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
//        if (cart == null) {
//            cart = new ArrayList<>();
//        }
//        
//        // 检查商品是否已在购物车中
//        boolean found = false;
//        for (Map<String, Object> item : cart) {
//            if (productId.equals(item.get("id"))) {
//                // 增加数量
//                Integer quantity = (Integer) item.get("quantity");
//                item.put("quantity", quantity + 1);
//                found = true;
//                break;
//            }
//        }
//        
//        // 如果商品不在购物车中，添加新商品
//        if (!found) {
//            Map<String, Object> newItem = new HashMap<>();
//            newItem.put("id", productId);
//            newItem.put("name", productName);
//            newItem.put("price", productPrice);
//            newItem.put("image", productImage);
//            newItem.put("quantity", 1);
//            cart.add(newItem);
//        }
//        
//        // 保存购物车到session
//        session.setAttribute("cart", cart);
//        
//        return "redirect:/cart";
//    }
//
//    /**
//     * 从购物车移除商品
//     */
//    @PostMapping("/cart/remove")
//    public String removeFromCart(@RequestParam String productId, HttpSession session) {
//        @SuppressWarnings("unchecked")
//        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
//        
//        if (cart != null) {
//            cart.removeIf(item -> productId.equals(item.get("id")));
//            session.setAttribute("cart", cart);
//        }
//        
//        return "redirect:/cart";
//    }
//
//    /**
//     * 更新购物车商品数量
//     */
//    @PostMapping("/cart/update")
//    public String updateQuantity(@RequestParam String productId,
//                                @RequestParam Integer quantity,
//                                HttpSession session) {
//        @SuppressWarnings("unchecked")
//        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
//        
//        if (cart != null) {
//            for (Map<String, Object> item : cart) {
//                if (productId.equals(item.get("id"))) {
//                    if (quantity <= 0) {
//                        cart.remove(item);
//                    } else {
//                        item.put("quantity", quantity);
//                    }
//                    break;
//                }
//            }
//            session.setAttribute("cart", cart);
//        }
//        
//        return "redirect:/cart";
//    }
//
//    /**
//     * 清空购物车
//     */
//    @PostMapping("/cart/clear")
//    public String clearCart(HttpSession session) {
//        session.removeAttribute("cart");
//        return "redirect:/cart";
//    }
//
//    /**
//     * 结算
//     */
//    @PostMapping("/cart/checkout")
//    public String checkout(HttpSession session) {
//        // 这里可以添加结算逻辑
//        // 暂时只是清空购物车
//        session.removeAttribute("cart");
//        return "redirect:/cart?message=訂單提交成功！";
//    }
//} 