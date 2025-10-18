package cc.carce.sale.service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import cc.carce.sale.common.ECPayUtils;
import cc.carce.sale.common.R;
import cc.carce.sale.config.DsConstants;
import cc.carce.sale.config.ECPayConfig;
import cc.carce.sale.dto.ECPayStoreResponseDto;
import cc.carce.sale.dto.StoreInfoDto;
import cc.carce.sale.dto.StoreListDto;
import cc.carce.sale.entity.CarOrderDetailEntity;
import cc.carce.sale.entity.CarOrderInfoEntity;
import cc.carce.sale.entity.CarPaymentOrderEntity;
import cc.carce.sale.entity.CarStoreInfoEntity;
import cc.carce.sale.mapper.manager.CarPaymentOrderMapper;
import cc.carce.sale.mapper.manager.CarStoreInfoMapper;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 物流服务
 * 
 * 主要功能： 
 * 1. 生成宅配到府物流单（基于绿界科技API）
 * 2. 生成超商取货物流单（基于绿界科技API）
 * 3. 根据订单类型自动选择物流单创建方法（推荐使用）
 * 4. 查询物流订单信息（基于绿界科技API）
 * 5. 获取超商门店列表
 * 6. 物流单状态查询和更新
 * 
 * 使用示例：
 * 
 * <pre>
 * // 注入服务
 * @Resource
 * private LogisticsService logisticsService;
 * 
 * // 推荐：根据订单类型自动创建物流单（统一接口）
 * R&lt;Map&lt;String, Object&gt;&gt; result = logisticsService.createLogisticsByOrderType(orderInfo, orderDetails, paymentOrder);
 * 
 * // 或者分别调用特定方法
 * if (orderInfo.getOrderType() == 1) {
 *     // 宅配到府
 *     result = logisticsService.createHomeDeliveryLogistics(orderInfo, orderDetails, paymentOrder);
 * } else if (orderInfo.getOrderType() == 2) {
 *     // 超商取货
 *     result = logisticsService.createConvenienceStoreLogistics(orderInfo, orderDetails, paymentOrder);
 * }
 * 
 * if (result.getCode() == 1) {
 *     // 创建成功
 *     Map&lt;String, Object&gt; logisticsData = result.getData();
 *     String allPayLogisticsId = (String) logisticsData.get("AllPayLogisticsID");
 * 
 *     // 根据订单类型获取不同的返回信息
 *     if (orderInfo.getOrderType() == 1) {
 *         // 宅配到府
 *         String bookingNote = (String) logisticsData.get("BookingNote");
 *         log.info("宅配物流单创建成功，绿界物流单号：{}，托运单号：{}", allPayLogisticsId, bookingNote);
 *     } else if (orderInfo.getOrderType() == 2) {
 *         // 超商取货
 *         String cvsPaymentNo = (String) logisticsData.get("CVSPaymentNo");
 *         String cvsValidationNo = (String) logisticsData.get("CVSValidationNo");
 *         log.info("超商物流单创建成功，绿界物流单号：{}，寄货编号：{}，验证码：{}", allPayLogisticsId, cvsPaymentNo, cvsValidationNo);
 *     }
 * } else {
 *     // 创建失败
 *     log.error("物流单创建失败：{}", result.getMsg());
 * }
 * </pre>
 */
@Slf4j
@Service
public class LogisticsService {

    @Resource
    private ECPayConfig ecPayConfig;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private ECPayUtils ecPayUtils;

    @Resource
    private CarOrderInfoService carOrderInfoService;

    @Resource
    private CarOrderDetailService carOrderDetailService;

    @Resource
    private CarPaymentOrderMapper carPaymentOrderMapper;

    @Resource
    private CarStoreInfoMapper carStoreInfoMapper;

    @Resource
    private RedisTemplate<String, String> redis;

    private static final String STORE_LIST_KEY = "ecpay:store:list";


