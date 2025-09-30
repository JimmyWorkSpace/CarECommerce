package cc.carce.sale.dto;

import lombok.Data;

/**
 * 绿界支付回调结果DTO
 */
@Data
public class ECPayReturnResultDto {
    
    private String CustomField1;
    private String CustomField2;
    private String CustomField3;
    private String CustomField4;
    private String MerchantID;
    private String MerchantTradeNo;
    private String PaymentDate;
    private String PaymentType;
    private String PaymentTypeChargeFee;
    private String RtnCode;
    private String RtnMsg;
    private String SimulatePaid;
    private String StoreID;
    private String TradeAmt;
    private String TradeDate;
    private String TradeNo;
    private String CheckMacValue;
}
