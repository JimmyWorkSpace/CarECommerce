package cc.carce.sale.service;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cc.carce.sale.common.ECPayUtils;
import cc.carce.sale.common.R;
import cc.carce.sale.config.ECPayConfig;
import cc.carce.sale.dto.ECPayResultDto;
import cc.carce.sale.dto.ECPayReturnResultDto;
import cc.carce.sale.entity.CarOrderDetailEntity;
import cc.carce.sale.entity.CarOrderInfoEntity;
import cc.carce.sale.entity.CarPaymentOrderEntity;
import cc.carce.sale.entity.CarProductsEntity;
import cc.carce.sale.form.CartItem;
import cc.carce.sale.form.CreatePaymentForm;
import cc.carce.sale.mapper.manager.CarPaymentOrderMapper;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
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

    @Resource
    private LogisticsService logisticsService;
    
    // /**
    //  * 创建支付订单
    //  */
    // @Transactional
    // public R<Map<String, String>> createPayment(Long userId, Integer amount, String itemName, String description) {
    //     try {
    //         // 获取当前环境配置
    //         String activeProfile = System.getProperty("spring.profiles.active");
    //         if (activeProfile == null) {
    //             activeProfile = "dev"; // 默认使用dev环境
    //         }
            
    //         Integer finalAmount = amount;
            
    //         // 生成商户订单号
    //         String merchantTradeNo = generateMerchantTradeNo();
            
    //         // 创建支付订单记录
    //         CarPaymentOrderEntity paymentOrder = new CarPaymentOrderEntity();
    //         paymentOrder.setMerchantTradeNo(merchantTradeNo);
    //         paymentOrder.setUserId(userId);
    //         paymentOrder.setTotalAmount(finalAmount);
    //         paymentOrder.setItemName(itemName);
    //         paymentOrder.setTradeDesc(description);
    //         paymentOrder.setPaymentStatus(CarPaymentOrderEntity.PaymentStatus.PENDING.getCode());
    //         paymentOrder.setCreateTime(new Date());
    //         paymentOrder.setUpdateTime(new Date());
    //         paymentOrder.setDelFlag(false);
            
    //         // 保存到数据库
    //         int result = paymentOrderMapper.insert(paymentOrder);
    //         if (result <= 0) {
    //             log.error("保存支付订单失败，用户ID: {}, 金额: {}", userId, finalAmount);
    //             return R.fail("創建支付訂單失敗", null);
    //         }
            

    //         CarOrderInfoEntity orderInfo = carOrderInfoService.getOrderByOrderNo(paymen);
    //         // 构建绿界支付参数
    //         Map<String, String> paymentParams = ecPayUtils.buildPaymentParams(
    //             merchantTradeNo,
    //             description,
    //             finalAmount,
    //             itemName
    //         );
            
    //         log.info("创建支付订单成功，商户订单号: {}, 用户ID: {}, 金额: {}, 环境: {}", 
    //                 merchantTradeNo, userId, finalAmount, activeProfile);
            
    //         return R.ok(paymentParams);
            
    //     } catch (Exception e) {
    //         log.error("创建支付订单异常", e);
    //         return R.fail("創建支付訂單異常: " + e.getMessage(), null);
    //     }
    // }
    
    /**
     * 处理绿界支付回调
     */
    @Transactional
    public String handlePaymentCallback(Map<String, String> callbackParams) {
        try {
            String merchantTradeNo = callbackParams.get("MerchantTradeNo");
            String ecpayTradeNo = callbackParams.get("TradeNo");
            String paymentType = callbackParams.get("PaymentType");
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

            logisticsService.createLogisticsByOrderNo(merchantTradeNo);
            
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
            

            String orderNo = paymentOrder.getMerchantTradeNo();
            CarOrderInfoEntity orderInfo = carOrderInfoService.getOrderByOrderNo(orderNo);
            paymentOrder.setOrderId(orderInfo.getId());
            return R.ok(paymentOrder);
            
        } catch (Exception e) {
            log.error("查询支付订单状态异常", e);
            return R.fail("查詢支付訂單狀態異常", null);
        }
    }
    
    /**
     * 根据商户订单号获取支付订单
     */
    public CarPaymentOrderEntity getPaymentOrderByMerchantTradeNo(String merchantTradeNo) {
        try {
            return paymentOrderMapper.selectByMerchantTradeNo(merchantTradeNo);
        } catch (Exception e) {
            log.error("根据商户订单号获取支付订单异常，商户订单号: {}", merchantTradeNo, e);
            return null;
        }
    }
    
    /**
     * 根据绿界支付交易号获取支付订单
     */
    public CarPaymentOrderEntity getPaymentOrderByEcpayTradeNo(String ecpayTradeNo) {
        try {
            return paymentOrderMapper.selectByEcpayTradeNo(ecpayTradeNo);
        } catch (Exception e) {
            log.error("根据绿界支付交易号获取支付订单异常，绿界支付交易号: {}", ecpayTradeNo, e);
            return null;
        }
    }
    
    /**
     * 获取订单信息服务
     */
    public CarOrderInfoService getOrderInfoService() {
        return carOrderInfoService;
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
            
            // 检查是否是重新支付订单
            CarOrderInfoEntity existingOrder = null;
            if (form.getOrderId() != null) {
                existingOrder = carOrderInfoService.getOrderById(form.getOrderId());
                if (existingOrder != null && existingOrder.getUserId().equals(userId)) {
                    // 使用现有订单的订单号作为商户订单号
                    merchantTradeNo = existingOrder.getOrderNo();
                    log.info("重新支付订单，使用现有订单号: {}", merchantTradeNo);
                } else {
                    log.warn("订单不存在或无权限访问，用户ID: {}, 订单ID: {}", userId, form.getOrderId());
                    return R.fail("订单不存在或无权限访问", null);
                }
            }
            
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
            
            // 创建或使用现有业务订单
            CarOrderInfoEntity orderInfo;
            if (existingOrder != null) {
                // 重新支付，使用现有订单
                orderInfo = existingOrder;
                log.info("使用现有订单进行重新支付，订单ID: {}", orderInfo.getId());
            } else {
                // 新订单，创建业务订单
                orderInfo = carOrderInfoService.createOrder(
                    userId, merchantTradeNo, form, orderDetails);
                
                if (orderInfo == null) {
                    log.error("创建业务订单失败，用户ID: {}", userId);
                    return R.fail("創建訂單失敗", null);
                }
                
                // 设置订单类型和超商信息
                if (form.getOrderType() != null) {
                    orderInfo.setOrderType(form.getOrderType());
                }
                
                // 如果是超商取货，设置超商信息
                if (form.getOrderType() != null && form.getOrderType() == 2) {
                    orderInfo.setCvsStoreID(form.getCvsStoreID());
                    orderInfo.setCvsStoreName(form.getCvsStoreName());
                    orderInfo.setCvsAddress(form.getCvsAddress());
                    orderInfo.setCvsTelephone(form.getCvsTelephone());
                    orderInfo.setCvsOutSide(form.getCvsOutSide());
                }
                
                // 更新订单信息到数据库
                carOrderInfoService.updateOrder(orderInfo);
            }
            
            // 删除购物车中已下单的商品（仅在新订单时）
            if (existingOrder == null && form.getCartData() != null && !form.getCartData().isEmpty()) {
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
                orderInfo.getId(),
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
    
    /**
     * 验证付款结果通知的检查码
     * 
     * @param request HTTP请求对象
     * @return 验证是否通过
     */
    public boolean verifyPaymentResult(HttpServletRequest request) {
        try {
            // 获取检查码
            String checkMacValue = request.getParameter("CheckMacValue");
            if (checkMacValue == null || checkMacValue.trim().isEmpty()) {
                log.error("付款结果通知缺少检查码");
                return false;
            }
            
            // 构建验证参数
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> parameterMap = request.getParameterMap();
            
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                if (values != null && values.length > 0 && !"CheckMacValue".equals(key)) {
                    params.put(key, values[0]);
                }
            }
            
            // 使用ECPayUtils验证检查码
            String calculatedCheckMac = ecPayUtils.generateSignatureWithSha256(params);
            
            boolean isValid = checkMacValue.equals(calculatedCheckMac);
            if (!isValid) {
                log.error("付款结果通知检查码验证失败 - 接收到的: {}, 计算出的: {}", checkMacValue, calculatedCheckMac);
            } else {
                log.info("付款结果通知检查码验证成功");
            }
            
            return isValid;
            
        } catch (Exception e) {
            log.error("验证付款结果通知检查码时发生异常", e);
            return false;
        }
    }
    
    /**
     * 处理付款结果通知
     * 
     * @param merchantId 特店编号
     * @param merchantTradeNo 特店交易编号
     * @param rtnCode 交易状态
     * @param rtnMsg 交易消息
     * @param tradeNo 绿界交易编号
     * @param tradeAmt 交易金额
     * @param paymentDate 付款时间
     * @param paymentType 付款方式
     * @param checkMacValue 检查码
     * @return 处理是否成功
     */
    @Transactional
    public boolean processPaymentResult(ECPayReturnResultDto dto) {
        try {
            String merchantTradeNo = dto.getMerchantTradeNo();
            log.info("开始处理付款结果 - 特店交易编号: {}, 交易状态: {}", dto.getMerchantTradeNo(), dto.getRtnCode());
            
            // 查找支付订单
            CarPaymentOrderEntity paymentOrder = paymentOrderMapper.selectByMerchantTradeNo(merchantTradeNo);
            if (paymentOrder == null) {
                log.error("未找到对应的支付订单 - 特店交易编号: {}", merchantTradeNo);
                return false;
            }
            
            // 检查订单状态，避免重复处理
            if (paymentOrder.getPaymentStatus() != null && paymentOrder.getPaymentStatus() == 1) {
                log.warn("订单已处理过付款结果 - 特店交易编号: {}", merchantTradeNo);
                return true;
            }
            
            // 根据交易状态更新订单
            if ("1".equals(dto.getRtnCode())) {
                // 付款成功
                paymentOrder.setPaymentStatus(1); // 1表示已支付
                paymentOrder.setPaymentTime(new Date());
                paymentOrder.setEcpayTradeNo(dto.getTradeNo());
                paymentOrder.setPaymentType(dto.getPaymentType());
                paymentOrder.setPaymentMessage(dto.getRtnMsg());
                
                // 更新支付订单
                paymentOrderMapper.updateByPrimaryKeySelective(paymentOrder);
                
                // 更新关联的订单状态
                if (StrUtil.isNotBlank(merchantTradeNo)) {
                    CarOrderInfoEntity orderInfo = carOrderInfoService.getOrderByOrderNo(merchantTradeNo);
                    if (orderInfo != null) {
                        carOrderInfoService.updateOrderStatus(orderInfo.getId(), 2); // 2表示已支付
                        
                        log.info("订单状态已更新为已支付 - 订单ID: {}, 特店交易编号: {}", 
                                orderInfo.getId(), merchantTradeNo);
                    }
                }
                
                log.info("付款成功处理完成 - 特店交易编号: {}, 绿界交易编号: {}, 交易金额: {}", 
                        merchantTradeNo, dto.getTradeNo(), dto.getTradeAmt());
                
            } else {
                // 付款失败
                paymentOrder.setPaymentStatus(2); // 2表示支付失败
                paymentOrder.setPaymentMessage(dto.getRtnMsg());
                paymentOrder.setEcpayTradeNo(dto.getTradeNo());
                
                // 更新支付订单
                paymentOrderMapper.updateByPrimaryKeySelective(paymentOrder);
                
                // 更新关联的订单状态
                if (paymentOrder.getOrderId() != null) {
                    CarOrderInfoEntity orderInfo = carOrderInfoService.getOrderById(paymentOrder.getOrderId());
                    if (orderInfo != null) {
                        carOrderInfoService.updateOrderStatus(orderInfo.getId(), 4); // 4表示支付失败
                        
                        log.info("订单状态已更新为支付失败 - 订单ID: {}, 特店交易编号: {}", 
                                orderInfo.getId(), merchantTradeNo);
                    }
                }
                
                log.warn("付款失败处理完成 - 特店交易编号: {}, 交易状态: {}, 失败原因: {}", 
                        merchantTradeNo, dto.getRtnCode(), dto.getRtnMsg());
            }
            
            return true;
            
        } catch (Exception e) {
            log.error("处理付款结果时发生异常 - 特店交易编号: {}", dto.getMerchantTradeNo(), e);
            return false;
        }
    }
    
    /**
     * 查询绿界订单状态
     * 根据绿界支付官方文档：https://developers.ecpay.com.tw/?p=2890
     * 
     * @param merchantTradeNo 特店交易编号
     * @return 查询结果
     */
    public ECPayResultDto queryOrderStatusFromECPay(String merchantTradeNo) {
        try {
            log.info("开始查询绿界订单状态，商户订单号: {}", merchantTradeNo);
            
            // 构建查询参数
            Map<String, String> params = new HashMap<>();
            params.put("MerchantID", ecPayConfig.getMerchantId());
            params.put("MerchantTradeNo", merchantTradeNo);
            params.put("TimeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            
            // 生成检查码
            String checkMacValue = ecPayUtils.generateSignatureWithSha256(params);
            params.put("CheckMacValue", checkMacValue);
            
            // 从配置文件获取查询API地址
            String queryUrl = ecPayConfig.getQueryServerUrl();
            
            log.info("查询绿界订单状态，请求URL: {}, 参数: {}", queryUrl, params);
            
            // 使用Hutool的HttpUtil发送POST请求
            Map<String, Object> formParams = new HashMap<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formParams.put(entry.getKey(), entry.getValue());
            }
            
            HttpResponse response = HttpRequest.post(queryUrl)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .form(formParams)
                .timeout(30000) // 30秒超时
                .execute();
            
            if (response.isOk()) {
                String responseBody = response.body();
                log.info("绿界订单状态查询成功，响应: {}", responseBody);
                
                // 解析响应结果
                return parseQueryResponse(responseBody);
            } else {
                log.error("绿界订单状态查询失败，状态码: {}, 响应: {}", 
                         response.getStatus(), response.body());
                return null;
            }
            
        } catch (Exception e) {
            log.error("查询绿界订单状态异常，商户订单号: {}", merchantTradeNo, e);
            return null;
        }
    }
    
    /**
     * 解析查询响应结果
     * 
     * @param responseBody 响应体
     * @return 解析后的ECPayResultDto对象
     */
    private ECPayResultDto parseQueryResponse(String responseBody) {
        try {
            if (responseBody != null && !responseBody.trim().isEmpty()) {
                log.info("开始解析绿界查询响应: {}", responseBody);
                
                // 绿界返回的是URL编码的查询字符串格式，如：
                // CustomField1=&CustomField2=&CustomField3=&CustomField4=&HandlingCharge=0&ItemName=...
                
                // 先解析为Map
                Map<String, String> paramMap = new HashMap<>();
                
                // 按&分割参数
                String[] params = responseBody.split("&");
                for (String param : params) {
                    if (param.contains("=")) {
                        String[] keyValue = param.split("=", 2);
                        if (keyValue.length == 2) {
                            String key = keyValue[0];
                            String value = keyValue[1];
                            
                            // URL解码
                            try {
                                key = URLDecoder.decode(key, "UTF-8");
                                value = URLDecoder.decode(value, "UTF-8");
                            } catch (Exception e) {
                                log.warn("URL解码失败，参数: {}={}", key, value, e);
                            }
                            
                            paramMap.put(key, value);
                            log.debug("解析参数: {}={}", key, value);
                        }
                    }
                }
                
                log.info("解析完成，共解析到 {} 个参数", paramMap.size());
                
                // 使用JSONUtil将Map转换为ECPayResultDto对象
                return JSONUtil.toBean(JSONUtil.toJsonStr(paramMap), ECPayResultDto.class);
            }
        } catch (Exception e) {
            log.error("解析查询响应结果异常", e);
        }
        
        return new ECPayResultDto();
    }
    
    /**
     * 根据查询结果更新支付订单状态
     * 
     * @param merchantTradeNo 商户订单号
     * @param queryResult 查询结果
     * @return 是否更新成功
     */
    @Transactional
    public boolean updateOrderStatusFromQuery(String merchantTradeNo, ECPayResultDto queryResult) {
        try {
            if (queryResult == null) {
                log.warn("查询结果为空，商户订单号: {}", merchantTradeNo);
                return false;
            }
            
            // 验证返回的检查码
            String receivedCheckMac = queryResult.getCheckMacValue();
            if (receivedCheckMac == null) {
                log.error("查询结果中缺少检查码，商户订单号: {}", merchantTradeNo);
                return false;
            }
            
            // 验证检查码
            // Map<String, String> verifyParams = new HashMap<>(queryResult);
            // verifyParams.remove("CheckMacValue");
            // String calculatedCheckMac = ecPayUtils.generateSignature(verifyParams);
            
            // if (!receivedCheckMac.equalsIgnoreCase(calculatedCheckMac)) {
            //     log.error("查询结果检查码验证失败，商户订单号: {}", merchantTradeNo);
            //     return false;
            // }
            
            // 获取交易状态
            String tradeStatus = queryResult.getTradeStatus();
            if (tradeStatus == null) {
                log.error("查询结果中缺少交易状态，商户订单号: {}", merchantTradeNo);
                return false;
            }
            
            // 查找支付订单
            CarPaymentOrderEntity paymentOrder = paymentOrderMapper.selectByMerchantTradeNo(merchantTradeNo);
            if (paymentOrder == null) {
                log.error("未找到对应的支付订单，商户订单号: {}", merchantTradeNo);
                return false;
            }
            
            // 检查是否已经处理过
            if (CarPaymentOrderEntity.PaymentStatus.SUCCESS.getCode().equals(paymentOrder.getPaymentStatus()) ||
                CarPaymentOrderEntity.PaymentStatus.CANCELLED.getCode().equals(paymentOrder.getPaymentStatus())) {
                log.info("订单状态已确定，无需更新，商户订单号: {}, 当前状态: {}", 
                        merchantTradeNo, paymentOrder.getPaymentStatus());
                return true;
            }
            
            // 根据交易状态更新订单
            boolean updated = false;
            if ("1".equals(tradeStatus)) {
                // 交易成功
                paymentOrder.setPaymentStatus(CarPaymentOrderEntity.PaymentStatus.SUCCESS.getCode());
                paymentOrder.setEcpayTradeNo(queryResult.getTradeNo());
                paymentOrder.setPaymentType(queryResult.getPaymentType());
                paymentOrder.setPaymentTime(parsePaymentDate(queryResult.getPaymentDate()));
                paymentOrder.setEcpayStatus("SUCCESS");
                paymentOrder.setEcpayStatusDesc("支付成功");
                updated = true;
                
                log.info("订单状态更新为支付成功，商户订单号: {}", merchantTradeNo);
                
            } else if ("10200095".equals(tradeStatus)) {
                // 交易失败
                paymentOrder.setPaymentStatus(CarPaymentOrderEntity.PaymentStatus.FAILED.getCode());
                paymentOrder.setEcpayStatus("FAILED");
                paymentOrder.setEcpayStatusDesc("交易失败");
                updated = true;
                
                log.info("订单状态更新为支付失败，商户订单号: {}", merchantTradeNo);
                
            } else if ("0".equals(tradeStatus)) {
                // 未付款，保持待支付状态
                log.info("订单仍为未付款状态，商户订单号: {}", merchantTradeNo);
                return true;
            }
            
            if (updated) {
                // 更新支付订单
                paymentOrderMapper.updateByPrimaryKeySelective(paymentOrder);
                
                // 更新关联的业务订单状态
                if (paymentOrder.getEcpayTradeNo() != null) {
                    CarOrderInfoEntity orderInfo = carOrderInfoService.getOrderByOrderNo(paymentOrder.getEcpayTradeNo());
                    if (orderInfo != null) {
                        if ("1".equals(tradeStatus)) {
                            carOrderInfoService.updateOrderStatus(orderInfo.getId(), CarOrderInfoEntity.OrderStatus.PAID.getCode());
                        } else if ("10200095".equals(tradeStatus)) {
                            carOrderInfoService.updateOrderStatus(orderInfo.getId(), CarOrderInfoEntity.OrderStatus.PAYMENT_FAILED.getCode());
                        }
                        
                        log.info("业务订单状态已同步更新，订单号: {}", orderInfo.getOrderNo());
                    }
                }
            }
            
            return true;
            
        } catch (Exception e) {
            log.error("根据查询结果更新订单状态异常，商户订单号: {}", merchantTradeNo, e);
            return false;
        }
    }
    
    /**
     * 解析支付时间
     * 格式：yyyy/MM/dd HH:mm:ss
     */
    private Date parsePaymentDate(String paymentDateStr) {
        try {
            if (paymentDateStr == null || paymentDateStr.trim().isEmpty()) {
                return null;
            }
            
            // 简单的时间解析，实际项目中建议使用更robust的日期解析
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            return sdf.parse(paymentDateStr);
        } catch (Exception e) {
            log.error("解析支付时间异常: {}", paymentDateStr, e);
            return null;
        }
    }
}
