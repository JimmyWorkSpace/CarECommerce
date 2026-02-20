package cc.carce.sale.form;

import lombok.Data;

import java.util.List;

/**
 * 建立卡券支付訂單表單（對接金流，不需地址）
 */
@Data
public class CreateCardPaymentForm {

    /** 票券項目列表 */
    private List<CardItem> cardData;
}
