package cc.carce.sale.service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.carce.sale.config.EcpayConfig;
import cc.carce.sale.entity.PaymentLog;
import cc.carce.sale.mapper.PaymentLogRepository;

@Service
public class PaymentService {

    @Autowired
    private EcpayConfig ecpayConfig;

    @Autowired
    private PaymentLogRepository paymentLogRepository;

    /**
     * Generate checkout form for ECPay
     */
    public String generatePaymentForm(String tradeNo, String itemName, BigDecimal amount, String returnUrl) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("MerchantID", ecpayConfig.getMerchantId());
        params.put("MerchantTradeNo", tradeNo);
        params.put("MerchantTradeDate", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        params.put("PaymentType", "aio");
        params.put("TotalAmount", String.valueOf(amount));
        params.put("TradeDesc", "CarEC Demo Payment");
        params.put("ItemName", itemName);
        params.put("ReturnURL", returnUrl);
        params.put("ChoosePayment", "ALL"); 
        params.put("EncryptType", "1");

        String checkMacValue = generateCheckMacValue(params);
        params.put("CheckMacValue", checkMacValue);

        StringBuilder formBuilder = new StringBuilder();
        formBuilder.append("<form id='ecpay' method='post' action='").append(ecpayConfig.getServiceUrl()).append("'>");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formBuilder.append("<input type='hidden' name='").append(entry.getKey())
                       .append("' value='").append(entry.getValue()).append("'>");
        }
        formBuilder.append("<input type='submit' value='Pay Now'>");
        formBuilder.append("</form>");
        formBuilder.append("<script>document.getElementById('ecpay').submit();</script>");

        return formBuilder.toString();
    }

    /**
     * Verify CheckMacValue from ECPay return params
     */
    public boolean verifyCheckMacValue(Map<String, String> params) {
        if (!params.containsKey("CheckMacValue")) return false;

        String receivedMac = params.get("CheckMacValue");
        Map<String, String> filtered = new LinkedHashMap<>(params);
        filtered.remove("CheckMacValue");

        String calculatedMac = generateCheckMacValue(filtered);
        return receivedMac.equalsIgnoreCase(calculatedMac);
    }

    /**
     * Save payment result into DB (success or fail)
     */
    public void savePaymentResult(Map<String, String> params) {
        try {
            PaymentLog log = new PaymentLog();
            log.setMerchantTradeNo(params.get("MerchantTradeNo"));
            log.setRtnCode(params.get("RtnCode"));
            log.setRtnMsg(params.get("RtnMsg"));
            log.setTradeAmt(params.get("TradeAmt"));
            log.setPaymentType(params.get("PaymentType"));
            log.setTradeNo(params.get("TradeNo"));
            log.setRawParams(params.toString());
            log.setCreatedAt(java.time.LocalDateTime.now());

            paymentLogRepository.save(log);

            if ("1".equals(params.get("RtnCode"))) {
                System.out.println("✅ Payment SUCCESS logged: " + log.getMerchantTradeNo());
            } else {
                System.out.println("⚠️ Payment FAILED logged: " + log.getMerchantTradeNo() + " | Msg: " + log.getRtnMsg());
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to save payment log: " + e.getMessage());
        }
    }

    /**
     * Generate CheckMacValue
     */
    private String generateCheckMacValue(Map<String, String> params) {
        try {
            List<String> keys = new ArrayList<>(params.keySet());
            Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);

            StringBuilder sb = new StringBuilder("HashKey=" + ecpayConfig.getHashKey());
            for (String key : keys) {
                sb.append("&").append(key).append("=").append(params.get(key));
            }
            sb.append("&HashIV=").append(ecpayConfig.getHashIv());

            String encoded = URLEncoder.encode(sb.toString(), "UTF-8")
                    .toLowerCase()
                    .replace("%21", "!")
                    .replace("%28", "(")
                    .replace("%29", ")")
                    .replace("%2a", "*")
                    .replace("%20", "+");

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(encoded.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString().toUpperCase();

        } catch (Exception e) {
            throw new RuntimeException("Error generating CheckMacValue", e);
        }
    }
}
