package cc.carce.sale.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "car_brand")
public class CarBrandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT UNSIGNED COMMENT '主键ID'")
    private Long id;

    @Column(name = "brand", nullable = false, columnDefinition = "VARCHAR(50) COMMENT '品牌名称'")
    private String brand;
}
