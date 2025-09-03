package cc.carce.sale.service;

import cc.carce.sale.common.ECPayUtils;
import cc.carce.sale.common.R;
import cc.carce.sale.config.ECPayConfig;
import cc.carce.sale.entity.PaymentOrderEntity;
import cc.carce.sale.mapper.PaymentOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 绿界支付服务
 */
@Slf4j
@Service
public class ECPayService {
    
    @Resource
    private ECPayConfig ecPayConfig;
    
    @Resource
    private ECPayUtils ecPayUtils;
    
    @Resource
    private PaymentOrderMapper paymentOrderMapper;
    
    /**
     * 创建支付订单
     */
    @Transactional
    public R<Map<String, String>> createPayment(Long userId, BigDecimal amount, String itemName, String description) {
        try {
            // 获取当前环境配置
            String activeProfile = System.getProperty("spring.profiles.active");
            if (activeProfile == null) {
                activeProfile = "dev"; // 默认使用dev环境
            }
            
            // 如果是dev或test环境，金额固定为1
            BigDecimal finalAmount = amount;
            if ("dev".equals(activeProfile) || "test".equals(activeProfile)) {
                finalAmount = BigDecimal.ONE;
                log.info("开发/测试环境，支付金额固定为1元，原始金额: {}", amount);
            }
            
            // 生成商户订单号
            String merchantTradeNo = generateMerchantTradeNo();
            
            // 创建支付订单记录
            PaymentOrderEntity paymentOrder = new PaymentOrderEntity();
            paymentOrder.setMerchantTradeNo(merchantTradeNo);
            paymentOrder.setUserId(userId);
            paymentOrder.setTotalAmount(finalAmount);
            paymentOrder.setItemName(itemName);
            paymentOrder.setTradeDesc(description);
            paymentOrder.setPaymentStatus(PaymentOrderEntity.PaymentStatus.PENDING.getCode());
            paymentOrder.setCreateTime(new Date());
            paymentOrder.setUpdateTime(new Date());
            paymentOrder.setDelFlag(false);
            
            // 保存到数据库
            int result = paymentOrderMapper.insert(paymentOrder);
            if (result <= 0) {
                log.error("保存支付订单失败，用户ID: {}, 金额: {}", userId, finalAmount);
                return R.fail("創建支付訂單失敗", null);
            }
            
            // 构建绿界支付参数
            Map<String, String> paymentParams = ecPayUtils.buildPaymentParams(
                merchantTradeNo,
                description,
                finalAmount.intValue(),
                itemName
            );
            
            log.info("创建支付订单成功，商户订单号: {}, 用户ID: {}, 金额: {}, 环境: {}", 
                    merchantTradeNo, userId, finalAmount, activeProfile);
            
            return R.ok(paymentParams);
            
        } catch (Exception e) {
            log.error("创建支付订单异常", e);
            return R.fail("創建支付訂單異常: " + e.getMessage(), null);
        }
    }
    
