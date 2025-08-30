package cc.carce.sale.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.carce.sale.config.EcpayConfig;
import cc.carce.sale.service.PaymentService;
import cc.carce.sale.service.ShippingService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private EcpayConfig ecpayConfig;

    @Autowired
    private ShippingService shippingService;  // 🔹 add shipping service

    /**
     * Checkout API: generate ECPay payment form
     * Also saves shipping info
     */
    @GetMapping("/checkout")
    public String checkout(@RequestParam String orderNo,
                           @RequestParam BigDecimal amount,
                           @RequestParam(defaultValue = "CarEC 商品") String itemName,
                           @RequestParam String shippingMethod,                // 🔹 新增 配送方式
                           @RequestParam(required = false) String receiverName, // 宅配用
                           @RequestParam(required = false) String receiverPhone,
                           @RequestParam(required = false) String receiverAddress,
                           @RequestParam(required = false) String storeId       // 超商取貨用
    ) {
        // 🔹 Save shipping info before payment
        shippingService.saveShippingInfo(orderNo, shippingMethod, receiverName, receiverPhone, receiverAddress, storeId);

        // Continue with payment
        return paymentService.generatePaymentForm(orderNo, itemName, amount, ecpayConfig.getReturnUrl());
    }

    /**
     * ECPay ReturnURL: receive transaction results (success & failure)
     */
    @PostMapping("/ecpay/return")
    public String ecpayReturn(@RequestParam Map<String, String> params) {
        System.out.println("===== ECPay Return Notification =====");
        params.forEach((k, v) -> System.out.println(k + " = " + v));

        // 1. Verify CheckMacValue
        boolean valid = paymentService.verifyCheckMacValue(params);
        if (!valid) {
            System.err.println("❌ Invalid CheckMacValue! Possible fake callback.");
            return "0|Fail"; // reject, ECPay will retry
        }

        // 2. Save payment result
        paymentService.savePaymentResult(params);

        // 3. If payment success, continue logistics
        if ("1".equals(params.get("RtnCode"))) {
            String orderNo = params.get("MerchantTradeNo");
            String trackingNo = shippingService.createShipment(orderNo);
            System.out.println("✅ Shipment created, trackingNo=" + trackingNo);
        }

        // 4. Always reply OK to ECPay
        return "1|OK";
    }
}
