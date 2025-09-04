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
@Table(name = "products_pimages")
public class ProductsPimagesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT UNSIGNED COMMENT '关联ID'")
    private Long id;

    @Column(name = "id_product", nullable = false, columnDefinition = "INT UNSIGNED COMMENT '商品ID'")
    private Long idProduct;

    @Column(name = "id_pimage", nullable = false, columnDefinition = "INT UNSIGNED COMMENT '图片ID'")
    private Long idPimage;
}
