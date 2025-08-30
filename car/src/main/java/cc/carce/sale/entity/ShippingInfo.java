package cc.carce.sale.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipping_info")
@Data
public class ShippingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNo;          // 對應 MerchantTradeNo
    private String shippingMethod;   // 配送方式 CVS/HOME
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String storeId;          // 超商取貨才需要
    private String logisticsStatus;  // 待出貨 / 配送中 / 已完成
    private String trackingNo;       // 託運單號 (綠界物流回傳)

    private LocalDateTime createdAt = LocalDateTime.now();
}
