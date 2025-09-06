package cc.carce.sale.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "car_shopping_cart")
public class CarShoppingCartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) COMMENT '主键ID'")
    private Long id;

    @Column(name = "userId", nullable = false, columnDefinition = "BIGINT(20) COMMENT '用户ID'")
    private Long userId;

    @Column(name = "productId", nullable = false, columnDefinition = "BIGINT(20) COMMENT '产品ID'")
    private Long productId;

    @Column(name = "productAmount", nullable = false, columnDefinition = "INT(11) COMMENT '产品数量'")
    private Integer productAmount;

    @Column(name = "productPrice", columnDefinition = "DECIMAL(10,2) COMMENT '加购时的产品价格'")
    private Integer productPrice;

    @Column(name = "productName", columnDefinition = "VARCHAR(255) COMMENT '产品名称'")
    private String productName;

    @Column(name = "delFlag", columnDefinition = "BIT(1) DEFAULT b'0' COMMENT '删除标记 1 是 0 否'")
    private Boolean delFlag;

    @Column(name = "createTime", columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;

    @Column(name = "showOrder", columnDefinition = "INT(11) COMMENT '显示顺序'")
    private Integer showOrder;
}