    /**
     * 调用绿界门店查询API（实际实现）
     * 根据绿界文档：https://developers.ecpay.com.tw/?p=47496
     * 获取所有合作超商的门店清单
     * 
     * @param cvsType 超商类别，可选值：All, FAMI, UNIMART, HILIFE, OKMART, UNIMARTFREEZE
     * @return 门店查询结果
     */
    public List<StoreListDto> callECPayStoreAPI(String cvsType) {
        List<StoreListDto> storeList = new ArrayList<>();
        try {

            log.info("调用绿界门店查询API，超商类别：{}", cvsType);

            // 构建请求参数 - 总是获取所有门店数据
            Map<String, Object> params = new TreeMap<>();
            params.put("MerchantID", ecPayConfig.getMerchantId());
            params.put("CvsType", "All"); // 总是获取所有门店数据
            params.put("PlatformID", ""); // 特约合作平台商代号，一般厂商为空

            // 生成检查码
            String checkMacValue = ecPayUtils.generateSignatureWithMd5(params);
            params.put("CheckMacValue", checkMacValue);

            // 调用绿界门店查询API
            String apiUrl = getLogisticsApiUrl() + "/Helper/GetStoreList";
            log.info("调用绿界门店查询API，URL：{}", apiUrl);

            // 发送POST请求
            HttpResponse response = HttpRequest.post(apiUrl)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Accept", "text/html")
                    .form(params).timeout(30000) // 30秒超时
                    .execute();

            if (response.isOk()) {
                String responseBody = response.body();
                log.info("绿界门店查询API响应：{}", responseBody);

                // 解析JSON响应
                ECPayStoreResponseDto result = JSONUtil.toBean(responseBody, ECPayStoreResponseDto.class);

                if (result != null && result.getRtnCode() != null && result.getRtnCode() == 1) {
                    log.info("绿界门店查询成功，共{}个超商类别", 
                            result.getStoreList() != null ? result.getStoreList().size() : 0);
                    
                    storeList = result.getStoreList();
                } else {
                    String errorMsg = result != null ? result.getRtnMsg() : "未知错误";
                    log.error("绿界门店查询失败，错误信息：{}", errorMsg);
                }
            } else {
                log.error("绿界门店查询API调用失败，状态码: {}, 响应: {}", response.getStatus(), response.body());
            }
        } catch (Exception e) {
            log.error("调用绿界门店查询API异常，超商类别：{}", cvsType, e);
        }
        
        // 如果请求的不是所有门店，需要过滤
        if(!"All".equalsIgnoreCase(cvsType) && storeList != null){
            storeList = storeList.stream()
                .filter(storeListDto -> storeListDto.getCvsType().equalsIgnoreCase(cvsType))
                .collect(Collectors.toList());
        }
        return storeList;
    }

    /**
     * 生成宅配到府物流单 基于绿界科技宅配到府API：https://developers.ecpay.com.tw/?p=7414
     * 
     * @param orderInfo    订单信息
     * @param orderDetails 订单详情列表
     * @param paymentOrder 支付订单信息
     * @return 物流单创建结果
     */
    public R<Map<String, Object>> createHomeDeliveryLogistics(CarOrderInfoEntity orderInfo,
            List<CarOrderDetailEntity> orderDetails, CarPaymentOrderEntity paymentOrder) {
        try {
            log.info("开始创建宅配物流单，订单号：{}", orderInfo.getOrderNo());

            // 验证必要参数
            if (orderInfo == null || orderDetails == null || paymentOrder == null) {
                return R.fail("订单信息、订单详情或支付订单信息不能为空", null);
            }

            // 验证订单类型是否为宅配到府
            if (!CarOrderInfoEntity.OrderType.HOME_DELIVERY.getCode().equals(orderInfo.getOrderType())) {
                return R.fail("订单类型不是宅配到府，无法创建宅配物流单", null);
            }

            // 验证收件人信息
            if (orderInfo.getReceiverName() == null || orderInfo.getReceiverName().trim().isEmpty()) {
                return R.fail("收件人姓名不能为空", null);
            }
            if (orderInfo.getReceiverMobile() == null || orderInfo.getReceiverMobile().trim().isEmpty()) {
                return R.fail("收件人电话不能为空", null);
            }
            if (orderInfo.getReceiverAddress() == null || orderInfo.getReceiverAddress().trim().isEmpty()) {
                return R.fail("收件人地址不能为空", null);
            }

            // 构建绿界API请求参数
            Map<String, Object> params = buildHomeDeliveryParams(orderInfo, orderDetails, paymentOrder);

            log.info("宅配物流单参数: {}", JSONUtil.toJsonStr(params));

            // 生成检查码
            String checkMacValue = ecPayUtils.generateSignatureWithMd5(params);
            params.put("CheckMacValue", checkMacValue);

            // 调用绿界宅配API
            String apiUrl = getLogisticsApiUrl() + "/Express/Create";
            log.info("调用绿界宅配API，URL：{}", apiUrl);

            HttpResponse response = HttpRequest.post(apiUrl)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Accept", "text/html")
                    .form(params).timeout(30000) // 30秒超时
                    .execute();

            if (response.isOk()) {
                String responseBody = response.body();
                log.info("绿界订单状态查询成功，响应: {}", responseBody);

                // 解析响应结果
                Map<String, Object> result = parseLogisticsResponse(responseBody);

                if (result != null && "1".equals(result.get("RtnCode"))) {
                    // 成功创建物流单
                    log.info("宅配物流单创建成功，订单号：{}，绿界物流单号：{}", orderInfo.getOrderNo(), result.get("AllPayLogisticsID"));

                    // 更新订单物流信息
                    updateOrderLogisticsInfo(orderInfo, result);

                    return R.ok("宅配物流单创建成功", result);
                } else {
                    // 创建失败
                    String errorMsg = result != null ? (String) result.get("RtnMsg") : "未知错误";
                    log.error("宅配物流单创建失败，订单号：{}，错误信息：{}", orderInfo.getOrderNo(), errorMsg);
                    return R.fail("宅配物流单创建失败：" + errorMsg, result);
                }
            } else {
                log.error("绿界订单状态查询失败，状态码: {}, 响应: {}", response.getStatus(), response.body());
                return null;
            }


        } catch (Exception e) {
            log.error("创建宅配物流单异常，订单号：{}", orderInfo.getOrderNo(), e);
            return R.fail("创建宅配物流单异常：" + e.getMessage(), null);
        }
    }

