package cc.carce.sale.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cc.carce.sale.common.ECPayUtils;
import cc.carce.sale.common.R;
import cc.carce.sale.config.ECPayConfig;
import cc.carce.sale.entity.CarOrderDetailEntity;
import cc.carce.sale.entity.CarOrderInfoEntity;
import cc.carce.sale.entity.CarPaymentOrderEntity;
import cc.carce.sale.entity.CarProductsEntity;
import cc.carce.sale.form.CartItem;
import cc.carce.sale.form.CreatePaymentForm;
import cc.carce.sale.mapper.manager.CarPaymentOrderMapper;
import lombok.extern.slf4j.Slf4j;

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
    private CarPaymentOrderMapper paymentOrderMapper;
    
    @Resource
    private CarOrderInfoService carOrderInfoService;
    
    @Resource
    private CarOrderDetailService carOrderDetailService;
    
    @Resource
    private CarProductsService carProductsService;
    
    @Resource
    private CarShoppingCartService carShoppingCartService;
    
    /**
     * 创建支付订单
     */
    @Transactional
    public R<Map<String, String>> createPayment(Long userId, Integer amount, String itemName, String description) {
        try {
            // 获取当前环境配置
            String activeProfile = System.getProperty("spring.profiles.active");
            if (activeProfile == null) {
                activeProfile = "dev"; // 默认使用dev环境
            }
            
            Integer finalAmount = amount;
            
            // 生成商户订单号
            String merchantTradeNo = generateMerchantTradeNo();
            
            // 创建支付订单记录
            CarPaymentOrderEntity paymentOrder = new CarPaymentOrderEntity();
            paymentOrder.setMerchantTradeNo(merchantTradeNo);
            paymentOrder.setUserId(userId);
            paymentOrder.setTotalAmount(finalAmount);
            paymentOrder.setItemName(itemName);
            paymentOrder.setTradeDesc(description);
            paymentOrder.setPaymentStatus(CarPaymentOrderEntity.PaymentStatus.PENDING.getCode());
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
                finalAmount,
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
            CarPaymentOrderEntity paymentOrder = paymentOrderMapper.selectByMerchantTradeNo(merchantTradeNo);
            if (paymentOrder == null) {
                log.error("未找到对应的支付订单，商户订单号: {}", merchantTradeNo);
                return "0|ErrorMessage";
            }
            
            // 检查是否已经处理过
            if (CarPaymentOrderEntity.PaymentStatus.SUCCESS.getCode().equals(paymentOrder.getPaymentStatus())) {
                log.info("支付订单已处理，商户订单号: {}", merchantTradeNo);
                return "1|OK";
            }
            
            // 更新支付状态
            CarPaymentOrderEntity.PaymentStatus paymentStatus = CarPaymentOrderEntity.PaymentStatus.SUCCESS;
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
                
                // 更新业务订单状态为已支付
                try {
                    // 根据支付订单中的业务订单号查找业务订单
                    if (paymentOrder.getMerchantTradeNo() != null) {
                        CarOrderInfoEntity orderInfo = carOrderInfoService.getOrderByOrderNo(paymentOrder.getMerchantTradeNo());
                        if (orderInfo != null) {
                            carOrderInfoService.updateOrderStatus(orderInfo.getId(), CarOrderInfoEntity.OrderStatus.PAID.getCode());
                            log.info("业务订单状态更新为已支付，订单号: {}", orderInfo.getOrderNo());
                        } else {
                            log.warn("未找到对应的业务订单，订单号: {}", paymentOrder.getMerchantTradeNo());
                        }
                    } else {
                        log.warn("支付订单中未保存业务订单号，商户订单号: {}", merchantTradeNo);
                    }
                } catch (Exception e) {
                    log.error("更新业务订单状态失败，商户订单号: {}", merchantTradeNo, e);
                }
                
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
    public R<CarPaymentOrderEntity> queryPaymentStatus(String merchantTradeNo) {
        try {
            CarPaymentOrderEntity paymentOrder = paymentOrderMapper.selectByMerchantTradeNo(merchantTradeNo);
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
            CarPaymentOrderEntity paymentOrder = paymentOrderMapper.selectByMerchantTradeNo(merchantTradeNo);
            if (paymentOrder == null) {
                return R.fail("支付訂單不存在", null);
            }
            
            // 检查订单所属用户
            if (!paymentOrder.getUserId().equals(userId)) {
                return R.fail("無權限操作此訂單", null);
            }
            
            // 检查订单状态
            if (!CarPaymentOrderEntity.PaymentStatus.PENDING.getCode().equals(paymentOrder.getPaymentStatus())) {
                return R.fail("訂單狀態不允許取消", null);
            }
            
            // 更新订单状态为已取消
            int result = paymentOrderMapper.updatePaymentStatus(
                paymentOrder.getId(),
                CarPaymentOrderEntity.PaymentStatus.CANCELLED.getCode(),
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
     * 检查是否为生产环境
     */
    public boolean isProductionEnvironment() {
        return ecPayConfig.isProduction();
    }
    
    /**
     * 获取支付服务器地址
     */
    public String getPaymentServerUrl() {
        return ecPayConfig.getCurrentServerUrl();
    }
    
    /**
     * 获取当前环境名称
     */
    public String getCurrentEnvironment() {
        String activeProfile = System.getProperty("spring.profiles.active");
        if (activeProfile == null) {
            activeProfile = "dev";
        }
        return activeProfile;
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
    
    /**
     * 创建支付订单并创建业务订单
     */
    @Transactional
    public R<Map<String, String>> createPaymentWithOrder(Long userId, CreatePaymentForm form) {
        try {
            // 获取当前环境配置
            String activeProfile = System.getProperty("spring.profiles.active");
            if (activeProfile == null) {
                activeProfile = "dev"; // 默认使用dev环境
            }
            
            // 如果是dev或test环境，金额固定为1元
            Integer finalAmount = form.getAmount();
            if ("dev".equals(activeProfile) || "test".equals(activeProfile)) {
                finalAmount = 1;
                log.info("开发/测试环境，支付金额固定为1元，原始金额: {}", form.getAmount());
            }
            
            // 生成商户订单号
            String merchantTradeNo = generateMerchantTradeNo();
            
            // 解析购物车数据并重新计算金额
            List<CarOrderDetailEntity> orderDetails = new ArrayList<>();
            int totalAmount = 0;
            if (form.getCartData() != null && !form.getCartData().isEmpty()) {
                try {
                    for (CartItem cartItem : form.getCartData()) {
                        Long productId = cartItem.getProductId();
                        Integer productAmount = cartItem.getProductAmount();
                        
                        // 从products表查询market_price
                        CarProductsEntity product = carProductsService.getProductById(productId);
                        if (product == null) {
                            log.error("商品不存在，商品ID：{}", productId);
                            return R.fail("商品不存在，商品ID：" + productId, null);
                        }
                        
                        // 使用market_price作为商品价格
                        int productPrice = product.getMarketPrice().intValue();
                        int itemTotal = productPrice * productAmount;
                        totalAmount += itemTotal;
                        
                        CarOrderDetailEntity detail = new CarOrderDetailEntity();
                        detail.setProductId(productId);
                        detail.setProductName(cartItem.getProductName());
                        detail.setProductAmount(productAmount);
                        detail.setProductPrice(productPrice);
                        orderDetails.add(detail);
                    }
                } catch (Exception e) {
                    log.error("处理购物车数据失败", e);
                    return R.fail("購物車數據處理失敗", null);
                }
            }
            
            // 使用重新计算的金额
            finalAmount = totalAmount;
            
            // 创建业务订单
            CarOrderInfoEntity orderInfo = carOrderInfoService.createOrder(
                userId, form.getReceiverName(), form.getReceiverMobile(), form.getReceiverAddress(), orderDetails);
            
            if (orderInfo == null) {
                log.error("创建业务订单失败，用户ID: {}", userId);
                return R.fail("創建訂單失敗", null);
            }
            
            // 删除购物车中已下单的商品
            if (form.getCartData() != null && !form.getCartData().isEmpty()) {
                for (CartItem cartItem : form.getCartData()) {
                    // 这里需要根据购物车ID删除，但CartItem中没有ID
                    // 我们需要通过productId和userId来删除购物车商品
                    carShoppingCartService.removeFromCartByProductId(userId, cartItem.getProductId());
                }
                log.info("已删除购物车中的商品，用户ID: {}, 订单号: {}", userId, orderInfo.getOrderNo());
            }
            
            // 创建支付订单记录
            CarPaymentOrderEntity paymentOrder = new CarPaymentOrderEntity();
            paymentOrder.setMerchantTradeNo(merchantTradeNo);
            paymentOrder.setUserId(userId);
            paymentOrder.setTotalAmount(finalAmount);
            paymentOrder.setItemName(form.getItemName());
            paymentOrder.setTradeDesc(form.getDescription());
            paymentOrder.setPaymentStatus(CarPaymentOrderEntity.PaymentStatus.PENDING.getCode());
            paymentOrder.setCreateTime(new Date());
            paymentOrder.setUpdateTime(new Date());
            paymentOrder.setDelFlag(false);
            // 保存业务订单号，用于支付回调时查找业务订单
            paymentOrder.setEcpayTradeNo(orderInfo.getOrderNo());
            
            // 保存支付订单到数据库
            int result = paymentOrderMapper.insert(paymentOrder);
            if (result <= 0) {
                log.error("保存支付订单失败，用户ID: {}, 金额: {}", userId, finalAmount);
                return R.fail("創建支付訂單失敗", null);
            }
            
            // 构建绿界支付参数
            Map<String, String> paymentParams = ecPayUtils.buildPaymentParams(
                merchantTradeNo,
                form.getDescription(),
                finalAmount,
                form.getItemName()
            );
            
            log.info("支付订单创建成功，商户订单号: {}, 用户ID: {}, 金额: {}", 
                    merchantTradeNo, userId, finalAmount);
            
            return R.ok("支付訂單創建成功", paymentParams);
            
        } catch (Exception e) {
            log.error("创建支付订单异常", e);
            return R.fail("創建支付訂單異常: " + e.getMessage(), null);
        }
    }
}
