package cc.carce.sale.controller;

import cc.carce.sale.entity.ReservationEntity;
import cc.carce.sale.service.ReservationService;
import cc.carce.sale.service.SmsService;
import cc.carce.sale.service.SmsService.SmsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private SmsService smsService;

    // 固定通知給車商的手機號碼
    private final String DEALER_MOBILE = "0911222333";

    @PostMapping("/submit")
    public String submitReservation(@RequestBody ReservationEntity reservation) {
        // 1. Save to DB
        ReservationEntity saved = reservationService.saveReservation(reservation);

        // 2. Send SMS to customer
        String customerMsg = String.format(
                "親愛的 %s 您好，預約已完成！時間: %s, 地點: %s",
                saved.getName(), saved.getDateTime(), saved.getLocation()
        );
        SmsResponse customerResult = smsService.sendSms(saved.getMobile(), customerMsg);

        // 3. Send SMS to dealer
        String dealerMsg = String.format(
                "有新的預約: 姓名: %s, 電話: %s, 時間: %s, 地點: %s",
                saved.getName(), saved.getMobile(), saved.getDateTime(), saved.getLocation()
        );
        SmsResponse dealerResult = smsService.sendSms(DEALER_MOBILE, dealerMsg);

        // 4. Build response message
        StringBuilder resultMsg = new StringBuilder("Reservation submitted and saved to DB. ");

        if (customerResult.isSuccess()) {
            resultMsg.append("客戶簡訊發送成功；");
        } else {
            resultMsg.append("客戶簡訊發送失敗，原因: ").append(customerResult.getRawResponse()).append("；");
        }

        if (dealerResult.isSuccess()) {
            resultMsg.append("車商簡訊發送成功。");
        } else {
            resultMsg.append("車商簡訊發送失敗，原因: ").append(dealerResult.getRawResponse());
        }

        return resultMsg.toString();
    }
}