    /**
     * 构建宅配到府API请求参数
     */
    private Map<String, Object> buildHomeDeliveryParams(CarOrderInfoEntity orderInfo,
            List<CarOrderDetailEntity> orderDetails, CarPaymentOrderEntity paymentOrder) {
        Map<String, Object> params = new TreeMap<>();

        // 必填参数
        params.put("MerchantID", ecPayConfig.getMerchantId());
        params.put("MerchantTradeNo", paymentOrder.getMerchantTradeNo());

        // 交易时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        params.put("MerchantTradeDate", sdf.format(paymentOrder.getCreateTime()));

        // 物流类型和子类型
        params.put("LogisticsType", "HOME"); // 宅配
        params.put("LogisticsSubType", "TCAT"); // 黑猫宅配，可根据需要改为POST(中华邮政)

        // 商品金额
        params.put("GoodsAmount", String.valueOf(orderInfo.getTotalPrice()));

        // 代收货款设置
        params.put("IsCollection", "N"); // 不代收货款

        // 收件人信息
        params.put("ReceiverName", orderInfo.getReceiverName());
        params.put("ReceiverPhone", orderInfo.getReceiverMobile());
        params.put("ReceiverAddress", orderInfo.getReceiverAddress());
        params.put("ReceiverZipCode", "10041");

        // 寄件人信息（从配置或固定值获取）
        params.put("SenderName", "汽车商城");
        params.put("SenderPhone", "02-1234-5678");
        params.put("SenderAddress", "台北市信义区信义路五段7号");
        params.put("SenderZipCode", "10041");

        // 商品信息
        StringBuilder goodsName = new StringBuilder();
        for (CarOrderDetailEntity detail : orderDetails) {
            if (goodsName.length() > 0) {
                goodsName.append(",");
            }
            goodsName.append(detail.getProductName());
        }
        params.put("GoodsName", goodsName.toString());

        // 交易描述
        params.put("TradeDesc", paymentOrder.getTradeDesc() != null ? paymentOrder.getTradeDesc() : "汽车商城订单");

        // 服务器回传网址
        params.put("ServerReplyURL", ecPayConfig.getNotifyUrl() + "/logistics/callback");

        // 客户端回传网址（可选）
        params.put("ClientReplyURL", ecPayConfig.getClientBackUrl() + "/order/detail/" + orderInfo.getId());

        // 备注
        params.put("Remark", "订单号：" + orderInfo.getOrderNo());

        // 物流规格（黑猫宅配）
        params.put("Specification", "0001"); // 60cm

        // 预定取件时段
        params.put("ScheduledPickupTime", "4"); // 不限时

        // 预定送达时段
        params.put("ScheduledDeliveryTime", "4"); // 不限时

        return params;
    }

    /**
     * 获取物流API地址
     */
    private String getLogisticsApiUrl() {
        return ecPayConfig.getCurrentLogisticsApiUrl();
    }

