package cc.carce.sale.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

public class ShippingService {

    @Resource
    private CarOrderInfoService carOrderInfoService;

    // public String createShipment(String orderNo) {
    //     ShippingInfo info = shippingInfoRepository.findByOrderNo(orderNo);
    //     if (info == null) {
    //         System.err.println("⚠️ No shipping info found for order " + orderNo);
    //         return null;
    //     }

    //     String trackingNo = null;

    //     if ("CVS".equalsIgnoreCase(info.getShippingMethod())) {
    //         trackingNo = createCvsShipment(info);
    //     } else if ("HOME".equalsIgnoreCase(info.getShippingMethod())) {
    //         trackingNo = createHomeShipment(info);
    //     }

    //     if (trackingNo != null) {
    //         info.setTrackingNo(trackingNo);
    //         info.setLogisticsStatus("配送中");
    //         shippingInfoRepository.save(info);
    //     }

    //     return trackingNo;
    // }


    // // 🚚 Call ECPay CVS Logistics API
    // private String createCvsShipment(ShippingInfo info) {
    //     String url = ecpayConfig.getLogisticsUrl() + "/Express/Create";

    //     Map<String, String> params = new LinkedHashMap<>();
    //     params.put("MerchantID", ecpayConfig.getMerchantId());
    //     params.put("MerchantTradeNo", info.getOrderNo());
    //     params.put("MerchantTradeDate", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
    //     params.put("LogisticsType", "CVS");
    //     params.put("LogisticsSubType", "UNIMART"); // 範例：7-11
    //     params.put("CVSStoreID", info.getStoreId());
    //     params.put("ReceiverName", info.getReceiverName());
    //     params.put("ReceiverPhone", info.getReceiverPhone());
    //     params.put("GoodsAmount", "100"); // 金額，可根據訂單動態
    //     params.put("GoodsName", "CarEC 商品");
    //     params.put("ServerReplyURL", ecpayConfig.getReturnUrl());

    //     // TODO: 產生 CheckMacValue (和金流一樣的簽章機制)
    //     // params.put("CheckMacValue", generateCheckMacValue(params));

    //     String response = restTemplate.postForObject(url, params, String.class);
    //     System.out.println("📡 CVS Shipment API Response: " + response);

    //     // TODO: 解析 response，取出 TrackingNo
    //     return "CVS_DYNAMIC_" + System.currentTimeMillis();
    // }

    // // 🚚 Call ECPay Home Delivery Logistics API
    // private String createHomeShipment(ShippingInfo info) {
    //     String url = ecpayConfig.getLogisticsUrl() + "/Express/Create";

    //     Map<String, String> params = new LinkedHashMap<>();
    //     params.put("MerchantID", ecpayConfig.getMerchantId());
    //     params.put("MerchantTradeNo", info.getOrderNo());
    //     params.put("MerchantTradeDate", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
    //     params.put("LogisticsType", "HOME");
    //     params.put("LogisticsSubType", "TCAT"); // 範例：黑貓宅配
    //     params.put("ReceiverName", info.getReceiverName());
    //     params.put("ReceiverPhone", info.getReceiverPhone());
    //     params.put("ReceiverAddress", info.getReceiverAddress());
    //     params.put("GoodsAmount", "100");
    //     params.put("GoodsName", "CarEC 商品");
    //     params.put("ServerReplyURL", ecpayConfig.getReturnUrl());

    //     // TODO: 產生 CheckMacValue
    //     // params.put("CheckMacValue", generateCheckMacValue(params));

    //     String response = restTemplate.postForObject(url, params, String.class);
    //     System.out.println("📡 HOME Shipment API Response: " + response);

    //     // TODO: 解析 response，取出 TrackingNo
    //     return "HOME_DYNAMIC_" + System.currentTimeMillis();
    // }
}
