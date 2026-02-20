package cc.carce.sale.dto;

import lombok.Data;

import java.util.Date;

/**
 * 我的票券列表項（含狀態：未核銷／已核銷／已過期）
 */
@Data
public class MyTicketItemDto {

    private Long id;
    private Long orderId;
    private String cardName;
    /** 票券序號（券碼） */
    private String ticketCode;
    /** 目前狀態：未核銷、已核銷、已過期 */
    private String status;
    private Date createTime;
    private Date redeemedTime;
    /** 過期時間（未核銷時有效） */
    private Date validityEndTime;
}
