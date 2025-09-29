package cc.carce.sale.mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cc.carce.sale.entity.CarOrderInfoEntity;
import tk.mybatis.mapper.common.Mapper;

/**
 * 订单主表Mapper
 */
public interface CarOrderInfoMapper extends Mapper<CarOrderInfoEntity> {
    
    /**
     * 插入订单信息
     */
    // int insert(CarOrderInfoEntity orderInfo);
    
    /**
     * 根据ID查询订单信息
     */
    // CarOrderInfoEntity selectById(@Param("id") Long id);
    
    /**
     * 根据订单号查询订单信息
     */
    CarOrderInfoEntity selectByOrderNo(@Param("orderNo") String orderNo);
    
    /**
     * 根据用户ID查询订单列表
     */
    List<CarOrderInfoEntity> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据订单状态查询订单列表
     */
    List<CarOrderInfoEntity> selectByOrderStatus(@Param("orderStatus") Integer orderStatus);
    
    /**
     * 根据用户ID和订单状态查询订单列表
     */
    List<CarOrderInfoEntity> selectByUserIdAndStatus(@Param("userId") Long userId, @Param("orderStatus") Integer orderStatus);
    
    /**
     * 更新订单信息
     */
    // int updateById(CarOrderInfoEntity orderInfo);
    
    /**
     * 更新订单状态
     */
    int updateOrderStatus(@Param("id") Long id, @Param("orderStatus") Integer orderStatus);
    
    /**
     * 更新物流信息
     */
    int updateLogisticsInfo(@Param("id") Long id, 
                           @Param("logicNumber") String logicNumber,
                           @Param("receiverName") String receiverName,
                           @Param("receiverMobile") String receiverMobile,
                           @Param("receiverAddress") String receiverAddress);
    
    /**
     * 删除订单（逻辑删除）
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 统计用户订单数量
     */
    int countByUserId(@Param("userId") Long userId);
    
    /**
     * 统计用户各状态订单数量
     */
    int countByUserIdAndStatus(@Param("userId") Long userId, @Param("orderStatus") Integer orderStatus);
}
