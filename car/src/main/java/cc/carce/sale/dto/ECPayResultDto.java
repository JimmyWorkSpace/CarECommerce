package cc.carce.sale.dto;

import lombok.Data;

@Data
public class ECPayResultDto {
    
    /**
     * 特店編號
     */
    private String MerchantID;
    
    /**
     * 特店交易編號
     * 訂單產生時傳送給綠界的特店交易編號
     */
    private String MerchantTradeNo;
    
    /**
     * 特店旗下店舖代號
     */
    private String StoreID;
    
    /**
     * 綠界的交易編號
     */
    private String TradeNo;
    
    /**
     * 交易金額
     */
    private Integer TradeAmt;
    
    /**
     * 付款時間
     * 格式為yyyy/MM/dd HH:mm:ss
     */
    private String PaymentDate;
    
    /**
     * 交易付款方式
     */
    private String PaymentType;
    
    /**
     * 手續費合計
     */
    private Double HandlingCharge;
    
    /**
     * 交易手續費金額
     * 注意事項：2025/04/01 起改為交易手續費+交易處理費的總金額
     */
    private Double PaymentTypeChargeFee;
    
    /**
     * 訂單成立時間
     * 格式為yyyy/MM/dd HH:mm:ss
     */
    private String TradeDate;
    
    /**
     * 交易狀態
     * 若為0時，代表交易訂單成立未付款
     * 若為1時，代表交易訂單成立已付款
     * 若為 10200095時，代表交易訂單未成立，消費者未完成付款作業，故交易失敗
     * 
     * BNPL的TradeStatus狀態說明：
     * TradeStatus=0，消費者申請已受理
     * TradeStatus=1，消費者申請成功
     * TradeStatus=10200163，消費者申請失敗
     */
    private String TradeStatus;
    
    /**
     * 商品名稱
     */
    private String ItemName;
    
    /**
     * 自訂名稱欄位1
     * 提供合作廠商使用記錄用客製化使用欄位
     */
    private String CustomField1;
    
    /**
     * 自訂名稱欄位2
     * 提供合作廠商使用記錄用客製化使用欄位
     */
    private String CustomField2;
    
    /**
     * 自訂名稱欄位3
     * 提供合作廠商使用記錄用客製化使用欄位
     */
    private String CustomField3;
    
    /**
     * 自訂名稱欄位4
     * 提供合作廠商使用記錄用客製化使用欄位
     */
    private String CustomField4;
    
    /**
     * 檢查碼
     */
    private String CheckMacValue;
}
