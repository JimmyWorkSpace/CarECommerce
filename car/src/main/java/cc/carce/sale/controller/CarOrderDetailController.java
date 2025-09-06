//package cc.carce.sale.controller;
//
//import java.math.BigDecimal;
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
//import cc.carce.sale.entity.CarOrderDetailEntity;
//import cc.carce.sale.entity.CarOrderInfoEntity;
//import cc.carce.sale.service.CarOrderDetailService;
//import cc.carce.sale.service.CarOrderInfoService;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * 订单详情表控制器
// */
//@RestController
//@RequestMapping("api/order-detail")
//@Slf4j
//public class CarOrderDetailController extends BaseController {
//
//    @Resource
//    private CarOrderDetailService carOrderDetailService;
//    
//    @Resource
//    private CarOrderInfoService carOrderInfoService;
//
//    /**
//     * 创建订单详情
//     */
//    @PostMapping("/create")
//    public R<CarOrderDetailEntity> createOrderDetail(@RequestBody CreateOrderDetailRequest request, HttpServletRequest req) {
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
//            if (request.getOrderId() == null) {
//                return R.fail("订单ID不能为空", null);
//            }
//            
//            if (request.getProductId() == null) {
//                return R.fail("产品ID不能为空", null);
//            }
//            
//            if (request.getProductName() == null || request.getProductName().trim().isEmpty()) {
//                return R.fail("产品名称不能为空", null);
//            }
//            
//            if (request.getProductAmount() == null || request.getProductAmount() <= 0) {
//                return R.fail("产品数量必须大于0", null);
//            }
//            
//            // 检查订单是否存在且属于当前用户
//            CarOrderInfoEntity order = carOrderInfoService.getOrderById(request.getOrderId());
//            if (order == null) {
//                return R.fail("订单不存在", null);
//            }
//            
//            if (!order.getUserId().equals(user.getId())) {
//                return R.fail("无权限操作此订单", null);
//            }
//            
//            // 创建订单详情
//            CarOrderDetailEntity orderDetail = carOrderDetailService.createOrderDetail(
//                request.getOrderId(),
//                request.getProductId(),
//                request.getProductName(),
//                request.getProductAmount(),
//                request.getProductPrice()
//            );
//            
//            return R.ok("创建订单详情成功", orderDetail);
//            
//        } catch (Exception e) {
//            log.error("创建订单详情失败", e);
//            return R.fail("创建订单详情失败：" + e.getMessage(), null);
//        }
//    }
//
//    /**
//     * 根据ID查询订单详情
//     */
//    @GetMapping("/{id}")
//    public R<CarOrderDetailEntity> getOrderDetailById(@PathVariable Long id, HttpServletRequest req) {
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
//            CarOrderDetailEntity orderDetail = carOrderDetailService.getOrderDetailById(id);
//            if (orderDetail == null) {
//                return R.fail("订单详情不存在", null);
//            }
//            
//            // 检查订单是否属于当前用户
//            CarOrderInfoEntity order = carOrderInfoService.getOrderById(orderDetail.getOrderId());
//            if (order == null || !order.getUserId().equals(user.getId())) {
//                return R.fail("无权限查看此订单详情", null);
//            }
//            
//            return R.ok("查询订单详情成功", orderDetail);
//            
//        } catch (Exception e) {
//            log.error("查询订单详情失败，详情ID：{}", id, e);
//            return R.fail("查询订单详情失败：" + e.getMessage(), null);
//        }
//    }
//
//    /**
//     * 根据订单ID查询订单详情列表
//     */
//    @GetMapping("/order/{orderId}")
//    public R<List<CarOrderDetailEntity>> getOrderDetailsByOrderId(@PathVariable Long orderId, HttpServletRequest req) {
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
//            // 检查订单是否属于当前用户
//            CarOrderInfoEntity order = carOrderInfoService.getOrderById(orderId);
//            if (order == null) {
//                return R.fail("订单不存在", null);
//            }
//            
//            if (!order.getUserId().equals(user.getId())) {
//                return R.fail("无权限查看此订单详情", null);
//            }
//            
//            List<CarOrderDetailEntity> details = carOrderDetailService.getOrderDetailsByOrderId(orderId);
//            
//            return R.ok("查询订单详情列表成功", details);
//            
//        } catch (Exception e) {
//            log.error("查询订单详情列表失败，订单ID：{}", orderId, e);
//            return R.fail("查询订单详情列表失败：" + e.getMessage(), null);
//        }
//    }
//
//    /**
//     * 根据产品ID查询订单详情列表
//     */
//    @GetMapping("/product/{productId}")
//    public R<List<CarOrderDetailEntity>> getOrderDetailsByProductId(@PathVariable Long productId, HttpServletRequest req) {
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
//            List<CarOrderDetailEntity> details = carOrderDetailService.getOrderDetailsByProductId(productId);
//            
//            // 过滤出属于当前用户的订单详情
//            List<CarOrderDetailEntity> userDetails = new java.util.ArrayList<>();
//            for (CarOrderDetailEntity detail : details) {
//                CarOrderInfoEntity order = carOrderInfoService.getOrderById(detail.getOrderId());
//                if (order != null && order.getUserId().equals(user.getId())) {
//                    userDetails.add(detail);
//                }
//            }
//            
//            return R.ok("查询产品订单详情列表成功", userDetails);
//            
//        } catch (Exception e) {
//            log.error("查询产品订单详情列表失败，产品ID：{}", productId, e);
//            return R.fail("查询产品订单详情列表失败：" + e.getMessage(), null);
//        }
//    }
//
//    /**
//     * 更新订单详情
//     */
//    @PostMapping("/{id}/update")
//    public R<Boolean> updateOrderDetail(@PathVariable Long id, @RequestBody UpdateOrderDetailRequest request, HttpServletRequest req) {
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
//            // 检查订单详情是否存在
//            CarOrderDetailEntity existingDetail = carOrderDetailService.getOrderDetailById(id);
//            if (existingDetail == null) {
//                return R.fail("订单详情不存在", false);
//            }
//            
//            // 检查订单是否属于当前用户
//            CarOrderInfoEntity order = carOrderInfoService.getOrderById(existingDetail.getOrderId());
//            if (order == null || !order.getUserId().equals(user.getId())) {
//                return R.fail("无权限操作此订单详情", false);
//            }
//            
//            // 更新订单详情
//            CarOrderDetailEntity orderDetail = new CarOrderDetailEntity();
//            orderDetail.setId(id);
//            orderDetail.setProductName(request.getProductName());
//            orderDetail.setProductAmount(request.getProductAmount());
//            orderDetail.setProductPrice(request.getProductPrice());
//            orderDetail.setTotalPrice(request.getProductPrice() * request.getProductAmount());
//            
//            boolean result = carOrderDetailService.updateOrderDetail(orderDetail);
//            
//            if (result) {
//                return R.ok("更新订单详情成功", true);
//            } else {
//                return R.fail("更新订单详情失败", false);
//            }
//            
//        } catch (Exception e) {
//            log.error("更新订单详情失败，详情ID：{}", id, e);
//            return R.fail("更新订单详情失败：" + e.getMessage(), false);
//        }
//    }
//
//    /**
//     * 更新产品数量
//     */
//    @PostMapping("/{id}/amount")
//    public R<Boolean> updateProductAmount(@PathVariable Long id, @RequestParam Integer productAmount, HttpServletRequest req) {
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
//            if (productAmount == null || productAmount <= 0) {
//                return R.fail("产品数量必须大于0", false);
//            }
//            
//            // 检查订单详情是否存在
//            CarOrderDetailEntity existingDetail = carOrderDetailService.getOrderDetailById(id);
//            if (existingDetail == null) {
//                return R.fail("订单详情不存在", false);
//            }
//            
//            // 检查订单是否属于当前用户
//            CarOrderInfoEntity order = carOrderInfoService.getOrderById(existingDetail.getOrderId());
//            if (order == null || !order.getUserId().equals(user.getId())) {
//                return R.fail("无权限操作此订单详情", false);
//            }
//            
//            boolean result = carOrderDetailService.updateProductAmount(id, productAmount);
//            
//            if (result) {
//                return R.ok("更新产品数量成功", true);
//            } else {
//                return R.fail("更新产品数量失败", false);
//            }
//            
//        } catch (Exception e) {
//            log.error("更新产品数量失败，详情ID：{}，产品数量：{}", id, productAmount, e);
//            return R.fail("更新产品数量失败：" + e.getMessage(), false);
//        }
//    }
//
//    /**
//     * 更新产品价格
//     */
//    @PostMapping("/{id}/price")
//    public R<Boolean> updateProductPrice(@PathVariable Long id, @RequestParam Integer productPrice, HttpServletRequest req) {
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
//            if (productPrice == null || productPrice <= 0) {
//                return R.fail("产品价格必须大于0", false);
//            }
//            
//            // 检查订单详情是否存在
//            CarOrderDetailEntity existingDetail = carOrderDetailService.getOrderDetailById(id);
//            if (existingDetail == null) {
//                return R.fail("订单详情不存在", false);
//            }
//            
//            // 检查订单是否属于当前用户
//            CarOrderInfoEntity order = carOrderInfoService.getOrderById(existingDetail.getOrderId());
//            if (order == null || !order.getUserId().equals(user.getId())) {
//                return R.fail("无权限操作此订单详情", false);
//            }
//            
//            boolean result = carOrderDetailService.updateProductPrice(id, productPrice);
//            
//            if (result) {
//                return R.ok("更新产品价格成功", true);
//            } else {
//                return R.fail("更新产品价格失败", false);
//            }
//            
//        } catch (Exception e) {
//            log.error("更新产品价格失败，详情ID：{}，产品价格：{}", id, productPrice, e);
//            return R.fail("更新产品价格失败：" + e.getMessage(), false);
//        }
//    }
//
//    /**
//     * 删除订单详情
//     */
//    @PostMapping("/{id}/delete")
//    public R<Boolean> deleteOrderDetail(@PathVariable Long id, HttpServletRequest req) {
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
//            // 检查订单详情是否存在
//            CarOrderDetailEntity existingDetail = carOrderDetailService.getOrderDetailById(id);
//            if (existingDetail == null) {
//                return R.fail("订单详情不存在", false);
//            }
//            
//            // 检查订单是否属于当前用户
//            CarOrderInfoEntity order = carOrderInfoService.getOrderById(existingDetail.getOrderId());
//            if (order == null || !order.getUserId().equals(user.getId())) {
//                return R.fail("无权限操作此订单详情", false);
//            }
//            
//            boolean result = carOrderDetailService.deleteOrderDetail(id);
//            
//            if (result) {
//                return R.ok("删除订单详情成功", true);
//            } else {
//                return R.fail("删除订单详情失败", false);
//            }
//            
//        } catch (Exception e) {
//            log.error("删除订单详情失败，详情ID：{}", id, e);
//            return R.fail("删除订单详情失败：" + e.getMessage(), false);
//        }
//    }
//
//    /**
//     * 统计订单详情数量
//     */
//    @GetMapping("/count/order/{orderId}")
//    public R<Integer> countOrderDetailsByOrderId(@PathVariable Long orderId, HttpServletRequest req) {
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
//            // 检查订单是否属于当前用户
//            CarOrderInfoEntity order = carOrderInfoService.getOrderById(orderId);
//            if (order == null) {
//                return R.fail("订单不存在", 0);
//            }
//            
//            if (!order.getUserId().equals(user.getId())) {
//                return R.fail("无权限查看此订单", 0);
//            }
//            
//            int count = carOrderDetailService.countOrderDetailsByOrderId(orderId);
//            
//            return R.ok("统计订单详情数量成功", count);
//            
//        } catch (Exception e) {
//            log.error("统计订单详情数量失败，订单ID：{}", orderId, e);
//            return R.fail("统计订单详情数量失败：" + e.getMessage(), 0);
//        }
//    }
//
//    /**
//     * 统计产品在订单中的总数量
//     */
//    @GetMapping("/count/product/{productId}")
//    public R<Integer> sumProductAmountByProductId(@PathVariable Long productId, HttpServletRequest req) {
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
//            int count = carOrderDetailService.sumProductAmountByProductId(productId);
//            
//            return R.ok("统计产品订单数量成功", count);
//            
//        } catch (Exception e) {
//            log.error("统计产品订单数量失败，产品ID：{}", productId, e);
//            return R.fail("统计产品订单数量失败：" + e.getMessage(), 0);
//        }
//    }
//
//    /**
//     * 计算订单详情总金额
//     */
//    @GetMapping("/total/order/{orderId}")
//    public R<Integer> calculateOrderDetailsTotal(@PathVariable Long orderId, HttpServletRequest req) {
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
//            // 检查订单是否属于当前用户
//            CarOrderInfoEntity order = carOrderInfoService.getOrderById(orderId);
//            if (order == null) {
//                return R.fail("订单不存在", 0);
//            }
//            
//            if (!order.getUserId().equals(user.getId())) {
//                return R.fail("无权限查看此订单", 0);
//            }
//            
//            Integer total = carOrderDetailService.calculateOrderDetailsTotal(orderId);
//            
//            return R.ok("计算订单详情总金额成功", total);
//            
//        } catch (Exception e) {
//            log.error("计算订单详情总金额失败，订单ID：{}", orderId, e);
//            return R.fail("计算订单详情总金额失败：" + e.getMessage(), 0);
//        }
//    }
//
//    /**
//     * 创建订单详情请求类
//     */
//    public static class CreateOrderDetailRequest {
//        private Long orderId;
//        private Long productId;
//        private String productName;
//        private Integer productAmount;
//        private Integer productPrice;
//
//        // Getters and Setters
//        public Long getOrderId() {
//            return orderId;
//        }
//
//        public void setOrderId(Long orderId) {
//            this.orderId = orderId;
//        }
//
//        public Long getProductId() {
//            return productId;
//        }
//
//        public void setProductId(Long productId) {
//            this.productId = productId;
//        }
//
//        public String getProductName() {
//            return productName;
//        }
//
//        public void setProductName(String productName) {
//            this.productName = productName;
//        }
//
//        public Integer getProductAmount() {
//            return productAmount;
//        }
//
//        public void setProductAmount(Integer productAmount) {
//            this.productAmount = productAmount;
//        }
//
//        public Integer getProductPrice() {
//            return productPrice;
//        }
//
//        public void setProductPrice(Integer productPrice) {
//            this.productPrice = productPrice;
//        }
//    }
//
//    /**
//     * 更新订单详情请求类
//     */
//    public static class UpdateOrderDetailRequest {
//        private String productName;
//        private Integer productAmount;
//        private Integer productPrice;
//
//        // Getters and Setters
//        public String getProductName() {
//            return productName;
//        }
//
//        public void setProductName(String productName) {
//            this.productName = productName;
//        }
//
//        public Integer getProductAmount() {
//            return productAmount;
//        }
//
//        public void setProductAmount(Integer productAmount) {
//            this.productAmount = productAmount;
//        }
//
//        public Integer getProductPrice() {
//            return productPrice;
//        }
//
//        public void setProductPrice(Integer productPrice) {
//            this.productPrice = productPrice;
//        }
//    }
//}
