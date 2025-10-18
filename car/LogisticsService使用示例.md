# LogisticsService 使用示例

## 概述

LogisticsService 提供了完整的物流单创建功能，支持宅配到府和超商取货两种方式。推荐使用统一接口 `createLogisticsByOrderType()` 方法，该方法会根据订单类型自动选择相应的物流单创建方法。

## 订单类型说明

根据 `CarOrderInfoEntity.orderType` 字段：
- `1` - 宅配到府
- `2` - 超商取货

## 推荐使用方式（统一接口）

```java
@Resource
private LogisticsService logisticsService;

/**
 * 根据订单类型自动创建物流单
 */
public void createLogisticsOrder(CarOrderInfoEntity orderInfo, 
                                List<CarOrderDetailEntity> orderDetails, 
                                CarPaymentOrderEntity paymentOrder) {
    
    // 使用统一接口，自动根据订单类型选择物流方式
    R<Map<String, Object>> result = logisticsService.createLogisticsByOrderType(
        orderInfo, orderDetails, paymentOrder);
    
    if (result.getCode() == 1) {
        // 创建成功
        Map<String, Object> logisticsData = result.getData();
        String allPayLogisticsId = (String) logisticsData.get("AllPayLogisticsID");
        
        // 根据订单类型处理不同的返回信息
        if (orderInfo.getOrderType() == 1) {
            // 宅配到府
            String bookingNote = (String) logisticsData.get("BookingNote");
            log.info("宅配物流单创建成功，绿界物流单号：{}，托运单号：{}", 
                    allPayLogisticsId, bookingNote);
        } else if (orderInfo.getOrderType() == 2) {
            // 超商取货
            String cvsPaymentNo = (String) logisticsData.get("CVSPaymentNo");
            String cvsValidationNo = (String) logisticsData.get("CVSValidationNo");
            log.info("超商物流单创建成功，绿界物流单号：{}，寄货编号：{}，验证码：{}", 
                    allPayLogisticsId, cvsPaymentNo, cvsValidationNo);
        }
        
        // 更新订单状态为已发货
        updateOrderStatus(orderInfo.getId(), 5); // 5 = 已发货
        
    } else {
        // 创建失败
        log.error("物流单创建失败：{}", result.getMsg());
        // 处理失败情况
    }
}
```

## 分别调用特定方法

```java
/**
 * 根据订单类型分别调用特定方法
 */
public void createLogisticsOrderByType(CarOrderInfoEntity orderInfo, 
                                      List<CarOrderDetailEntity> orderDetails, 
                                      CarPaymentOrderEntity paymentOrder) {
    
    R<Map<String, Object>> result;
    
    if (orderInfo.getOrderType() == 1) {
        // 宅配到府
        result = logisticsService.createHomeDeliveryLogistics(
            orderInfo, orderDetails, paymentOrder);
            
    } else if (orderInfo.getOrderType() == 2) {
        // 超商取货
        result = logisticsService.createConvenienceStoreLogistics(
            orderInfo, orderDetails, paymentOrder);
            
    } else {
        log.error("不支持的订单类型：{}", orderInfo.getOrderType());
        return;
    }
    
    // 处理结果...
}
```

## 便捷方法使用

```java
/**
 * 通过订单ID创建物流单（需要注入相关服务）
 */
public void createLogisticsByOrderId(Long orderId) {
    // 注意：此方法需要注入 CarOrderInfoService、CarOrderDetailService、CarPaymentOrderService
    R<Map<String, Object>> result = logisticsService.createLogisticsByOrderId(orderId);
    
    if (result.getCode() == 1) {
        log.info("物流单创建成功");
    } else {
        log.error("物流单创建失败：{}", result.getMsg());
    }
}
```

## 工具方法使用

```java
/**
 * 获取订单类型描述
 */
public void getOrderTypeInfo(Integer orderType) {
    String description = logisticsService.getOrderTypeDescription(orderType);
    log.info("订单类型 {} 的描述：{}", orderType, description);
}

/**
 * 验证订单类型是否支持
 */
public boolean validateOrderType(Integer orderType) {
    boolean isSupported = logisticsService.isSupportedOrderType(orderType);
    log.info("订单类型 {} 是否支持物流单创建：{}", orderType, isSupported);
    return isSupported;
}
```

