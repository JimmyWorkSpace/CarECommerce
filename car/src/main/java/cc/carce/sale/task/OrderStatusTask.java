package cc.carce.sale.task;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cc.carce.sale.dto.ECPayResultDto;
import cc.carce.sale.entity.CarPaymentOrderEntity;
import cc.carce.sale.mapper.manager.CarPaymentOrderMapper;
import cc.carce.sale.service.ECPayService;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单状态查询定时任务
 * 每隔一分钟查询未完成订单的状态，超过15分钟的设置为已超时不再查询
 */
@Slf4j
@Component
public class OrderStatusTask {

    @Resource
    private CarPaymentOrderMapper paymentOrderMapper;
    
    @Resource
    private ECPayService ecPayService;
    
    /**
     * 查询订单状态定时任务
     * 每隔一分钟执行一次
     * 查询条件：
     * 1. 支付状态为待支付(0)
     * 2. 创建时间在15分钟内
     * 3. 未删除的订单
     */
    // @PostConstruct
   @Scheduled(fixedRate = 300000) // 每5分钟执行一次
    public void queryOrderStatusTask() {
        try {
            log.info("开始执行订单状态查询定时任务");
            
            // 查询待支付的订单
            List<CarPaymentOrderEntity> pendingOrders = paymentOrderMapper.selectPendingOrdersForQuery();
            
            if (CollUtil.isEmpty(pendingOrders)) {
                log.info("没有找到需要查询状态的待支付订单");
                return;
            }
            
            log.info("找到 {} 个待支付订单需要查询状态", pendingOrders.size());
            
            int successCount = 0;
            int failCount = 0;
            int timeoutCount = 0;
            
            for (CarPaymentOrderEntity order : pendingOrders) {
                try {
                    
                    // 查询绿界订单状态
                    log.info("查询订单状态：{}", order.getMerchantTradeNo());
                    ECPayResultDto queryResult = ecPayService.queryOrderStatusFromECPay(order.getMerchantTradeNo());
                    
                    if (queryResult != null) {
                        // 根据查询结果更新订单状态
                        boolean updated = ecPayService.updateOrderStatusFromQuery(order.getMerchantTradeNo(), queryResult);
                        if (updated) {
                            successCount++;
                            log.info("订单 {} 状态查询并更新成功", order.getMerchantTradeNo());
                        } else {
                            failCount++;
                            log.warn("订单 {} 状态查询成功但更新失败", order.getMerchantTradeNo());
                        }
                    } else {
                        failCount++;
                        log.warn("订单 {} 状态查询失败，返回结果为空", order.getMerchantTradeNo());
                    }
                    
                    // 避免请求过于频繁，每次查询后稍微等待
                    Thread.sleep(100); // 等待100毫秒
                    
                } catch (Exception e) {
                    failCount++;
                    log.error("查询订单 {} 状态时发生异常", order.getMerchantTradeNo(), e);
                }
            }
            
            log.info("订单状态查询定时任务执行完成，成功：{}，失败：{}，超时：{}", 
                    successCount, failCount, timeoutCount);
            
        } catch (Exception e) {
            log.error("执行订单状态查询定时任务时发生异常", e);
        }
    }
    
    /**
     * 标记订单为超时
     * 
     * @param order 支付订单
     */
    private void markOrderAsTimeout(CarPaymentOrderEntity order) {
        try {
            // 更新订单状态为已取消（超时）
            int result = paymentOrderMapper.updatePaymentStatus(
                order.getId(),
                CarPaymentOrderEntity.PaymentStatus.CANCELLED.getCode(),
                "TIMEOUT",
                "订单超时自动取消",
                null
            );
            
            if (result > 0) {
                log.info("订单 {} 已标记为超时取消", order.getMerchantTradeNo());
            } else {
                log.error("订单 {} 标记为超时取消失败", order.getMerchantTradeNo());
            }
            
        } catch (Exception e) {
            log.error("标记订单 {} 为超时时发生异常", order.getMerchantTradeNo(), e);
        }
    }
}
