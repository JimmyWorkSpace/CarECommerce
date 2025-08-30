package cc.carce.sale.controller;

import cc.carce.sale.config.EcpayConfig;
import cc.carce.sale.entity.ShippingInfo;
import cc.carce.sale.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private EcpayConfig ecpayConfig;

    /**
     * 1️⃣ 使用者在 checkout 頁選擇「超商取貨」後，點擊「選擇超商門市」
     *    - 導向 ECPay 超商地圖頁
     */
    @GetMapping("/selectStore")
    public void selectStore(@RequestParam String orderNo,
                            @RequestParam(defaultValue = "UNIMART") String logisticsSubType, // UNIMART=7-11, FAMI=全家, HILIFE=萊爾富, OKMART=OK
                            HttpServletResponse response) throws IOException {

        String url = ecpayConfig.getMapUrl()
                + "?MerchantID=" + ecpayConfig.getMerchantId()
                + "&LogisticsSubType=" + logisticsSubType
                + "&IsCollection=N" // 是否取貨付款 (N=否 / Y=是)
                + "&ServerReplyURL=" + ecpayConfig.getStoreCallbackUrl() + "?orderNo=" + orderNo;

        System.out.println("🔗 Redirecting to ECPay Map: " + url);
        response.sendRedirect(url);
    }

    /**
     * 2️⃣ 使用者在 ECPay Map 選完門市後 → ECPay POST 回來
     *    - 呼叫 ShippingService.selectedStore 存入 DB
     *    - 回傳 JS，更新 checkout 頁面的 hidden input
     */
    @PostMapping("/storeCallback")
    public ResponseEntity<String> storeCallback(@RequestParam String orderNo,
                                                @RequestParam Map<String, String> params) {
        try {
            shippingService.selectedStore(orderNo, params);

            String js = "<script>"
                    + "window.opener.document.getElementById('storeId').value='" + params.get("CVSStoreID") + "';"
                    + "window.opener.document.getElementById('storeName').value='" + params.get("CVSStoreName") + "';"
                    + "window.opener.document.getElementById('storeAddress').value='" + params.get("CVSAddress") + "';"
                    + "window.close();"
                    + "</script>";

            return ResponseEntity.ok()
                    .header("Content-Type", "text/html; charset=UTF-8")
                    .body(js);

        } catch (Exception e) {
            System.err.println("❌ Store callback failed for order " + orderNo + ": " + e.getMessage());
            return ResponseEntity.status(500).body("Store selection failed");
        }
    }

    /**
     * 3️⃣ 查詢物流狀態
     *    - 可以用 trackingNo (物流單號) 或 orderNo (商店訂單號)
     */
    @GetMapping("/status")
    public ResponseEntity<?> getShippingStatus(@RequestParam(required = false) String trackingNo,
                                               @RequestParam(required = false) String orderNo) {
        ShippingInfo info = null;

        if (trackingNo != null) {
            info = shippingService.findByTrackingNo(trackingNo);
        } else if (orderNo != null) {
            info = shippingService.findByOrderNo(orderNo);
        }

        if (info == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(info);
    }
}
