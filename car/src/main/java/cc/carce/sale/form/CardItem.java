package cc.carce.sale.form;

import lombok.Data;

/**
 * 票券下單項（用於建立卡券支付訂單）
 */
@Data
public class CardItem {

    /** 票券方案ID */
    private Long cardId;

    /** 購買數量 */
    private Integer quantity;
}
