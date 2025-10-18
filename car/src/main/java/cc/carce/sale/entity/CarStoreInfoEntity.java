package cc.carce.sale.entity;

import lombok.Data;
import javax.persistence.*;

/**
 * 门店列表实体
 */
@Data
@Table(name = "car_store_list")
public class CarStoreInfoEntity {
    
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20) COMMENT '主键ID'")
    private Long id;
    
    /**
     * 便利店类型
     */
    @Column(name = "CvsType", columnDefinition = "VARCHAR(50) COMMENT '便利店类型'")
    private String cvsType;
    
    /**
     * 门店ID
     */
    @Column(name = "StoreId", columnDefinition = "BIGINT(20) COMMENT '门店ID'")
    private Long storeId;
    
    /**
     * 门店名称
     */
    @Column(name = "StoreName", columnDefinition = "VARCHAR(255) COMMENT '门店名称'")
    private String storeName;
    
    /**
     * 门店地址
     */
    @Column(name = "StoreAddr", columnDefinition = "VARCHAR(2000) COMMENT '门店地址'")
    private String storeAddr;
    
    /**
     * 门店电话
     */
    @Column(name = "StorePhone", columnDefinition = "VARCHAR(50) COMMENT '门店电话'")
    private String storePhone;
}
