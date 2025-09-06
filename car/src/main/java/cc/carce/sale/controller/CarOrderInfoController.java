//package cc.carce.sale.controller;
//
//import java.util.List;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import cc.carce.sale.common.R;
//import cc.carce.sale.config.AuthInterceptor.UserInfo;
//import cc.carce.sale.entity.CarOrderInfoEntity;
//import cc.carce.sale.entity.CarOrderDetailEntity;
//import cc.carce.sale.service.CarOrderInfoService;
//import cc.carce.sale.service.CarOrderDetailService;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * 订单主表控制器
// */
//@RestController
//@RequestMapping("api/order")
//@Slf4j
//public class CarOrderInfoController extends BaseController {
//
//    @Resource
//    private CarOrderInfoService carOrderInfoService;
//    
//    @Resource
//    private CarOrderDetailService carOrderDetailService;
//
//    /**
//     * 创建订单
//     */
//    @PostMapping("/create")
//    public R<CarOrderInfoEntity> createOrder(@RequestBody CreateOrderRequest request, HttpServletRequest req) {
//        try {
//            // 检查用户登录状态
//            if (!isLogin()) {
//                return R.fail("请先登录", null);
//            }
//            
//            UserInfo user = getSessionUser();
//            if (user == null) {
//                return R.fail("用户信息获取失败", null);
//            }
//            
//            // 验证请求参数
//            if (request.getOrderDetails() == null || request.getOrderDetails().isEmpty()) {
//                return R.fail("订单详情不能为空", null);
//            }
//            
//            if (request.getReceiverName() == null || request.getReceiverName().trim().isEmpty()) {
//                return R.fail("收件人姓名不能为空", null);
//            }
//            
//            if (request.getReceiverMobile() == null || request.getReceiverMobile().trim().isEmpty()) {
//                return R.fail("收件人电话不能为空", null);
//            }
//            
//            if (request.getReceiverAddress() == null || request.getReceiverAddress().trim().isEmpty()) {
//                return R.fail("收件人地址不能为空", null);
//            }
//            
//            // 创建订单
//            CarOrderInfoEntity order = carOrderInfoService.createOrder(
//                user.getId(),
//                request.getReceiverName(),
//                request.getReceiverMobile(),
//                request.getReceiverAddress(),
//                request.getOrderDetails()
//            );
//            
//            return R.ok("创建订单成功", order);
//            
//        } catch (Exception e) {
//            log.error("创建订单失败", e);
//            return R.fail("创建订单失败：" + e.getMessage(), null);
//        }
//    }
//
//    /**
//     * 根据ID查询订单
//     */
//    @GetMapping("/{id}")
//    public R<CarOrderInfoEntity> getOrderById(@PathVariable Long id, HttpServletRequest req) {
//        try {
//            // 检查用户登录状态
//            if (!isLogin()) {
//                return R.fail("请先登录", null);
//            }
//            
//            UserInfo user = getSessionUser();
//            if (user == null) {
//                return R.fail("用户信息获取失败", null);
//            }
//            
//            CarOrderInfoEntity order = carOrderInfoService.getOrderById(id);
//            if (order == null) {
//                return R.fail("订单不存在", null);
//            }
//            
//            // 检查订单是否属于当前用户
//            if (!order.getUserId().equals(user.getId())) {
//                return R.fail("无权限查看此订单", null);
//            }
//            
//            return R.ok("查询订单成功", order);
//            
//        } catch (Exception e) {
//            log.error("查询订单失败，订单ID：{}", id, e);
//            return R.fail("查询订单失败：" + e.getMessage(), null);
//        }
//    }
//
//    /**
//     * 根据订单号查询订单
//     */
//    @GetMapping("/orderNo/{orderNo}")
//    public R<CarOrderInfoEntity> getOrderByOrderNo(@PathVariable String orderNo, HttpServletRequest req) {
//        try {
//            // 检查用户登录状态
//            if (!isLogin()) {
//                return R.fail("请先登录", null);
//            }
//            
//            UserInfo user = getSessionUser();
//            if (user == null) {
//                return R.fail("用户信息获取失败", null);
//            }
//            
//            CarOrderInfoEntity order = carOrderInfoService.getOrderByOrderNo(orderNo);
//            if (order == null) {
//                return R.fail("订单不存在", null);
//            }
//            
//            // 检查订单是否属于当前用户
//            if (!order.getUserId().equals(user.getId())) {
//                return R.fail("无权限查看此订单", null);
//            }
//            
//            return R.ok("查询订单成功", order);
//            
//        } catch (Exception e) {
//            log.error("查询订单失败，订单号：{}", orderNo, e);
//            return R.fail("查询订单失败：" + e.getMessage(), null);
//        }
//    }
//
//    /**
//     * 获取用户订单列表
//     */
//    @GetMapping("/list")
//    public R<List<CarOrderInfoEntity>> getOrdersByUserId(HttpServletRequest req) {
//        try {
//            // 检查用户登录状态
//            if (!isLogin()) {
//                return R.fail("请先登录", null);
//            }
//            
//            UserInfo user = getSessionUser();
//            if (user == null) {
//                return R.fail("用户信息获取失败", null);
//            }
//            
//            List<CarOrderInfoEntity> orders = carOrderInfoService.getOrdersByUserId(user.getId());
//            
//            return R.ok("查询订单列表成功", orders);
//            
//        } catch (Exception e) {
//            log.error("查询订单列表失败", e);
//            return R.fail("查询订单列表失败：" + e.getMessage(), null);
//        }
//    }
//
//    /**
//     * 根据订单状态获取用户订单列表
//     */
//    @GetMapping("/list/status/{orderStatus}")
//    public R<List<CarOrderInfoEntity>> getOrdersByUserIdAndStatus(@PathVariable Integer orderStatus, HttpServletRequest req) {
//        try {
//            // 检查用户登录状态
//            if (!isLogin()) {
//                return R.fail("请先登录", null);
//            }
//            
//            UserInfo user = getSessionUser();
//            if (user == null) {
//                return R.fail("用户信息获取失败", null);
//            }
//            
//            List<CarOrderInfoEntity> orders = carOrderInfoService.getOrdersByUserIdAndStatus(user.getId(), orderStatus);
//            
//            return R.ok("查询订单列表成功", orders);
//            
//        } catch (Exception e) {
//            log.error("查询订单列表失败，订单状态：{}", orderStatus, e);
//            return R.fail("查询订单列表失败：" + e.getMessage(), null);
//        }
//    }
//
//    /**
//     * 更新订单状态
//     */
//    @PostMapping("/{id}/status")
//    public R<Boolean> updateOrderStatus(@PathVariable Long id, @RequestParam Integer orderStatus, HttpServletRequest req) {
//        try {
//            // 检查用户登录状态
//            if (!isLogin()) {
//                return R.fail("请先登录", null);
//            }
//            
//            UserInfo user = getSessionUser();
//            if (user == null) {
//                return R.fail("用户信息获取失败", null);
//            }
//            
//            // 检查订单是否存在且属于当前用户
//            CarOrderInfoEntity order = carOrderInfoService.getOrderById(id);
//            if (order == null) {
//                return R.fail("订单不存在", false);
//            }
//            
//            if (!order.getUserId().equals(user.getId())) {
//                return R.fail("无权限操作此订单", false);
//            }
//            
//            boolean result = carOrderInfoService.updateOrderStatus(id, orderStatus);
//            
//            if (result) {
//                return R.ok("更新订单状态成功", true);
//            } else {
//                return R.fail("更新订单状态失败", false);
//            }
//            
//        } catch (Exception e) {
//            log.error("更新订单状态失败，订单ID：{}，订单状态：{}", id, orderStatus, e);
//            return R.fail("更新订单状态失败：" + e.getMessage(), false);
//        }
//    }
//
//    /**
//     * 更新物流信息
//     */
//    @PostMapping("/{id}/logistics")
//    public R<Boolean> updateLogisticsInfo(@PathVariable Long id, @RequestBody LogisticsInfoRequest request, HttpServletRequest req) {
//        try {
//            // 检查用户登录状态
//            if (!isLogin()) {
//                return R.fail("请先登录", null);
//            }
//            
//            UserInfo user = getSessionUser();
//            if (user == null) {
//                return R.fail("用户信息获取失败", null);
//            }
//            
//            // 检查订单是否存在且属于当前用户
//            CarOrderInfoEntity order = carOrderInfoService.getOrderById(id);
//            if (order == null) {
//                return R.fail("订单不存在", false);
//            }
//            
//            if (!order.getUserId().equals(user.getId())) {
//                return R.fail("无权限操作此订单", false);
//            }
//            
//            boolean result = carOrderInfoService.updateLogisticsInfo(
//                id, 
//                request.getLogicNumber(), 
//                request.getReceiverName(), 
//                request.getReceiverMobile(), 
//                request.getReceiverAddress()
//            );
//            
//            if (result) {
//                return R.ok("更新物流信息成功", true);
//            } else {
//                return R.fail("更新物流信息失败", false);
//            }
//            
//        } catch (Exception e) {
//            log.error("更新物流信息失败，订单ID：{}", id, e);
//            return R.fail("更新物流信息失败：" + e.getMessage(), false);
//        }
//    }
//
//    /**
//     * 取消订单
//     */
//    @PostMapping("/{id}/cancel")
//    public R<Boolean> cancelOrder(@PathVariable Long id, HttpServletRequest req) {
//        try {
//            // 检查用户登录状态
//            if (!isLogin()) {
//                return R.fail("请先登录", null);
//            }
//            
//            UserInfo user = getSessionUser();
//            if (user == null) {
//                return R.fail("用户信息获取失败", null);
//            }
//            
//            // 检查订单是否存在且属于当前用户
//            CarOrderInfoEntity order = carOrderInfoService.getOrderById(id);
//            if (order == null) {
//                return R.fail("订单不存在", false);
//            }
//            
//            if (!order.getUserId().equals(user.getId())) {
//                return R.fail("无权限操作此订单", false);
//            }
//            
//            // 检查订单状态是否可以取消
//            if (!order.getOrderStatus().equals(CarOrderInfoEntity.OrderStatus.UNPAID.getCode())) {
//                return R.fail("只有未支付订单可以取消", false);
//            }
//            
//            boolean result = carOrderInfoService.cancelOrder(id);
//            
//            if (result) {
//                return R.ok("取消订单成功", true);
//            } else {
//                return R.fail("取消订单失败", false);
//            }
//            
//        } catch (Exception e) {
//            log.error("取消订单失败，订单ID：{}", id, e);
//            return R.fail("取消订单失败：" + e.getMessage(), false);
//        }
//    }
//
//    /**
//     * 删除订单
//     */
//    @PostMapping("/{id}/delete")
//    public R<Boolean> deleteOrder(@PathVariable Long id, HttpServletRequest req) {
//        try {
//            // 检查用户登录状态
//            if (!isLogin()) {
//                return R.fail("请先登录", null);
//            }
//            
//            UserInfo user = getSessionUser();
//            if (user == null) {
//                return R.fail("用户信息获取失败", null);
//            }
//            
//            // 检查订单是否存在且属于当前用户
//            CarOrderInfoEntity order = carOrderInfoService.getOrderById(id);
//            if (order == null) {
//                return R.fail("订单不存在", false);
//            }
//            
//            if (!order.getUserId().equals(user.getId())) {
//                return R.fail("无权限操作此订单", false);
//            }
//            
//            boolean result = carOrderInfoService.deleteOrder(id);
//            
//            if (result) {
//                return R.ok("删除订单成功", true);
//            } else {
//                return R.fail("删除订单失败", false);
//            }
//            
//        } catch (Exception e) {
//            log.error("删除订单失败，订单ID：{}", id, e);
//            return R.fail("删除订单失败：" + e.getMessage(), false);
//        }
//    }
//
//    /**
//     * 获取订单详情列表
//     */
//    @GetMapping("/{id}/details")
//    public R<List<CarOrderDetailEntity>> getOrderDetails(@PathVariable Long id, HttpServletRequest req) {
//        try {
//            // 检查用户登录状态
//            if (!isLogin()) {
//                return R.fail("请先登录", null);
//            }
//            
//            UserInfo user = getSessionUser();
//            if (user == null) {
//                return R.fail("用户信息获取失败", null);
//            }
//            
//            // 检查订单是否存在且属于当前用户
//            CarOrderInfoEntity order = carOrderInfoService.getOrderById(id);
//            if (order == null) {
//                return R.fail("订单不存在", null);
//            }
//            
//            if (!order.getUserId().equals(user.getId())) {
//                return R.fail("无权限查看此订单", null);
//            }
//            
//            List<CarOrderDetailEntity> details = carOrderDetailService.getOrderDetailsByOrderId(id);
//            
//            return R.ok("查询订单详情成功", details);
//            
//        } catch (Exception e) {
//            log.error("查询订单详情失败，订单ID：{}", id, e);
//            return R.fail("查询订单详情失败：" + e.getMessage(), null);
//        }
//    }
//
//    /**
//     * 统计用户订单数量
//     */
//    @GetMapping("/count")
//    public R<Integer> countOrdersByUserId(HttpServletRequest req) {
//        try {
//            // 检查用户登录状态
//            if (!isLogin()) {
//                return R.fail("请先登录", null);
//            }
//            
//            UserInfo user = getSessionUser();
//            if (user == null) {
//                return R.fail("用户信息获取失败", null);
//            }
//            
//            int count = carOrderInfoService.countOrdersByUserId(user.getId());
//            
//            return R.ok("统计订单数量成功", count);
//            
//        } catch (Exception e) {
//            log.error("统计订单数量失败", e);
//            return R.fail("统计订单数量失败：" + e.getMessage(), 0);
//        }
//    }
//
//    /**
//     * 统计用户各状态订单数量
//     */
//    @GetMapping("/count/status/{orderStatus}")
//    public R<Integer> countOrdersByUserIdAndStatus(@PathVariable Integer orderStatus, HttpServletRequest req) {
//        try {
//            // 检查用户登录状态
//            if (!isLogin()) {
//                return R.fail("请先登录", null);
//            }
//            
//            UserInfo user = getSessionUser();
//            if (user == null) {
//                return R.fail("用户信息获取失败", null);
//            }
//            
//            int count = carOrderInfoService.countOrdersByUserIdAndStatus(user.getId(), orderStatus);
//            
//            return R.ok("统计订单数量成功", count);
//            
//        } catch (Exception e) {
//            log.error("统计订单数量失败，订单状态：{}", orderStatus, e);
//            return R.fail("统计订单数量失败：" + e.getMessage(), 0);
//        }
//    }
//
//    /**
//     * 创建订单请求类
//     */
//    public static class CreateOrderRequest {
//        private String receiverName;
//        private String receiverMobile;
//        private String receiverAddress;
//        private List<CarOrderDetailEntity> orderDetails;
//
//        // Getters and Setters
//        public String getReceiverName() {
//            return receiverName;
//        }
//
//        public void setReceiverName(String receiverName) {
//            this.receiverName = receiverName;
//        }
//
//        public String getReceiverMobile() {
//            return receiverMobile;
//        }
//
//        public void setReceiverMobile(String receiverMobile) {
//            this.receiverMobile = receiverMobile;
//        }
//
//        public String getReceiverAddress() {
//            return receiverAddress;
//        }
//
//        public void setReceiverAddress(String receiverAddress) {
//            this.receiverAddress = receiverAddress;
//        }
//
//        public List<CarOrderDetailEntity> getOrderDetails() {
//            return orderDetails;
//        }
//
//        public void setOrderDetails(List<CarOrderDetailEntity> orderDetails) {
//            this.orderDetails = orderDetails;
//        }
//    }
//
//    /**
//     * 物流信息请求类
//     */
//    public static class LogisticsInfoRequest {
//        private String logicNumber;
//        private String receiverName;
//        private String receiverMobile;
//        private String receiverAddress;
//
//        // Getters and Setters
//        public String getLogicNumber() {
//            return logicNumber;
//        }
//
//        public void setLogicNumber(String logicNumber) {
//            this.logicNumber = logicNumber;
//        }
//
//        public String getReceiverName() {
//            return receiverName;
//        }
//
//        public void setReceiverName(String receiverName) {
//            this.receiverName = receiverName;
//        }
//
//        public String getReceiverMobile() {
//            return receiverMobile;
//        }
//
//        public void setReceiverMobile(String receiverMobile) {
//            this.receiverMobile = receiverMobile;
//        }
//
//        public String getReceiverAddress() {
//            return receiverAddress;
//        }
//
//        public void setReceiverAddress(String receiverAddress) {
//            this.receiverAddress = receiverAddress;
//        }
//    }
//}
