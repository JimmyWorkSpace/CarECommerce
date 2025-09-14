package cc.carce.sale.controller;

import cc.carce.sale.entity.ReservationEntity;
import cc.carce.sale.service.ReservationService;
import cc.carce.sale.service.SmsService;
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
        smsService.sendSms(saved.getMobile(), customerMsg);

        // 3. Send SMS to dealer
        String dealerMsg = String.format(
                "有新的預約: 姓名: %s, 電話: %s, 時間: %s, 地點: %s",
                saved.getName(), saved.getMobile(), saved.getDateTime(), saved.getLocation()
        );
        smsService.sendSms(DEALER_MOBILE, dealerMsg);

        return "Reservation submitted, saved to DB, and SMS notifications sent.";
    }
}
