package cc.carce.sale.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class CarProductsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT UNSIGNED COMMENT '主键ID'")
    private Long id;

    @Column(name = "source", nullable = false, columnDefinition = "VARCHAR(20) COMMENT '商品來源'")
    private String source;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(20) COMMENT '商品名稱'")
    private String name;

    @Column(name = "alias", nullable = false, columnDefinition = "VARCHAR(50) COMMENT '商品別名'")
    private String alias;

    @Column(name = "model", nullable = false, columnDefinition = "VARCHAR(30) COMMENT '商品型號'")
    private String model;

    @Column(name = "market_price", nullable = false, columnDefinition = "INT UNSIGNED COMMENT '商品市價'")
    private Long marketPrice;

    @Column(name = "price", nullable = false, columnDefinition = "INT UNSIGNED COMMENT '商品售價'")
    private Long price;

    @Column(name = "id_supplier", nullable = false, columnDefinition = "INT UNSIGNED COMMENT '供應商id'")
    private Long idSupplier;

    @Column(name = "brand", nullable = false, columnDefinition = "VARCHAR(30) COMMENT '商品品牌'")
    private String brand;

    @Column(name = "tag", nullable = false, columnDefinition = "VARCHAR(10) COMMENT '商品類別(零件、工具、設備…)'")
    private String tag;

    @Column(name = "is_public", nullable = false, columnDefinition = "TINYINT(1) UNSIGNED COMMENT '是否發佈 0 否 / 1 是'")
    private Boolean isPublic;

    @Column(name = "memo", columnDefinition = "TEXT COMMENT '備註'")
    private String memo;

    @Column(name = "c_dt", nullable = false, columnDefinition = "DATETIME COMMENT '建立日期'")
    private Date cDt;

    @Column(name = "u_dt", nullable = false, columnDefinition = "DATETIME COMMENT '修改日期'")
    private Date uDt;
}
