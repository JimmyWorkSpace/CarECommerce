package cc.carce.sale.dto;

import lombok.Data;

/**
 * 物流查询结果实体
 * 基于绿界科技物流查询API：https://developers.ecpay.com.tw/?p=7418
 */
@Data
public class LogisticsQueryResultDto {
    
    /**
     * 实际重量 (Number)
     * 当物流子类型为POST(中华邮政)才会回传
     * 上限20公斤，最多显示至小数3位，单位是公斤
     */
    private String ActualWeight;
    
    /**
     * 绿界科技的物流交易编号 (String(20))
     */
    private String AllPayLogisticsID;
    
    /**
     * 托运单号 (String(50))
     * 物流类型为HOME才会回传
     */
    private String BookingNote;
    
    /**
     * 物流代收款项拨款金额 (Int)
     * 注意：当收到CollectionAllocateAmount为0元时，不代表拨款金额为0元
     */
    private String CollectionAllocateAmount;
    
    /**
     * 物流代收款项拨款日期 (String(10))
     * 格式为：yyyy/MM/dd
     */
    private String CollectionAllocateDate;
    
    /**
     * 代收金额 (Int)
     */
    private String CollectionAmount;
    
    /**
     * 代收金额手续费 (Int)
     */
    private String CollectionChargeFee;
    
    /**
     * 寄货编号 (String(15))
     * 物流类型为CVS才会回传
     * 超商C2C配送编号请抓取此字段，7-ELEVEN需再抓取CVSValidationNo进行组合
     */
    private String CVSPaymentNo;
    
    /**
     * 验证码 (String(10))
     * (C2C) 7-ELEVEN才会回传
     * 注意：7-ELEVEN C2C 交货便代码为CVSPaymentNo与CVSValidationNo的组合
     */
    private String CVSValidationNo;
    
    /**
     * 商品金额 (Int)
     * 商品遗失赔偿依据
     */
    private String GoodsAmount;
    
    /**
     * 商品名称 (String(200))
     */
    private String GoodsName;
    
    /**
     * 商品重量 (Number)
     * 当物流子类型为POST(中华邮政)才会回传
     * 上限20公斤，最多显示至小数3位，单位是公斤
     */
    private String GoodsWeight;
    
    /**
     * 物流费用 (Int)
     */
    private String HandlingCharge;
    
    /**
     * 物流状态 (String(8))
     * 请参考物流状态代码一览表
     */
    private String LogisticsStatus;
    
    /**
     * 会员选择的物流方式 (String(20))
     * 请参考物流方式一览表
     */
    private String LogisticsType;
    
    /**
     * 厂商编号 (String(9))
     */
    private String MerchantID;
    
    /**
     * 厂商交易编号 (String(20))
     */
    private String MerchantTradeNo;
    
    /**
     * 寄件人手机 (String(10))
     */
    private String SenderCellPhone;
    
    /**
     * 寄件人姓名 (String(10))
     */
    private String SenderName;
    
    /**
     * 寄件人电话 (String(20))
     * 由于此参数在建单时为非必填，且申请物流服务时也非必填，所以可能回传空值
     */
    private String SenderPhone;
    
    /**
     * 物流运费扣款日期 (String(10))
     * 格式为：yyyy/MM/dd
     */
    private String ShipChargeDate;
    
    /**
     * 配送编号 (String(25))
     * 物流类型为CVS才会回传
     * 超商B2C配送编号请抓取此字段
     */
    private String ShipmentNo;
    
    /**
     * 订单成立时间 (String(20))
     * 格式为：yyyy/MM/dd HH:mm:ss
     */
    private String TradeDate;
    
    /**
     * 检查码 (String)
     * 请参考附录检查码机制
     */
    private String CheckMacValue;
}
