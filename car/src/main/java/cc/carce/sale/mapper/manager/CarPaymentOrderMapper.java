package cc.carce.sale.mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cc.carce.sale.entity.CarPaymentOrderEntity;
import tk.mybatis.mapper.common.Mapper;

/**
 * 支付订单Mapper
 */
public interface CarPaymentOrderMapper extends Mapper<CarPaymentOrderEntity> {
    
    /**
     * 插入支付订单
     */
    int insert(CarPaymentOrderEntity paymentOrder);
    
    /**
     * 根据ID查询支付订单
     */
    CarPaymentOrderEntity selectById(@Param("id") Long id);
    
    /**
     * 根据商户订单号查询支付订单
     */
    CarPaymentOrderEntity selectByMerchantTradeNo(@Param("merchantTradeNo") String merchantTradeNo);
    
    /**
     * 根据绿界支付交易号查询支付订单
     */
    CarPaymentOrderEntity selectByEcpayTradeNo(@Param("ecpayTradeNo") String ecpayTradeNo);
    
    /**
     * 根据用户ID查询支付订单列表
     */
    List<CarPaymentOrderEntity> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 更新支付订单
     */
    int updateById(CarPaymentOrderEntity paymentOrder);
    
    /**
     * 更新支付状态
     */
    int updatePaymentStatus(@Param("id") Long id, 
                           @Param("paymentStatus") Integer paymentStatus,
                           @Param("ecpayStatus") String ecpayStatus,
                           @Param("ecpayStatusDesc") String ecpayStatusDesc,
                           @Param("paymentTime") java.util.Date paymentTime);
    
    /**
     * 根据支付状态查询订单列表
     */
    List<CarPaymentOrderEntity> selectByPaymentStatus(@Param("paymentStatus") Integer paymentStatus);
    
    /**
     * 查询待支付的订单（用于定时任务查询状态）
     * 查询条件：支付状态为待支付(0)，创建时间在指定时间范围内，未删除
     */
    List<CarPaymentOrderEntity> selectPendingOrdersForQuery();
    
    /**
     * 删除支付订单（逻辑删除）
     */
    int deleteById(@Param("id") Long id);
}