## 测试方法使用

```java
/**
 * 测试参数构建（用于调试）
 */
public void testLogisticsParams(CarOrderInfoEntity orderInfo, 
                               List<CarOrderDetailEntity> orderDetails, 
                               CarPaymentOrderEntity paymentOrder) {
    
    if (orderInfo.getOrderType() == 1) {
        // 测试宅配参数构建
        R<Map<String, String>> result = logisticsService.testBuildHomeDeliveryParams(
            orderInfo, orderDetails, paymentOrder);
        log.info("宅配参数构建结果：{}", result.getData());
        
    } else if (orderInfo.getOrderType() == 2) {
        // 测试超商参数构建
        R<Map<String, String>> result = logisticsService.testBuildConvenienceStoreParams(
            orderInfo, orderDetails, paymentOrder);
        log.info("超商参数构建结果：{}", result.getData());
    }
}
```

## 完整示例

```java
@Service
public class OrderService {
    
    @Resource
    private LogisticsService logisticsService;
    
    @Resource
    private CarOrderInfoService carOrderInfoService;
    
    @Resource
    private CarOrderDetailService carOrderDetailService;
    
    @Resource
    private CarPaymentOrderService carPaymentOrderService;
    
    /**
     * 处理订单发货
     */
    @Transactional
    public void processOrderShipment(Long orderId) {
        try {
            // 1. 查询订单信息
            CarOrderInfoEntity orderInfo = carOrderInfoService.getOrderById(orderId);
            if (orderInfo == null) {
                throw new RuntimeException("订单不存在");
            }
            
            // 2. 验证订单状态
            if (orderInfo.getOrderStatus() != 2) { // 2 = 已支付
                throw new RuntimeException("订单状态不正确，无法发货");
            }
            
            // 3. 查询订单详情
            List<CarOrderDetailEntity> orderDetails = carOrderDetailService.getOrderDetailsByOrderId(orderId);
            if (orderDetails == null || orderDetails.isEmpty()) {
                throw new RuntimeException("订单详情不存在");
            }
            
            // 4. 查询支付订单信息
            CarPaymentOrderEntity paymentOrder = carPaymentOrderService.getPaymentOrderByOrderId(orderId);
            if (paymentOrder == null) {
                throw new RuntimeException("支付订单不存在");
            }
            
            // 5. 创建物流单
            R<Map<String, Object>> logisticsResult = logisticsService.createLogisticsByOrderType(
                orderInfo, orderDetails, paymentOrder);
            
            if (logisticsResult.getCode() == 1) {
                // 6. 更新订单状态为已发货
                carOrderInfoService.updateOrderStatus(orderId, 5); // 5 = 已发货
                
                // 7. 记录物流信息
                Map<String, Object> logisticsData = logisticsResult.getData();
                String allPayLogisticsId = (String) logisticsData.get("AllPayLogisticsID");
                
                // 更新订单物流单号
                carOrderInfoService.updateLogisticsInfo(orderId, allPayLogisticsId, 
                    orderInfo.getReceiverName(), orderInfo.getReceiverMobile(), 
                    orderInfo.getReceiverAddress());
                
                log.info("订单 {} 发货成功，物流单号：{}", orderId, allPayLogisticsId);
                
            } else {
                throw new RuntimeException("物流单创建失败：" + logisticsResult.getMsg());
            }
            
        } catch (Exception e) {
            log.error("处理订单发货异常，订单ID：{}", orderId, e);
            throw e;
        }
    }
}
```

## 注意事项

1. **推荐使用统一接口**：`createLogisticsByOrderType()` 方法会自动根据订单类型选择相应的物流方式
2. **订单类型验证**：确保订单类型为 1（宅配到府）或 2（超商取货）
3. **必要信息检查**：
   - 宅配到府：需要收件人姓名、电话、地址
   - 超商取货：需要收件人姓名、手机、超商门店代码
4. **错误处理**：建议对物流单创建结果进行完整的错误处理
5. **状态更新**：物流单创建成功后，记得更新订单状态为已发货
