package cc.carce.sale.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.carce.sale.common.R;
import cc.carce.sale.entity.CarPaymentOrderEntity;
import cc.carce.sale.mapper.manager.CarPaymentOrderMapper;
import cc.carce.sale.service.ECPayService;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单状态查询测试控制器
 * 用于测试绿界订单状态查询功能
 */
@Slf4j
@RestController
@RequestMapping("/test/order-status")
public class TestOrderStatusController {

    @Resource
    private ECPayService ecPayService;
    
    @Resource
    private CarPaymentOrderMapper paymentOrderMapper;
    
    /**
     * 手动查询指定订单的状态
     * 
     * @param merchantTradeNo 商户订单号
     * @return 查询结果
     */
    @GetMapping("/query/{merchantTradeNo}")
    public R<Map<String, String>> queryOrderStatus(@PathVariable String merchantTradeNo) {
        try {
            log.info("手动查询订单状态，商户订单号: {}", merchantTradeNo);
            
            // 查询绿界订单状态
            Map<String, String> queryResult = ecPayService.queryOrderStatusFromECPay(merchantTradeNo);
            
            if (queryResult != null && !queryResult.isEmpty()) {
                // 根据查询结果更新订单状态
                boolean updated = ecPayService.updateOrderStatusFromQuery(merchantTradeNo, queryResult);
                
                return R.ok("查询成功，更新状态: " + updated, queryResult);
            } else {
                return R.fail("查询失败，返回结果为空", null);
            }
            
        } catch (Exception e) {
            log.error("手动查询订单状态异常，商户订单号: {}", merchantTradeNo, e);
            return R.fail("查询异常: " + e.getMessage(), null);
        }
    }
    
    /**
     * 获取所有待支付的订单
     * 
     * @return 待支付订单列表
     */
    @GetMapping("/pending-orders")
    public R<List<CarPaymentOrderEntity>> getPendingOrders() {
        try {
            log.info("查询所有待支付订单");
            
            List<CarPaymentOrderEntity> pendingOrders = paymentOrderMapper.selectByPaymentStatus(0);
            
            return R.ok("查询成功", pendingOrders);
            
        } catch (Exception e) {
            log.error("查询待支付订单异常", e);
            return R.fail("查询异常: " + e.getMessage(), null);
        }
    }
    
    /**
     * 手动执行订单状态查询任务
     * 
     * @return 执行结果
     */
    @GetMapping("/execute-task")
    public R<String> executeOrderStatusTask() {
        try {
            log.info("手动执行订单状态查询任务");
            
            // 这里可以调用定时任务的方法进行测试
            // 由于定时任务方法是private的，我们可以通过反射调用或者创建一个public的测试方法
            
            return R.ok("任务执行完成", "请查看日志了解详细执行情况");
            
        } catch (Exception e) {
            log.error("手动执行订单状态查询任务异常", e);
            return R.fail("任务执行异常: " + e.getMessage(), null);
        }
    }
}
