package cc.carce.sale.service;

import cc.carce.sale.config.EcpayConfig;
import cc.carce.sale.entity.ShippingInfo;
import cc.carce.sale.mapper.ShippingInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ShippingService {

    @Autowired
    private ShippingInfoRepository shippingInfoRepository;

    @Autowired
    private EcpayConfig ecpayConfig;

    private final RestTemplate restTemplate = new RestTemplate();

    // 🚚 Save shipping info (宅配/超商)
    public void saveShippingInfo(String orderNo,
                                 String shippingMethod,
                                 String receiverName,
                                 String receiverPhone,
                                 String receiverAddress,
                                 String storeId) {
        try {
            ShippingInfo info = new ShippingInfo();
            info.setOrderNo(orderNo);
            info.setShippingMethod(shippingMethod);
            info.setReceiverName(receiverName);
            info.setReceiverPhone(receiverPhone);
            info.setReceiverAddress(receiverAddress);
            info.setStoreId(storeId);
            info.setLogisticsStatus("待出貨");

            shippingInfoRepository.save(info);

            System.out.println("🚚 Shipping info saved for order " + orderNo + " | method=" + shippingMethod);
        } catch (Exception e) {
            System.err.println("❌ Failed to save shipping info: " + e.getMessage());
        }
    }

    // 🏪 Called when 超商門市選擇完成 (ECPay Map 回調)
    public void selectedStore(String orderNo, Map<String, String> params) {
        try {
            ShippingInfo info = shippingInfoRepository.findByOrderNo(orderNo);
            if (info == null) {
                System.err.println("⚠️ No shipping info found for order " + orderNo);
                return;
            }

            String storeId = params.get("CVSStoreID");
            String storeName = params.get("CVSStoreName");
            String storeAddress = params.get("CVSAddress");

            info.setStoreId(storeId);
            info.setReceiverAddress(storeAddress); // 存超商地址
            info.setLogisticsStatus("待付款前已選門市");

            shippingInfoRepository.save(info);

            System.out.println("🏪 Store selected for order " + orderNo +
                    " | storeId=" + storeId +
                    " | storeName=" + storeName +
                    " | address=" + storeAddress);
        } catch (Exception e) {
            System.err.println("❌ Failed to update store selection: " + e.getMessage());
        }
    }

    public String createShipment(String orderNo) {
        ShippingInfo info = shippingInfoRepository.findByOrderNo(orderNo);
        if (info == null) {
            System.err.println("⚠️ No shipping info found for order " + orderNo);
            return null;
        }

        String trackingNo = null;

        if ("CVS".equalsIgnoreCase(info.getShippingMethod())) {
            trackingNo = createCvsShipment(info);
        } else if ("HOME".equalsIgnoreCase(info.getShippingMethod())) {
            trackingNo = createHomeShipment(info);
        }

        if (trackingNo != null) {
            info.setTrackingNo(trackingNo);
            info.setLogisticsStatus("配送中");
            shippingInfoRepository.save(info);
        }

        return trackingNo;
    }


    // 🚚 Call ECPay CVS Logistics API
    private String createCvsShipment(ShippingInfo info) {
        String url = ecpayConfig.getLogisticsUrl() + "/Express/Create";

        Map<String, String> params = new LinkedHashMap<>();
        params.put("MerchantID", ecpayConfig.getMerchantId());
        params.put("MerchantTradeNo", info.getOrderNo());
        params.put("MerchantTradeDate", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        params.put("LogisticsType", "CVS");
        params.put("LogisticsSubType", "UNIMART"); // 範例：7-11
        params.put("CVSStoreID", info.getStoreId());
        params.put("ReceiverName", info.getReceiverName());
        params.put("ReceiverPhone", info.getReceiverPhone());
        params.put("GoodsAmount", "100"); // 金額，可根據訂單動態
        params.put("GoodsName", "CarEC 商品");
        params.put("ServerReplyURL", ecpayConfig.getReturnUrl());

        // TODO: 產生 CheckMacValue (和金流一樣的簽章機制)
        // params.put("CheckMacValue", generateCheckMacValue(params));

        String response = restTemplate.postForObject(url, params, String.class);
        System.out.println("📡 CVS Shipment API Response: " + response);

        // TODO: 解析 response，取出 TrackingNo
        return "CVS_DYNAMIC_" + System.currentTimeMillis();
    }

    // 🚚 Call ECPay Home Delivery Logistics API
    private String createHomeShipment(ShippingInfo info) {
        String url = ecpayConfig.getLogisticsUrl() + "/Express/Create";

        Map<String, String> params = new LinkedHashMap<>();
        params.put("MerchantID", ecpayConfig.getMerchantId());
        params.put("MerchantTradeNo", info.getOrderNo());
        params.put("MerchantTradeDate", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        params.put("LogisticsType", "HOME");
        params.put("LogisticsSubType", "TCAT"); // 範例：黑貓宅配
        params.put("ReceiverName", info.getReceiverName());
        params.put("ReceiverPhone", info.getReceiverPhone());
        params.put("ReceiverAddress", info.getReceiverAddress());
        params.put("GoodsAmount", "100");
        params.put("GoodsName", "CarEC 商品");
        params.put("ServerReplyURL", ecpayConfig.getReturnUrl());

        // TODO: 產生 CheckMacValue
        // params.put("CheckMacValue", generateCheckMacValue(params));

        String response = restTemplate.postForObject(url, params, String.class);
        System.out.println("📡 HOME Shipment API Response: " + response);

        // TODO: 解析 response，取出 TrackingNo
        return "HOME_DYNAMIC_" + System.currentTimeMillis();
    }
    
    public ShippingInfo findByTrackingNo(String trackingNo) {
        return shippingInfoRepository.findByTrackingNo(trackingNo);
    }
    
    public ShippingInfo findByOrderNo(String orderNo) {
        return shippingInfoRepository.findByOrderNo(orderNo);
    }
}
