package cc.carce.sale.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_log")
@Data
public class PaymentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String merchantTradeNo;  // 訂單編號
    private String rtnCode;          // 狀態碼
    private String rtnMsg;           // 狀態訊息
    private String tradeAmt;         // 交易金額
    private String paymentType;      // 付款方式
    private String tradeNo;          // 綠界交易序號

    @Lob
    private String rawParams;        // 完整回傳內容(JSON 或 key=value)

    private LocalDateTime createdAt = LocalDateTime.now();
}