    /**
     * 处理绿界支付回调
     */
    @Transactional
    public String handlePaymentCallback(Map<String, String> callbackParams) {
        try {
            String merchantTradeNo = callbackParams.get("MerchantTradeNo");
            String ecpayTradeNo = callbackParams.get("TradeNo");
            String paymentDate = callbackParams.get("PaymentDate");
            String paymentType = callbackParams.get("PaymentType");
            String paymentTypeChargeFee = callbackParams.get("PaymentTypeChargeFee");
            String simulatePaid = callbackParams.get("SimulatePaid");
            String checkMacValue = callbackParams.get("CheckMacValue");
            
            log.info("收到绿界支付回调，商户订单号: {}, 绿界交易号: {}, 支付方式: {}", 
                    merchantTradeNo, ecpayTradeNo, paymentType);
            
            // 验证签名
            if (!ecPayUtils.verifySignature(callbackParams, checkMacValue)) {
                log.error("绿界支付回调签名验证失败，商户订单号: {}", merchantTradeNo);
                return "0|ErrorMessage";
            }
            
            // 查询支付订单
            PaymentOrderEntity paymentOrder = paymentOrderMapper.selectByMerchantTradeNo(merchantTradeNo);
            if (paymentOrder == null) {
                log.error("未找到对应的支付订单，商户订单号: {}", merchantTradeNo);
                return "0|ErrorMessage";
            }
            
            // 检查是否已经处理过
            if (PaymentOrderEntity.PaymentStatus.SUCCESS.getCode().equals(paymentOrder.getPaymentStatus())) {
                log.info("支付订单已处理，商户订单号: {}", merchantTradeNo);
                return "1|OK";
            }
            
            // 更新支付状态
            PaymentOrderEntity.PaymentStatus paymentStatus = PaymentOrderEntity.PaymentStatus.SUCCESS;
            String ecpayStatus = "SUCCESS";
            String ecpayStatusDesc = "支付成功";
            
            // 如果是模拟支付，标记为成功
            if ("Y".equals(simulatePaid)) {
                ecpayStatusDesc = "模拟支付成功";
            }
            
            // 更新订单状态
            int updateResult = paymentOrderMapper.updatePaymentStatus(
                paymentOrder.getId(),
                paymentStatus.getCode(),
                ecpayStatus,
                ecpayStatusDesc,
                new Date()
            );
            
            if (updateResult > 0) {
                log.info("支付订单状态更新成功，商户订单号: {}, 状态: {}", 
                        merchantTradeNo, paymentStatus.getDescription());
                
                // 这里可以添加其他业务逻辑，比如：
                // 1. 更新商品库存
                // 2. 发送支付成功通知
                // 3. 记录支付日志
                
                return "1|OK";
            } else {
                log.error("支付订单状态更新失败，商户订单号: {}", merchantTradeNo);
                return "0|ErrorMessage";
            }
            
        } catch (Exception e) {
            log.error("处理绿界支付回调异常", e);
            return "0|ErrorMessage";
        }
    }
    
    /**
     * 查询支付订单状态
     */
    public R<PaymentOrderEntity> queryPaymentStatus(String merchantTradeNo) {
        try {
            PaymentOrderEntity paymentOrder = paymentOrderMapper.selectByMerchantTradeNo(merchantTradeNo);
            if (paymentOrder == null) {
                return R.fail("支付訂單不存在", null);
            }
            
            return R.ok(paymentOrder);
            
        } catch (Exception e) {
            log.error("查询支付订单状态异常", e);
            return R.fail("查詢支付訂單狀態異常", null);
        }
    }
    
    /**
     * 取消支付订单
     */
    @Transactional
    public R<Boolean> cancelPayment(String merchantTradeNo, Long userId) {
        try {
            PaymentOrderEntity paymentOrder = paymentOrderMapper.selectByMerchantTradeNo(merchantTradeNo);
            if (paymentOrder == null) {
                return R.fail("支付訂單不存在", null);
            }
            
            // 检查订单所属用户
            if (!paymentOrder.getUserId().equals(userId)) {
                return R.fail("無權限操作此訂單", null);
            }
            
            // 检查订单状态
            if (!PaymentOrderEntity.PaymentStatus.PENDING.getCode().equals(paymentOrder.getPaymentStatus())) {
                return R.fail("訂單狀態不允許取消", null);
            }
            
            // 更新订单状态为已取消
            int result = paymentOrderMapper.updatePaymentStatus(
                paymentOrder.getId(),
                PaymentOrderEntity.PaymentStatus.CANCELLED.getCode(),
                "CANCELLED",
                "用户取消",
                null
            );
            
            if (result > 0) {
                log.info("支付订单取消成功，商户订单号: {}", merchantTradeNo);
                return R.ok(true);
            } else {
                return R.fail("取消支付訂單失敗", null);
            }
            
        } catch (Exception e) {
            log.error("取消支付订单异常", e);
            return R.fail("取消支付訂單異常", null);
        }
    }
    
    /**
     * 生成商户订单号
     */
    private String generateMerchantTradeNo() {
        // 格式：年月日时分秒+4位随机数
        String timestamp = String.format("%tY%<tm%<td%<tH%<tM%<tS", new Date());
        String random = String.format("%04d", (int)(Math.random() * 10000));
        return timestamp + random;
    }
}
