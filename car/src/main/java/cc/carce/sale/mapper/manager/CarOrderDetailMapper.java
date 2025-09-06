package cc.carce.sale.mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cc.carce.sale.entity.CarOrderDetailEntity;
import tk.mybatis.mapper.common.Mapper;

/**
 * 订单详情表Mapper
 */
public interface CarOrderDetailMapper extends Mapper<CarOrderDetailEntity> {
    
    /**
     * 插入订单详情
     */
    int insert(CarOrderDetailEntity orderDetail);
    
    /**
     * 根据ID查询订单详情
     */
    CarOrderDetailEntity selectById(@Param("id") Long id);
    
    /**
     * 根据订单ID查询订单详情列表
     */
    List<CarOrderDetailEntity> selectByOrderId(@Param("orderId") Long orderId);
    
    /**
     * 根据产品ID查询订单详情列表
     */
    List<CarOrderDetailEntity> selectByProductId(@Param("productId") Long productId);
    
    /**
     * 根据订单ID和产品ID查询订单详情
     */
    CarOrderDetailEntity selectByOrderIdAndProductId(@Param("orderId") Long orderId, @Param("productId") Long productId);
    
    /**
     * 更新订单详情
     */
    int updateById(CarOrderDetailEntity orderDetail);
    
    /**
     * 更新产品数量
     */
    int updateProductAmount(@Param("id") Long id, @Param("productAmount") Integer productAmount);
    
    /**
     * 更新产品价格
     */
    int updateProductPrice(@Param("id") Long id, @Param("productPrice") Integer productPrice);
    
    /**
     * 删除订单详情（逻辑删除）
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据订单ID删除所有订单详情（逻辑删除）
     */
    int deleteByOrderId(@Param("orderId") Long orderId);
    
    /**
     * 统计订单详情数量
     */
    int countByOrderId(@Param("orderId") Long orderId);
    
    /**
     * 统计产品在订单中的总数量
     */
    int sumProductAmountByProductId(@Param("productId") Long productId);
}
