package com.ruoyi.car.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 票券明細詳情 VO（含所屬用戶用戶名、手機號）
 *
 * @author ruoyi
 */
@Data
public class CarCardDetailVo {

    private Long id;
    private Long orderId;
    private Long cardId;
    private String cardName;
    private String ticketCode;
    private Integer redeemed;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date redeemedTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Long redeemedUserId;

    /** 所屬用戶用戶名（訂單對應用戶） */
    private String buyerUserName;
    /** 所屬用戶手機號 */
    private String buyerPhonenumber;
}