    /**
     * 解析物流API响应
     */
    private Map<String, Object> parseLogisticsResponse(String response) {
        try {
            Map<String, Object> result = new HashMap<>();

            if (response == null || response.trim().isEmpty()) {
                return null;
            }

            // 绿界API返回格式：1|MerchantID=XXX&MerchantTradeNo=XXX&RtnCode=XXX&...
            String[] parts = response.split("\\|");
            if (parts.length < 2) {
                log.error("绿界API响应格式错误：{}", response);
                return null;
            }

            String status = parts[0];
            String data = parts[1];

            if (!"1".equals(status)) {
                // 错误响应
                result.put("RtnCode", "0");
                result.put("RtnMsg", data);
                return result;
            }

            // 解析参数
            String[] paramPairs = data.split("&");
            for (String pair : paramPairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    result.put(keyValue[0], keyValue[1]);
                }
            }

            return result;

        } catch (Exception e) {
            log.error("解析绿界API响应异常：{}", response, e);
            return null;
        }
    }

    /**
     * 更新订单物流信息
     */
    private void updateOrderLogisticsInfo(CarOrderInfoEntity orderInfo, Map<String, Object> logisticsResult) {
        try {
            // 这里应该调用订单服务更新物流信息
            // 例如：carOrderInfoService.updateLogisticsInfo(orderInfo.getId(),
            // logisticsResult);

            String allPayLogisticsId = (String) logisticsResult.get("AllPayLogisticsID");
            String bookingNote = (String) logisticsResult.get("BookingNote");

            log.info("更新订单物流信息，订单ID：{}，绿界物流单号：{}，托运单号：{}", orderInfo.getId(), allPayLogisticsId, bookingNote);

            // TODO: 实际项目中需要调用订单服务更新物流单号等信息

        } catch (Exception e) {
            log.error("更新订单物流信息异常，订单ID：{}", orderInfo.getId(), e);
        }
    }

    /**
     * 通过订单ID创建宅配物流单（便捷方法）
     * 
     * @param orderId 订单ID
     * @return 物流单创建结果
     */
    public R<Map<String, Object>> createHomeDeliveryLogisticsByOrderId(Long orderId) {
        try {
            log.info("通过订单ID创建宅配物流单，订单ID：{}", orderId);

            // TODO: 这里需要注入相关的服务来查询订单信息
            // 示例代码：
            /*
             * // 查询订单信息 CarOrderInfoEntity orderInfo =
             * carOrderInfoService.getOrderById(orderId); if (orderInfo == null) { return
             * R.fail("订单不存在", null); }
             * 
             * // 查询订单详情 List<CarOrderDetailEntity> orderDetails =
             * carOrderDetailService.getOrderDetailsByOrderId(orderId); if (orderDetails ==
             * null || orderDetails.isEmpty()) { return R.fail("订单详情不存在", null); }
             * 
             * // 查询支付订单信息 CarPaymentOrderEntity paymentOrder =
             * carPaymentOrderService.getPaymentOrderByOrderId(orderId); if (paymentOrder ==
             * null) { return R.fail("支付订单不存在", null); }
             * 
             * // 调用创建物流单方法 return createHomeDeliveryLogistics(orderInfo, orderDetails,
             * paymentOrder);
             */

            return R.fail("此方法需要注入相关服务才能使用，请直接调用createHomeDeliveryLogistics方法", null);

        } catch (Exception e) {
            log.error("通过订单ID创建宅配物流单异常，订单ID：{}", orderId, e);
            return R.fail("创建宅配物流单异常：" + e.getMessage(), null);
        }
    }


    /**
     * 生成超商取货物流单 基于绿界科技超商取货API：https://developers.ecpay.com.tw/?p=8809
     * 
     * @param orderInfo    订单信息
     * @param orderDetails 订单详情列表
     * @param paymentOrder 支付订单信息
     * @return 物流单创建结果
     */
    public R<Map<String, Object>> createConvenienceStoreLogistics(CarOrderInfoEntity orderInfo,
            List<CarOrderDetailEntity> orderDetails, CarPaymentOrderEntity paymentOrder) {
        try {
            log.info("开始创建超商取货物流单，订单号：{}", orderInfo.getOrderNo());

            // 验证必要参数
            if (orderInfo == null || orderDetails == null || paymentOrder == null) {
                return R.fail("订单信息、订单详情或支付订单信息不能为空", null);
            }

            // 验证订单类型是否为超商取货
            if (!CarOrderInfoEntity.OrderType.CONVENIENCE_STORE.getCode().equals(orderInfo.getOrderType())) {
                return R.fail("订单类型不是超商取货，无法创建超商物流单", null);
            }

            // 验证超商门店信息
            if (orderInfo.getCvsStoreID() == null || orderInfo.getCvsStoreID().trim().isEmpty()) {
                return R.fail("超商门店代码不能为空", null);
            }
            if (orderInfo.getReceiverName() == null || orderInfo.getReceiverName().trim().isEmpty()) {
                return R.fail("收件人姓名不能为空", null);
            }
            if (orderInfo.getReceiverMobile() == null || orderInfo.getReceiverMobile().trim().isEmpty()) {
                return R.fail("收件人手机不能为空", null);
            }

            // 构建绿界API请求参数
            Map<String, String> params = buildConvenienceStoreParams(orderInfo, orderDetails, paymentOrder);

            // 生成检查码
            String checkMacValue = ecPayUtils.generateSignatureWithSha256(params);
            params.put("CheckMacValue", checkMacValue);

            // 调用绿界超商API
            String apiUrl = getLogisticsApiUrl() + "/Express/Create";
            log.info("调用绿界超商API，URL：{}", apiUrl);

            // 构建请求体
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                requestBody.add(entry.getKey(), entry.getValue());
            }

            // 设置请求头，按照绿界API文档要求
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Accept", "text/html");

            // 创建HTTP实体
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            // 发送POST请求
            String response = restTemplate.postForObject(apiUrl, requestEntity, String.class);
            log.info("绿界超商API响应：{}", response);

            // 解析响应结果
            Map<String, Object> result = parseLogisticsResponse(response);

            if (result != null && "1".equals(result.get("RtnCode"))) {
                // 成功创建物流单
                log.info("超商物流单创建成功，订单号：{}，绿界物流单号：{}", orderInfo.getOrderNo(), result.get("AllPayLogisticsID"));

                // 更新订单物流信息
                updateOrderLogisticsInfo(orderInfo, result);

                return R.ok("超商物流单创建成功", result);
            } else {
                // 创建失败
                String errorMsg = result != null ? (String) result.get("RtnMsg") : "未知错误";
                log.error("超商物流单创建失败，订单号：{}，错误信息：{}", orderInfo.getOrderNo(), errorMsg);
                return R.fail("超商物流单创建失败：" + errorMsg, result);
            }

        } catch (Exception e) {
            log.error("创建超商物流单异常，订单号：{}", orderInfo.getOrderNo(), e);
            return R.fail("创建超商物流单异常：" + e.getMessage(), null);
        }
    }

    /**
     * 构建超商取货API请求参数
     */
    private Map<String, String> buildConvenienceStoreParams(CarOrderInfoEntity orderInfo,
            List<CarOrderDetailEntity> orderDetails, CarPaymentOrderEntity paymentOrder) {
        Map<String, String> params = new HashMap<>();

        // 必填参数
        params.put("MerchantID", ecPayConfig.getMerchantId());
        params.put("MerchantTradeNo", paymentOrder.getMerchantTradeNo());

        // 交易时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        params.put("MerchantTradeDate", sdf.format(paymentOrder.getCreateTime()));

        // 物流类型和子类型
        params.put("LogisticsType", "CVS"); // 超商取货

        // 根据门店代码判断超商类型
        String storeId = orderInfo.getCvsStoreID();
        String logisticsSubType = determineLogisticsSubType(storeId);
        params.put("LogisticsSubType", logisticsSubType);

        // 商品金额
        params.put("GoodsAmount", String.valueOf(orderInfo.getTotalPrice()));

        // 代收货款设置
        params.put("IsCollection", "N"); // 不代收货款

        // 收件人信息
        params.put("ReceiverName", orderInfo.getReceiverName());
        params.put("ReceiverCellPhone", orderInfo.getReceiverMobile());

        // 收件人门店代码
        params.put("ReceiverStoreID", orderInfo.getCvsStoreID());

        // 退货车门店代码（可选，仅7-ELEVEN C2C适用）
        if ("UNIMART".equals(logisticsSubType) && orderInfo.getCvsStoreID() != null) {
            params.put("ReturnStoreID", orderInfo.getCvsStoreID());
        }

        // 寄件人信息（从配置或固定值获取）
        params.put("SenderName", "汽车商城");
        params.put("SenderPhone", "02-1234-5678");
        params.put("SenderCellPhone", "02-1234-5678");
        params.put("SenderAddress", "台北市信义区信义路五段7号");

        // 商品信息
        StringBuilder goodsName = new StringBuilder();
        for (CarOrderDetailEntity detail : orderDetails) {
            if (goodsName.length() > 0) {
                goodsName.append(",");
            }
            goodsName.append(detail.getProductName());
        }
        params.put("GoodsName", goodsName.toString());

        // 交易描述
        params.put("TradeDesc", paymentOrder.getTradeDesc() != null ? paymentOrder.getTradeDesc() : "汽车商城订单");

        // 服务器回传网址
        params.put("ServerReplyURL", ecPayConfig.getNotifyUrl() + "/logistics/callback");

        // 客户端回传网址（可选）
        params.put("ClientReplyURL", ecPayConfig.getClientBackUrl() + "/order/detail/" + orderInfo.getId());

        // 备注
        params.put("Remark", "订单号：" + orderInfo.getOrderNo());

        return params;
    }

    /**
     * 根据门店代码判断物流子类型
     */
    private String determineLogisticsSubType(String storeId) {
        if (storeId == null || storeId.trim().isEmpty()) {
            return "UNIMART"; // 默认7-ELEVEN
        }

        // 根据门店代码前缀判断超商类型
        if (storeId.startsWith("131") || storeId.startsWith("896")) {
            return "UNIMART"; // 7-ELEVEN
        } else if (storeId.startsWith("006") || storeId.startsWith("F")) {
            return "FAMI"; // 全家
        } else if (storeId.startsWith("132") || storeId.startsWith("OK")) {
            return "OKMART"; // OK超商
        } else if (storeId.startsWith("H") || storeId.startsWith("L")) {
            return "HILIFE"; // 莱尔富
        } else {
            return "UNIMART"; // 默认7-ELEVEN
        }
    }

    /**
     * 通过订单ID创建超商取货物流单（便捷方法）
     * 
     * @param orderId 订单ID
     * @return 物流单创建结果
     */
    public R<Map<String, Object>> createConvenienceStoreLogisticsByOrderId(Long orderId) {
        try {
            log.info("通过订单ID创建超商取货物流单，订单ID：{}", orderId);

            // TODO: 这里需要注入相关的服务来查询订单信息
            // 示例代码：
            /*
             * // 查询订单信息 CarOrderInfoEntity orderInfo =
             * carOrderInfoService.getOrderById(orderId); if (orderInfo == null) { return
             * R.fail("订单不存在", null); }
             * 
             * // 查询订单详情 List<CarOrderDetailEntity> orderDetails =
             * carOrderDetailService.getOrderDetailsByOrderId(orderId); if (orderDetails ==
             * null || orderDetails.isEmpty()) { return R.fail("订单详情不存在", null); }
             * 
             * // 查询支付订单信息 CarPaymentOrderEntity paymentOrder =
             * carPaymentOrderService.getPaymentOrderByOrderId(orderId); if (paymentOrder ==
             * null) { return R.fail("支付订单不存在", null); }
             * 
             * // 调用创建物流单方法 return createConvenienceStoreLogistics(orderInfo, orderDetails,
             * paymentOrder);
             */

            return R.fail("此方法需要注入相关服务才能使用，请直接调用createConvenienceStoreLogistics方法", null);

        } catch (Exception e) {
            log.error("通过订单ID创建超商取货物流单异常，订单ID：{}", orderId, e);
            return R.fail("创建超商取货物流单异常：" + e.getMessage(), null);
        }
    }

    /**
     * 测试超商取货参数构建功能（用于调试）
     * 
     * @param orderInfo    订单信息
     * @param orderDetails 订单详情列表
     * @param paymentOrder 支付订单信息
     * @return 构建的参数
     */
    public R<Map<String, String>> testBuildConvenienceStoreParams(CarOrderInfoEntity orderInfo,
            List<CarOrderDetailEntity> orderDetails, CarPaymentOrderEntity paymentOrder) {
        try {
            log.info("测试构建超商取货物流单参数，订单号：{}", orderInfo.getOrderNo());

            // 验证必要参数
            if (orderInfo == null || orderDetails == null || paymentOrder == null) {
                return R.fail("订单信息、订单详情或支付订单信息不能为空", null);
            }

            // 构建参数
            Map<String, String> params = buildConvenienceStoreParams(orderInfo, orderDetails, paymentOrder);

            log.info("超商取货参数构建成功，共{}个参数", params.size());
            return R.ok("超商取货参数构建成功", params);

        } catch (Exception e) {
            log.error("测试超商取货参数构建异常，订单号：{}", orderInfo.getOrderNo(), e);
            return R.fail("超商取货参数构建异常：" + e.getMessage(), null);
        }
    }

    /**
     * 根据订单类型自动创建物流单（统一接口） 根据CarOrderInfoEntity.orderType字段自动选择调用宅配到府或超商取货方法
     * 
     * @param orderInfo    订单信息
     * @param orderDetails 订单详情列表
     * @param paymentOrder 支付订单信息
     * @return 物流单创建结果
     */
    public R<Map<String, Object>> createLogisticsByOrderType(CarOrderInfoEntity orderInfo,
            List<CarOrderDetailEntity> orderDetails, CarPaymentOrderEntity paymentOrder) {
        try {
            log.info("根据订单类型创建物流单，订单号：{}，订单类型：{}", orderInfo.getOrderNo(), orderInfo.getOrderType());

            // 验证必要参数
            if (orderInfo == null || orderDetails == null || paymentOrder == null) {
                return R.fail("订单信息、订单详情或支付订单信息不能为空", null);
            }

            // 根据订单类型调用不同的物流单创建方法
            if (CarOrderInfoEntity.OrderType.HOME_DELIVERY.getCode().equals(orderInfo.getOrderType())) {
                // 宅配到府
                log.info("订单类型为宅配到府，调用宅配物流单创建方法");
                return createHomeDeliveryLogistics(orderInfo, orderDetails, paymentOrder);

            } else if (CarOrderInfoEntity.OrderType.CONVENIENCE_STORE.getCode().equals(orderInfo.getOrderType())) {
                // 超商取货
                log.info("订单类型为超商取货，调用超商物流单创建方法");
                return createConvenienceStoreLogistics(orderInfo, orderDetails, paymentOrder);

            } else {
                // 不支持的订单类型
                String errorMsg = String.format("不支持的订单类型：%d，支持的订单类型：1(宅配到府)、2(超商取货)", orderInfo.getOrderType());
                log.error("订单类型不支持，订单号：{}，订单类型：{}", orderInfo.getOrderNo(), orderInfo.getOrderType());
                return R.fail(errorMsg, null);
            }

        } catch (Exception e) {
            log.error("根据订单类型创建物流单异常，订单号：{}，订单类型：{}", orderInfo.getOrderNo(), orderInfo.getOrderType(), e);
            return R.fail("创建物流单异常：" + e.getMessage(), null);
        }
    }

    /**
     * 通过订单ID根据订单类型自动创建物流单（便捷方法）
     * 
     * @param orderId 订单ID
     * @return 物流单创建结果
     */
    public R<Map<String, Object>> createLogisticsByOrderNo(String orderNo) {
        try {
            log.info("通过订单号根据订单类型创建物流单，订单号：{}", orderNo);

            // 查询订单信息
            CarOrderInfoEntity orderInfo = carOrderInfoService.getOrderByOrderNo(orderNo);
            if (orderInfo == null) {
                return R.fail("订单不存在", null);
            }

            // 查询订单详情
            List<CarOrderDetailEntity> orderDetails = carOrderDetailService.getOrderDetailsByOrderId(orderInfo.getId());
            if (orderDetails == null || orderDetails.isEmpty()) {
                return R.fail("订单详情不存在", null);
            }

            // 查询支付订单信息
            CarPaymentOrderEntity paymentOrder = carPaymentOrderMapper.getPaymentOrderByOrderNo(orderNo);
            if (paymentOrder == null) {
                return R.fail("支付订单不存在", null);
            }

            // 调用统一创建物流单方法
            return createLogisticsByOrderType(orderInfo, orderDetails, paymentOrder);

        } catch (Exception e) {
            log.error("通过订单号根据订单类型创建物流单异常，订单号：{}", orderNo, e);
            return R.fail("创建物流单异常：" + e.getMessage(), null);
        }
    }

    /**
     * 获取订单类型描述
     * 
     * @param orderType 订单类型代码
     * @return 订单类型描述
     */
    public String getOrderTypeDescription(Integer orderType) {
        if (orderType == null) {
            return "未知";
        }

        try {
            CarOrderInfoEntity.OrderType type = CarOrderInfoEntity.OrderType.fromCode(orderType);
            return type.getDescription();
        } catch (IllegalArgumentException e) {
            return "不支持的订单类型：" + orderType;
        }
    }

    /**
     * 验证订单类型是否支持物流单创建
     * 
     * @param orderType 订单类型代码
     * @return 是否支持
     */
    public boolean isSupportedOrderType(Integer orderType) {
        if (orderType == null) {
            return false;
        }

        return CarOrderInfoEntity.OrderType.HOME_DELIVERY.getCode().equals(orderType)
                || CarOrderInfoEntity.OrderType.CONVENIENCE_STORE.getCode().equals(orderType);
    }

    /**
     * 查询物流订单信息
     * 基于绿界科技物流查询API：https://developers.ecpay.com.tw/?p=7418
     * 
     * @param merchantTradeNo 商户交易编号
     * @return 物流订单查询结果
     */
    public R<Map<String, Object>> queryLogisticsOrder(String merchantTradeNo) {
        try {
            log.info("开始查询物流订单信息，商户交易编号：{}", merchantTradeNo);
            
            // 验证必要参数
            if (merchantTradeNo == null || merchantTradeNo.trim().isEmpty()) {
                return R.fail("商户交易编号不能为空", null);
            }
            
            // 构建绿界API请求参数
            Map<String, Object> params = buildLogisticsQueryParams(merchantTradeNo);
            
            log.info("物流查询参数: {}", JSONUtil.toJsonStr(params));
            
            // 生成检查码
            String checkMacValue = ecPayUtils.generateSignatureWithMd5(params);
            params.put("CheckMacValue", checkMacValue);
            
            // 调用绿界物流查询API
            String apiUrl = getLogisticsApiUrl() + "/Helper/QueryLogisticsTradeInfo/V5";
            log.info("调用绿界物流查询API，URL：{}", apiUrl);
            
            // 发送POST请求
            HttpResponse response = HttpRequest.post(apiUrl)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Accept", "text/html")
                    .form(params).timeout(30000) // 30秒超时
                    .execute();
            
            if (response.isOk()) {
                String responseBody = response.body();
                log.info("绿界物流查询成功，响应: {}", responseBody);
                
                // 解析响应结果
                Map<String, Object> result = parseLogisticsQueryResponse(responseBody);
                
                if (result != null) {
                    log.info("物流订单查询成功，商户交易编号：{}，物流状态：{}", 
                            merchantTradeNo, result.get("LogisticsStatus"));
                    return R.ok("物流订单查询成功", result);
                } else {
                    log.error("解析物流查询响应失败，商户交易编号：{}", merchantTradeNo);
                    return R.fail("解析物流查询响应失败", null);
                }
            } else {
                log.error("绿界物流查询失败，状态码: {}, 响应: {}", response.getStatus(), response.body());
                return R.fail("物流查询API调用失败，状态码：" + response.getStatus(), null);
            }
            
        } catch (Exception e) {
            log.error("查询物流订单异常，商户交易编号：{}", merchantTradeNo, e);
            return R.fail("查询物流订单异常：" + e.getMessage(), null);
        }
    }
    
    /**
     * 根据绿界物流单号查询物流信息
     * 
     * @param allPayLogisticsId 绿界物流交易编号
     * @return 物流订单查询结果
     */
    public R<Map<String, Object>> queryLogisticsOrderByAllPayId(String allPayLogisticsId) {
        try {
            log.info("开始根据绿界物流单号查询物流信息，绿界物流单号：{}", allPayLogisticsId);
            
            // 验证必要参数
            if (allPayLogisticsId == null || allPayLogisticsId.trim().isEmpty()) {
                return R.fail("绿界物流单号不能为空", null);
            }
            
            // 构建绿界API请求参数
            Map<String, Object> params = buildLogisticsQueryParamsByAllPayId(allPayLogisticsId);
            
            log.info("物流查询参数: {}", JSONUtil.toJsonStr(params));
            
            // 生成检查码
            String checkMacValue = ecPayUtils.generateSignatureWithMd5(params);
            params.put("CheckMacValue", checkMacValue);
            
            // 调用绿界物流查询API
            String apiUrl = getLogisticsApiUrl() + "/Helper/QueryLogisticsTradeInfo/V5";
            log.info("调用绿界物流查询API，URL：{}", apiUrl);
            
            // 发送POST请求
            HttpResponse response = HttpRequest.post(apiUrl)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Accept", "text/html")
                    .form(params).timeout(30000) // 30秒超时
                    .execute();
            
            if (response.isOk()) {
                String responseBody = response.body();
                log.info("绿界物流查询成功，响应: {}", responseBody);
                
                // 解析响应结果
                Map<String, Object> result = parseLogisticsQueryResponse(responseBody);
                
                if (result != null) {
                    log.info("物流订单查询成功，绿界物流单号：{}，物流状态：{}", 
                            allPayLogisticsId, result.get("LogisticsStatus"));
                    return R.ok("物流订单查询成功", result);
                } else {
                    log.error("解析物流查询响应失败，绿界物流单号：{}", allPayLogisticsId);
                    return R.fail("解析物流查询响应失败", null);
                }
            } else {
                log.error("绿界物流查询失败，状态码: {}, 响应: {}", response.getStatus(), response.body());
                return R.fail("物流查询API调用失败，状态码：" + response.getStatus(), null);
            }
            
        } catch (Exception e) {
            log.error("查询物流订单异常，绿界物流单号：{}", allPayLogisticsId, e);
            return R.fail("查询物流订单异常：" + e.getMessage(), null);
        }
    }
    
    /**
     * 构建物流查询API请求参数（根据商户交易编号）
     */
    private Map<String, Object> buildLogisticsQueryParams(String merchantTradeNo) {
        Map<String, Object> params = new TreeMap<>();
        
        // 必填参数
        params.put("MerchantID", ecPayConfig.getMerchantId());
        params.put("MerchantTradeNo", merchantTradeNo);
        
        // 验证时间戳（Unix时间戳）
        long timestamp = System.currentTimeMillis() / 1000;
        params.put("TimeStamp", timestamp);
        
        // 特约合作平台商代号（一般厂商为空）
        params.put("PlatformID", "");
        
        return params;
    }
    
    /**
     * 构建物流查询API请求参数（根据绿界物流单号）
     */
    private Map<String, Object> buildLogisticsQueryParamsByAllPayId(String allPayLogisticsId) {
        Map<String, Object> params = new TreeMap<>();
        
        // 必填参数
        params.put("MerchantID", ecPayConfig.getMerchantId());
        params.put("AllPayLogisticsID", allPayLogisticsId);
        
        // 验证时间戳（Unix时间戳）
        long timestamp = System.currentTimeMillis() / 1000;
        params.put("TimeStamp", timestamp);
        
        // 特约合作平台商代号（一般厂商为空）
        params.put("PlatformID", "");
        
        return params;
    }
    
    /**
     * 解析物流查询API响应
     */
    private Map<String, Object> parseLogisticsQueryResponse(String response) {
        try {
            Map<String, Object> result = new HashMap<>();
            
            if (response == null || response.trim().isEmpty()) {
                return null;
            }
            
            // 绿界API返回格式：参数=值&参数=值&参数=值
            String[] paramPairs = response.split("&");
            for (String pair : paramPairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    
                    // 处理特殊值
                    if ("null".equals(value)) {
                        value = null;
                    }
                    
                    result.put(key, value);
                }
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("解析绿界物流查询API响应异常：{}", response, e);
            return null;
        }
    }
    
    /**
     * 根据订单号查询物流信息（便捷方法）
     * 
     * @param orderNo 订单号
     * @return 物流订单查询结果
     */
    public R<Map<String, Object>> queryLogisticsByOrderNo(String orderNo) {
        try {
            log.info("根据订单号查询物流信息，订单号：{}", orderNo);
            
            // 查询支付订单信息获取商户交易编号
            CarPaymentOrderEntity paymentOrder = carPaymentOrderMapper.getPaymentOrderByOrderNo(orderNo);
            if (paymentOrder == null) {
                return R.fail("支付订单不存在", null);
            }
            
            // 调用物流查询接口
            return queryLogisticsOrder(paymentOrder.getMerchantTradeNo());
            
        } catch (Exception e) {
            log.error("根据订单号查询物流信息异常，订单号：{}", orderNo, e);
            return R.fail("查询物流信息异常：" + e.getMessage(), null);
        }
    }
    
    /**
     * 获取物流状态描述
     * 
     * @param logisticsStatus 物流状态代码
     * @return 物流状态描述
     */
    public String getLogisticsStatusDescription(String logisticsStatus) {
        if (logisticsStatus == null) {
            return "未知状态";
        }
        
        switch (logisticsStatus) {
            case "300":
                return "訂單建立";
            case "301":
                return "待收件";
            case "302":
                return "已收件";
            case "303":
                return "配送中";
            case "304":
                return "已取件";
            case "305":
                return "已送達";
            case "306":
                return "退貨中";
            case "307":
                return "已退貨";
            case "308":
                return "配送異常";
            case "309":
                return "已取消";
            default:
                return "未知状态：" + logisticsStatus;
        }
    }
    

    @Transactional(rollbackFor = Exception.class, transactionManager = DsConstants.tranManager)
    public void getAndSaveStoreList(){
        List<CarStoreInfoEntity> list = new ArrayList<>();
        List<StoreListDto> storeList = callECPayStoreAPI("ALL");
        for (StoreListDto store : storeList) {
            for (StoreInfoDto storeInfo : store.getStoreInfo()) {
                CarStoreInfoEntity carStoreInfo = new CarStoreInfoEntity();
                carStoreInfo.setStoreId(storeInfo.getStoreId());
                carStoreInfo.setStoreName(storeInfo.getStoreName());
                carStoreInfo.setStoreAddr(storeInfo.getStoreAddr());
                carStoreInfo.setStorePhone(storeInfo.getStorePhone());
                carStoreInfo.setCvsType(store.getCvsType());
                list.add(carStoreInfo);
            }
        }
        carStoreInfoMapper.deleteAll();
        carStoreInfoMapper.batchInsert(list);
    }

    @PostConstruct
    public void test() {
        // createLogisticsByOrderNo("202510160111246740");
        queryLogisticsByOrderNo("202510160111246740");
        // getAndSaveStoreList();
    }
}
