package com.ruoyi.car.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

import lombok.Data;

/**
 * 票券明細實體：關聯訂單，存儲每張票券的唯一碼及核銷狀態
 *
 * @author ruoyi
 */
@Data
@Table(name = "car_card_detail")
public class CarCardDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "orderId")
    @Excel(name = "訂單ID")
    private Long orderId;

    @Column(name = "cardId")
    @Excel(name = "票券方案ID")
    private Long cardId;

    @Column(name = "cardName")
    @Excel(name = "票券名稱")
    private String cardName;

    @Column(name = "ticketCode")
    @Excel(name = "票券唯一碼")
    private String ticketCode;

    @Column(name = "redeemed")
    @Excel(name = "是否核銷", readConverterExp = "0=未核銷,1=已核銷")
    private Integer redeemed;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "redeemedTime")
    @Excel(name = "核銷時間", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date redeemedTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "createTime")
    @Excel(name = "建立時間", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name = "redeemedUserId")
    @Excel(name = "核銷人ID")
    private Long redeemedUserId;
}
