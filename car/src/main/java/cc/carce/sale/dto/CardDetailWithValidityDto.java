package cc.carce.sale.dto;

import lombok.Data;

import java.util.Date;

/**
 * 票券明細（含過期時間），用於訂單詳情展示券碼與過期日
 */
@Data
public class CardDetailWithValidityDto {

    private Long id;
    private Long orderId;
    private Long cardId;
    private String cardName;
    private String ticketCode;
    private Integer redeemed;
    private Date redeemedTime;
    private Date createTime;
    /** 券碼過期時間（由 cardId 關聯 car_card 的 validityType/validityEndDate/validityDays 計算） */
    private Date validityEndTime;
}
