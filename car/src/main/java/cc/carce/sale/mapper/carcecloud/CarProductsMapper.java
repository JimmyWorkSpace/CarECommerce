package cc.carce.sale.mapper.carcecloud;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cc.carce.sale.entity.CarProductsEntity;
import cc.carce.sale.entity.PimagesEntity;
import tk.mybatis.mapper.common.Mapper;

public interface CarProductsMapper extends Mapper<CarProductsEntity> {
    
    /**
     * 根据商品ID查询商品的所有图片ID
     */
    List<Long> selectImageIdsByProductId(@Param("productId") Long productId);
    
    /**
     * 根据商品ID查询商品的所有图片信息
     */
    List<PimagesEntity> selectImagesByProductId(@Param("productId") Long productId);
}
